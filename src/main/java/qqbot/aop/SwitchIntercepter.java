package qqbot.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;

import qqbot.anno.Switch;
import qqbot.bean.QQGroup;
import qqbot.service.GroupService;
import qqbot.utils.FunctionType;
import qqbot.utils.MsgUtil;

@Aspect
@Component
@Order
public class SwitchIntercepter {
	
	@Autowired
	private GroupService groupService;


	@Around("@annotation(qqbot.anno.Switch)")
	public void doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			GroupMsg groupMsg = (GroupMsg) joinPoint.getArgs()[0];
			MsgSender sender = (MsgSender) joinPoint.getArgs()[1];
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			Switch annotation = method.getAnnotation(Switch.class);
			// 开关哪些功能
			FunctionType[] types = annotation.value();
			// 是开还是关的标记
			boolean state = annotation.state();
//			// 查询发送命令的人
//			boolean isMember = groupMsg.getPowerType().isMember();
//			// 判断是不是管理员
//			if (isMember) {
//				// 不是则返回
//				MsgUtil.send(groupMsg, sender, "您的权限不足！");
//				return;
//			}
//			// 是管理员可以更改群功能状态
			// 查询群详细信息
			Long groupId = groupMsg.getGroupCodeNumber();
			QQGroup group = groupService.findByGroupId(groupId);
			// 群信息不存在则新增
			if(group == null) {
				group = QQGroup.defaultQQGroup(groupId);
				group = groupService.save(group);
			}
			// 修改功能状态
			for (FunctionType type : types) {
				// 如果是要开
				if(state) {
					group.addPrivilege(type.getPrivilege());
					groupService.save(group);
					MsgUtil.send(groupMsg, sender, type.getDescribe()+"功能已开启");
				}
				// 如果是关
				else {
					group.removePrivilege(type.getPrivilege());
					groupService.save(group);
					MsgUtil.send(groupMsg, sender, type.getDescribe()+"功能已关闭");
				}
				// 保存状态
			}
			joinPoint.proceed();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}