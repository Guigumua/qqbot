package qqbot.listener;

import org.springframework.stereotype.Component;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.ListenBreak;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

@Component
public class AnimeListener {

	@Listen(value = { MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter("^anime.*CQ:image.*")
	@ListenBreak
	public void anime(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		
	}
}
