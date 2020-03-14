package qqbot.listener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.ListenBreak;
import com.forte.qqrobot.beans.messages.msgget.GroupAddRequest;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;

import qqbot.anno.Checked;
import qqbot.bean.Member;
import qqbot.service.MemberService;
import qqbot.utils.Constants;
import qqbot.utils.FunctionType;
import qqbot.utils.MsgUtil;
import qqbot.utils.Privileges;

@Component
public class AdminListener {
	@Autowired
	private MemberService memberService;

	@Listen(MsgGetTypes.groupAddRequest)
	public void groupAdd(GroupAddRequest request, MsgSender sender) {
		if (request.getQQ().contentEquals("${core.localQQCode}")) {
			MsgUtil.groupAdd(request, sender);
		} else {
			sender.SETTER.setGroupAddRequestDisagree(request, "只有管理员可以邀请bot进群");
		}
	}

	@Listen({ MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter("^help$")
	public void help(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender) {
		MsgUtil.send(groupMsg, privateMsg, sender, Constants.HELP_MSG);
	}

	@Listen({ MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter("^chmod\\s\\d+\\s\\d{1,2}$")
	@ListenBreak
	@Checked(FunctionType.ADMIN)
	public void changePrivileges(MsgGet msgGet, MsgSender sender) {
		String msg = msgGet.getMsg();
		long qq = Long.parseLong(StringUtils.substringBetween(msg, " ", " "));
		int privileges = Integer.parseInt(StringUtils.substringAfterLast(msg, " "));
		Member member = memberService.findById(qq);
		if (privileges > (Privileges.IMAGE | Privileges.SETU | Privileges.PIXIV)) {
			MsgUtil.send(msgGet, sender, "你输入的权限不正确！");
			return;
		}
		if (member.hasPrivilege(Privileges.ADMIN | Privileges.SUPER_ADMIN)) {
			MsgUtil.send(msgGet, sender, "你不可以更改管理员的权限");
			return;
		}
		memberService.setPrivileges(member, privileges);
		MsgUtil.send(msgGet, sender, "更改" + qq + "的权限成功！");
	}

	@Listen({ MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter("^admin\\s\\d+$")
	@ListenBreak
	@Checked(FunctionType.SUPER_ADMIN)
	public void admin(MsgGet msgGet, MsgSender sender) {
		String msg = msgGet.getMsg();
		long qq = Long.parseLong(StringUtils.substringBetween(msg, " ", " "));
		Member member = memberService.findById(qq);
		member.addPrivilege(Privileges.ADMIN);
		memberService.save(member);
		MsgUtil.send(msgGet, sender, "添加管理员成功");
	}
}
