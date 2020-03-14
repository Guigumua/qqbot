package qqbot.utils;

public class Privileges {
	public static final int SETU = 1;
	public static final int IMAGE = 1 << 1;
	public static final int PIXIV = 1 << 2;
	public static final int ADMIN = 1 << 15 - 1;
	public static final int SUPER_ADMIN = 1 << 16 | PIXIV | ADMIN | SETU | IMAGE;
}
