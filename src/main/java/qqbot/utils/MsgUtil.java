package qqbot.utils;

import com.forte.qqrobot.beans.messages.msgget.GroupAddRequest;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

import lombok.extern.slf4j.Slf4j;
import qqbot.bean.ImageTag;

@Slf4j
public class MsgUtil {
	public static boolean send(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, String msg) {
		boolean flag;
		try {
			if (groupMsg == null) {
				flag = sender.SENDER.sendPrivateMsg(privateMsg, msg);
				log.info("发送私聊消息给{}成功", privateMsg.getQQ());
			} else {
				flag = sender.SENDER.sendGroupMsg(groupMsg, msg);
				log.info("发送群聊消息给{}成功", groupMsg.getGroup());
			}
		} catch (Exception e) {
			log.info(e.toString());
			return false;
		}

		return flag;
	}

	public static boolean send(GroupMsg groupMsg, MsgSender sender, String msg) {
		boolean flag = sender.SENDER.sendGroupMsg(groupMsg, msg);
		log.info("发送群聊消息给{}成功", groupMsg.getGroup());
		return flag;
	}

	public static boolean send(GroupMsg groupMsg, MsgSender sender, ImageTag imageTag) {
		boolean flag = sender.SENDER.sendGroupMsg(groupMsg,
				CQCodeUtil.build().getCQCode_Image(imageTag.getFile()).toString());
		log.info("发送群聊消息给{}成功", groupMsg.getGroup());
		return flag;
	}

	public static boolean send(PrivateMsg privateMsg, MsgSender sender, String msg) {
		boolean flag = sender.SENDER.sendPrivateMsg(privateMsg, msg);
		log.info("发送群聊消息给{}成功", privateMsg.getQQ());
		return flag;
	}

	public static boolean groupAdd(GroupAddRequest request, MsgSender sender) {
		boolean isAdmin = request.getQQ().equals("1522204732");
		boolean flag;
		if (isAdmin) {
			flag = sender.SETTER.setGroupAddRequestAgree(request);
		} else {
			flag = sender.SETTER.setGroupAddRequest(request, false, "只有机器人管理员才能邀请进群");
		}
		return flag;
	}

	public static boolean send(Long groupId, MsgSender sender, String msg) {
		return sender.SENDER.sendGroupMsg(groupId.toString(), msg);
	}

	public static String getMsg(GroupMsg groupMsg, PrivateMsg privateMsg) {
		if (groupMsg == null) {
			return privateMsg.getMsg();
		} else {
			return groupMsg.getMsg();
		}
	}

	public static void send(MsgGet msgGet, MsgSender sender, String msg) {
		if (msgGet instanceof GroupMsg) {
			MsgUtil.send((GroupMsg) msgGet, sender, msg);
		} else {
			MsgUtil.send((PrivateMsg) msgGet, sender, msg);
		}

	}

}
