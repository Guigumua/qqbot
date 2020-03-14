package qqbot.listener;

import org.springframework.stereotype.Component;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.ListenBreak;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.MostType;
import com.forte.qqrobot.sender.MsgSender;

import qqbot.anno.Checked;
import qqbot.anno.Switch;
import qqbot.utils.Constants;
import qqbot.utils.FunctionType;
import qqbot.utils.ImageUtil;
import qqbot.utils.MsgUtil;

@Component
//@Slf4j
public class SetuListener {

	@Checked(value = FunctionType.SETU)
	@Listen({ MsgGetTypes.privateMsg, MsgGetTypes.groupMsg })
	@Filter("^setu$")
	@ListenBreak
	public void setu(MsgGet msg, MsgSender sender) throws Exception {
		GroupMsg groupMsg = null;
		PrivateMsg privateMsg = null;
		if (msg instanceof GroupMsg) {
			groupMsg = (GroupMsg) msg;
		} else {
			privateMsg = (PrivateMsg) msg;
		}
//		CQCode code = ImageUtil.getCQCodeFromRandom();
		CQCode code = ImageUtil.downloadRandomAndReturn();
		MsgUtil.send(groupMsg, privateMsg, sender, code.toString());
	}

	@Listen(MsgGetTypes.groupMsg)
	@Filter(value = { "^setu off$", "^色图关闭$", "^川川不要发色图了$" }, mostType = MostType.ANY_MATCH)
	@Switch(value = { FunctionType.SETU }, state = Constants.SWITCH_OFF)
	@Checked(FunctionType.ADMIN)
	public void setuOff(GroupMsg groupMsg, MsgSender sender) {
	}

	@Checked(FunctionType.ADMIN)
	@Listen(MsgGetTypes.groupMsg)
	@Filter(value = { "^setu on$", "^色图开启$", "^川川我要看色图$" }, mostType = MostType.ANY_MATCH)
	@Switch(value = { FunctionType.SETU }, state = Constants.SWITCH_ON)
	public void setuOn(GroupMsg groupMsg, MsgSender sender) {
	}

}
