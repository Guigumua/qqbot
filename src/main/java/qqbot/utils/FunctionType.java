package qqbot.utils;

import lombok.Getter;

public enum FunctionType {

	SETU(Privileges.SETU,"色图"), PIXIV(Privileges.PIXIV,"p站相关"), IMAGE(Privileges.IMAGE,"图片上传查看"), ADMIN(Privileges.ADMIN,"管理员"),
	SUPER_ADMIN(Privileges.SUPER_ADMIN,"超级管理员");

	@Getter
	private int privilege;
	
	@Getter
	private String describe;

	private FunctionType(int privilege,String describe) {
		this.privilege = privilege;
		this.describe = describe;
	}

}
