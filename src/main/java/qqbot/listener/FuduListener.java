package qqbot.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;

import qqbot.utils.MsgEntry;
import qqbot.utils.MsgUtil;

@Component
public class FuduListener {

	public static Map<String, MsgEntry> cache = new HashMap<>();

	@Listen(MsgGetTypes.groupMsg)
	public void fudu(GroupMsg groupMsg, MsgSender sender) {
		String group = groupMsg.getGroup();
		// 将新的消息缓存起来，并得到旧的消息
		MsgEntry entry = cache.put(group, new MsgEntry(groupMsg, 1));
		if (entry == null) {
			return;
		}
		// 有群消息
		GroupMsg msg = entry.getKey();
		Integer times = entry.getValue();
		// 判断消息内容和发送人是否与上次的相同
		if (msg.getMsg().equals(groupMsg.getMsg())) {
			if (!msg.getQQ().equals(groupMsg.getQQ())) {
				return;
			}
			// 消息内容与上次相同，发送者不同，判断消息是第几次出现
			// 不是第一次说明已经被复读过
			if (times > 1) {
				return;
			}
			// 消息还没有被复读
			MsgUtil.send(groupMsg, sender, msg.getMsg());
			// 修改状态
			entry.setValue(times + 1);
			cache.put(group, entry);
		}
		// 消息与上次的不相同
		else {
			//TODO
//			if (times > 3) {
//				MsgUtil.send(groupMsg, sender, "");
//			}
		}
	}
}
