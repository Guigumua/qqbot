package qqbot.listener;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.ListenBreak;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

import lombok.extern.slf4j.Slf4j;
import qqbot.bean.ImageTag;
import qqbot.service.ImageTagService;
import qqbot.utils.MsgUtil;

@Component
@Slf4j
public class ImageListener {

//	@Autowired
//	private ImageService imgService;

	@Autowired
	private ImageTagService imageTagService;

	@Listen({ MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter(value = "^img.*CQ:image.*")
	public void imgUp(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		String msg = MsgUtil.getMsg(groupMsg, privateMsg);
		List<CQCode> codes = util.getCQCodeFromMsg(msg);
		codes.forEach(code -> {
			String[] tags = StringUtils.split(StringUtils.substringBetween(msg, "g", "["));
			if (tags == null) {
				MsgUtil.send(groupMsg, privateMsg, sender, "请至少包含一个tag！");
				return;
			}
			Arrays.asList(tags).forEach(tag -> {
				ImageTag imageTag = new ImageTag();
				imageTag.setFile(code.get("file"));
				imageTag.setTag(tag);
				ImageTag _imageTag = imageTagService.save(imageTag);
				MsgUtil.send(groupMsg, privateMsg, sender,
						"图片" + _imageTag.getFile() + "保存到" + _imageTag.getTag() + ":保存成功");
			});
		});
	}

	@Listen({ MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter(value = "^img$")
	@ListenBreak
	public void imgHelp(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
		MsgUtil.send(groupMsg, privateMsg, sender, "$代表着是一个参数！\n" + "img $tag $image:上传图片，至少要有一个tag和一张图片 \n"
				+ "img $tag:查看一张tag的图片\n" + "命令中的空格可有可无，但必须以img开头！");
	}

//	@Filter("^img up.*")
//	public void imgUp(GroupMsg groupMsg, MsgSender sender, CQCodeUtil util) {
//		List<CQCode> codeList = util.getCQCodeFromMsg(groupMsg.getMsg());
//		codeList.forEach(code -> {
//			String[] tags = StringUtils.split(StringUtils.substringBetween(groupMsg.getMsg(), "p", "["));
//			Arrays.asList(tags).forEach(tag -> {
//				ImageTag id = new Image.ImageTag();
//				id.setFile(code.get("file"));
//				id.setTagName(tag);
//				Image image = new Image();
//				image.setImageTag(id);
//				Image _image = imgService.save(image);
//				MsgUtil.send(groupMsg, sender, "图片" + _image.getFile() + "保存到" + _image.getTagName() + ":保存成功");
//			});
//		});
//	}

	@Listen({ MsgGetTypes.groupMsg, MsgGetTypes.privateMsg })
	@Filter("^image((?!CQ).)*$*")
	public void imgGet(GroupMsg groupMsg, PrivateMsg privateMsg, MsgSender sender, CQCodeUtil util) {
//		log.info("监听到群{}的图片请求消息:{}", groupMsg.getGroup(), groupMsg.getMsg());
		String msg = MsgUtil.getMsg(groupMsg, privateMsg);
		String tag = StringUtils.trim(StringUtils.substringAfter(msg, "g"));

		if ("".equals(tag)) {
			MsgUtil.send(groupMsg, privateMsg, sender, "没有输入tag!");
			return;
		}

		List<ImageTag> list = imageTagService.findByTag(tag);
		if (list.size() == 0) {
			MsgUtil.send(groupMsg, privateMsg, sender, "没有这个tag的图片！");
			return;
		}
		ImageTag imageTag = list.get(RandomUtils.nextInt(0, list.size()));
		try {
			MsgUtil.send(groupMsg, privateMsg, sender, util.getCQCode_Image(imageTag.getFile()).toString());
		} catch (Exception e) {
			log.error("图片发送出现异常，异常原因:{}", e.getCause());
		}
	}
//	@Listen(MsgGetTypes.groupMsg)
//	@Filter("^img\\s*\\S+")
//	public void imgGet(GroupMsg groupMsg, MsgSender sender, CQCodeUtil util) {
//		log.info("监听到群{}的图片请求消息:{}", groupMsg.getGroup(), groupMsg.getMsg());
//		String tag = StringUtils.trim(StringUtils.substringAfter(groupMsg.getMsg(), "g"));
//		Image image = imgService.findRandOneByTagName(tag);
//		if (image == null) {
//			MsgUtil.send(groupMsg, sender, "没有这个tag的图片");
//			return;
//		}
//		MsgUtil.send(groupMsg, sender, util.getCQCode_Image(image.getFile()).toString());
//		log.info("发送tag为{}的图片", tag);
//	}
}
