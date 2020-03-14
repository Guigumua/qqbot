package qqbot.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

import qqbot.anno.Checked;
import qqbot.bean.Member;
import qqbot.bean.QQGroup;
import qqbot.service.GroupService;
import qqbot.service.MemberService;
import qqbot.utils.FunctionType;
import qqbot.utils.MsgUtil;

@Aspect
@Component
public class FunctionInterceptor {

	@Autowired
	private GroupService groupService;

	@Autowired
	private MemberService mService;

	// 检查调用命令所需权限
	/**
	 * 
	 */
	@Around("@annotation(qqbot.anno.Checked)")
	public void doAroud(ProceedingJoinPoint joinPoint) throws Throwable {
		Object object = joinPoint.getArgs()[0];
		GroupMsg groupMsg = null;
		PrivateMsg privateMsg = null;
		MsgSender sender = (MsgSender) joinPoint.getArgs()[1];
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Checked check = method.getAnnotation(Checked.class);
		FunctionType[] types = check.value();
		// 消息是否是群消息
		if (object instanceof GroupMsg) {
			// 是
			groupMsg = (GroupMsg) object;
			// 去数据库里找群信息
			Long groupId = groupMsg.getGroupCodeNumber();
			QQGroup group = groupService.findByGroupId(groupId);
			// 群信息还不存在 则插入一个默认的群
			if (group == null) {
				group = QQGroup.defaultQQGroup(groupId);
				groupService.save(group);
			}
			
			// 检查群是否包含所有权限
			for (FunctionType type : types) {
				boolean hasPrivilege = group.hasPrivilege(type.getPrivilege());
				// 一旦没有权限，返回失败信息
				if (!hasPrivilege) {
					MsgUtil.send(groupMsg, sender, type.getDescribe() + "功能已关闭");
					return;
				}
			}
			// 权限通过，查询调用者是否有权限
			// 先查询是否存在于数据库
			Long qq = groupMsg.getQQCodeNumber();
			// 如果数据库中不存在，会构建一个默认的对象
			Member member = mService.findById(qq);
			if (member == null) {
				// 没有则插入
				member = Member.defaultMember(qq);
				member = mService.save(member);
			}
			for (FunctionType type : types) {
				boolean hasPrivilege = member.hasPrivilege(type.getPrivilege());
				// 一旦没有权限，返回失败信息
				if (!hasPrivilege) {
					MsgUtil.send(groupMsg, sender, CQCodeUtil.build().getCQCode_At(qq.toString()).toString() + "你没有权限调用"
							+ type.getDescribe() + "功能");
					return;
				}
			}
		} else if(object instanceof PrivateMsg){
			// 是私聊消息
			privateMsg = (PrivateMsg) object;
			Long qq = privateMsg.getQQCodeNumber();
			Member member = mService.findById(qq);
			for (FunctionType type : types) {
				boolean hasPrivilege = member.hasPrivilege(type.getPrivilege());
				// 一旦没有权限，返回失败信息
				if (!hasPrivilege) {
					MsgUtil.send(privateMsg, sender, "你没有权限调用" + type.getDescribe() + "功能");
					return;
				}
			}
		}
		joinPoint.proceed();
	}
}
