package qqbot.utils;

import java.util.Map.Entry;

import com.forte.qqrobot.beans.messages.msgget.GroupMsg;

public class MsgEntry implements Entry<GroupMsg, Integer> {

	private GroupMsg groupMsg;
	private Integer count;

	public MsgEntry(GroupMsg groupMsg, Integer count) {
		this.groupMsg = groupMsg;
		this.count = count;
	}

	@Override
	public GroupMsg getKey() {
		return groupMsg;
	}

	@Override
	public Integer getValue() {
		return count;
	}

	@Override
	public Integer setValue(Integer count) {
		Integer oldValue = this.count;
		this.count = count;
		return oldValue;
	}

}