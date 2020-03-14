package qqbot.utils;

import org.apache.commons.lang3.StringUtils;

public class Constants {
	/* 文件保存地址 */
	public static final String SAVE_PATH;
	public static final String HELP_MSG = "阿川机器人-version:1.0.1 \n" + "setu:随机setu\n" + "img:图片上传和查看功能 \n"
			+ "pixiv:检索图片信息，关键字搜图，pid查询图片详情\n" + "help:查看功能\n" + "以上命令全部支持私聊和群聊，请注意节制使用，避免影响他人！";
	/* 各种接口 */
	public static final String PRE_URL = "https://search.pstatic.net/common?type=origin&src=";
	public static final String PIXIV_PROXY = "https://pixiv.cat/";
	public static final String PIXIV_API = "https://api.imjad.cn/pixiv/v2/";
	public static final String BIZHI_API = "http://api.btstu.cn/sjbz/api.php";
	public static final String SEARCH_API = "https://saucenao.com/search.php";
	public static final String SETU_API = "https://api.lolicon.app/setu/";
	public static final String API_KEY = "5e670c1065c5e472933258";
	public static final String ANIME_SEARCH_API = "https://trace.moe/search/";

	/* 一些参数 */
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

	/* 命令列表 */
	public static final String COMMAND_PRE_IMG = "img";
	public static final String COMMAND_PRE_PIXIV = "pixiv";
	public static final String COMMAND_PRE_HELP = "help";
	public static final String COMMAND_PRE_SETU = "setu";
	public static final String COMMAND_PRE_ADMIN = "bot";
	
	public static final boolean SWITCH_OFF = false;
	public static final boolean SWITCH_ON = true;

	static {
		String osname = System.getProperty("os.name");
		if (StringUtils.contains(osname, "Windows")) {
			SAVE_PATH = "D:/software/coolq/data/image/";
		} else if (StringUtils.contains(osname, "Linux")) {
			SAVE_PATH = "/usr/local/docker/coolq/data/image/";
		} else {
			SAVE_PATH = "";
		}
	}
}
