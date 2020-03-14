package qqbot.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.ListenBreak;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.beans.types.MostType;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

import lombok.extern.slf4j.Slf4j;
import qqbot.anno.Checked;
import qqbot.anno.Switch;
import qqbot.bean.ImageSearchResult;
import qqbot.utils.Constants;
import qqbot.utils.FunctionType;
import qqbot.utils.ImageUtil;
import qqbot.utils.MsgUtil;

@Slf4j
@Component
public class PixivListener {
	
	@Checked(FunctionType.PIXIV)
	@Listen(value = { MsgGetTypes.privateMsg, MsgGetTypes.groupMsg })
	@Filter("^pixiv\\s*\\d+")
	public void pixivForPid(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		String msg = MsgUtil.getMsg(groupMsg, privateMsg);
		String pid = StringUtils.deleteWhitespace(StringUtils.substringAfter(msg, "v"));
		String filename = pid + ".jpg";
		CQCode code = ImageUtil.getCQCodeFromUrl(Constants.PRE_URL + Constants.PIXIV_PROXY + filename);
		try {
			MsgUtil.send(groupMsg, privateMsg, sender, code.toString());
			log.info("图片发送成功！{}", msg);
		} catch (Exception e) {
			log.error("图片发送失败！错误：{}", e);
			MsgUtil.send(groupMsg, privateMsg, sender, "查询失败，该画廊可能已经被删除！");
			
		}
	}
	@Checked(FunctionType.PIXIV)
	@Listen(value = { MsgGetTypes.privateMsg, MsgGetTypes.groupMsg })
	@Filter(value = "^pixiv.*CQ.*")
	public void pixivSearchByImage(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		String msg = MsgUtil.getMsg(groupMsg, privateMsg);
		List<CQCode> codes = util.getCQCodeFromMsg(msg);
		if (codes.size() == 0) {
			MsgUtil.send(groupMsg, privateMsg, sender, "至少要一张图片！");
			return;
		}
		codes.forEach(code -> {
			String url = code.getParam("url");
			ImageSearchResult result = ImageUtil.searchFromSauceNao(url);
			MsgUtil.send(groupMsg, privateMsg, sender, result.toString());
		});
	}
	@Checked(FunctionType.PIXIV)
	@Listen(value = { MsgGetTypes.privateMsg, MsgGetTypes.groupMsg },sort = 1)
	@Filter(value = "^pixiv$")
	@ListenBreak
	public void pixivHelp(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		MsgUtil.send(groupMsg, privateMsg, sender, 
				  "pixiv $image:以图搜详细信息，至少要一张图片\n"
				+ "pixiv $pid:以pid查询一张图片，如果图片被删除，将可能不会返回任何信息！\n"
				+ "pixiv $keyword:以keyword为关键字查询一张图片\n"
				+ "powered by:https://pixiv.cat/ and https://saucenao.com/");
	}
	
	@Listen(value = { MsgGetTypes.privateMsg, MsgGetTypes.groupMsg })
	@Filter(value = "^^pixiv((?!CQ:image).)*$")
	public void pixivSearchByKeyword(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		String msg = MsgUtil.getMsg(groupMsg, privateMsg);
		String word = StringUtils.deleteWhitespace(StringUtils.substring(msg,5));
		ImageSearchResult result = ImageUtil.searchByWord(word);
		String url = result.getUrl();
		if(url == null) {
			MsgUtil.send(groupMsg, privateMsg, sender, result.toString());
			return;
		}
		String proxyUrl = StringUtils.replace(url, "ximg.net/c/600x1200_90_webp","ixiv.cat");
		CQCode code = ImageUtil.getCQCodeFromUrl(Constants.PRE_URL+proxyUrl);
		MsgUtil.send(groupMsg, privateMsg, sender, code.toString());
	}
	@ListenBreak
	@Listen(MsgGetTypes.groupMsg)
	@Filter(value = { "^pixiv off$", "^p站不要啊$" }, mostType = MostType.ANY_MATCH)
	@Switch(value = { FunctionType.PIXIV }, state = Constants.SWITCH_OFF)
	public void pixivOff(GroupMsg groupMsg, MsgSender sender) {
		System.out.println("pixiv功能关闭");
		MsgUtil.send(groupMsg, sender, "pixiv功能已关闭");
	}
	@ListenBreak
	@Listen(MsgGetTypes.groupMsg)
	@Filter(value = { "^pixiv on$", "给大爷打开p站功能" }, mostType = MostType.ANY_MATCH)
	@Switch(value = { FunctionType.PIXIV }, state = Constants.SWITCH_ON)
	public void pixivOn(GroupMsg groupMsg, MsgSender sender) {
		System.out.println("pixiv功能启动");
		MsgUtil.send(groupMsg, sender, "pixiv功能已开启");
	}

}
