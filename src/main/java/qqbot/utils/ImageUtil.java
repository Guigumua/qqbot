package qqbot.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.types.CQCodeTypes;
import com.forte.qqrobot.utils.CQCodeUtil;

import lombok.extern.slf4j.Slf4j;
import qqbot.bean.ImageSearchResult;

@Slf4j
public class ImageUtil {
	/**
	 * 下载图片
	 * 
	 * @param url      下载链接
	 * @param savePath 保存路径
	 * @param filename 文件名
	 * @throws Exception 保存失败
	 */
	private static Map<String, List<ImageSearchResult>> cache = new HashMap<>();
	private static Map<String, Integer> pageCache = new HashMap<>();
	private static String filterTags = "r18R18r-18R-18R-18Gr-18G";

	public static void download(String url, String savePath, String filename) throws Exception {
		filename = savePath + filename;
		File file = new File(filename);
		if (file.exists()) {
			return;
		}
		// 构造URL
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		conn.connect();
		// 打开连接
		URLConnection con = _url.openConnection();
		// 输入流
		InputStream in = con.getInputStream();

		Path path = file.toPath();
		Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
//		// 1K的数据缓冲
//		byte[] bs = new byte[1024];
//		// 读取到的数据长度
//		int len;
//		// 输出的文件流
////	       String path = "D:\\software\\CoolQ\\data\\image\\" + filename + ".jpg";  //下载路径及下载图片名称
//		FileOutputStream os = new FileOutputStream(file, true);
//		// 开始读取
//		while ((len = in.read(bs)) != -1) {
//			os.write(bs, 0, len);
//		}
//		// 完毕，关闭所有链接
//		os.close();
		in.close();
	}

	/**
	 * 获取随机色图url
	 * 
	 * @return 随机色图url
	 */
	public static String getRandomImageUrl() {
		String json = HttpUtil.get(Constants.SETU_API, "apikey=" + Constants.API_KEY);
		String url = ((JSONObject) JSONObject.parseObject(json).getJSONArray("data").get(0)).getString("url");
		return Constants.PRE_URL + url;
	}

	public static String getImageFilenameFromUrl(String url) {
		String filename = StringUtils.substringAfterLast(url, "/");
		if (StringUtils.contains(filename, "p0")) {
			filename = StringUtils.remove(filename, "_p0");
		} else {
			filename = StringUtils.replace(filename, "_p", "-");
		}
		return filename;
	}

	public static CQCode downloadRandomAndReturn() throws Exception {
		String url = ImageUtil.getRandomImageUrl();
		String filename = ImageUtil.getImageFilenameFromUrl(url);
		ImageUtil.download(url, Constants.SAVE_PATH, filename);
		CQCode code = CQCodeUtil.build().getCQCode_Image(filename);
		return code;
	}

	public static CQCode getCQCodeFromUrl(String url) {
		
		HashMap<String, String> params = new HashMap<>();
		params.put("file", url);
		CQCode code = CQCode.of(CQCodeTypes.image, params);
		log.info("获取图片{}的cqcode成功", code);
		return code;
	}

	public static CQCode getCQCodeFromRandom() {
		return getCQCodeFromUrl(ImageUtil.getRandomImageUrl());
	}

	public static ImageSearchResult searchFromSauceNao(String url) {
		Document document;
		try {
			document = Jsoup.connect("https://saucenao.com/search.php").data("url", url).userAgent(Constants.USER_AGENT)
					.post();
			Elements titles = document.getElementsByClass("resulttitle");
			Elements contents = document.getElementsByClass("resultcontentcolumn");
			String _url = contents.get(0).child(1).attr("href");
			String title = titles.get(0).child(0).text();
			String pid = contents.get(0).child(1).text();
			String member = contents.get(0).child(5).text();
			ImageSearchResult result = new ImageSearchResult();
			result.setUrl(_url);
			result.setTitle(title);
			result.setPid(pid);
			result.setMember(member);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return ImageSearchResult.DEFAULT_RESULT;
		}

	}

	public static ImageSearchResult searchByWord(String word) {
		int page = 1;
		// 关键字查询过
		if (cache.containsKey(word)) {
			List<ImageSearchResult> list = cache.get(word);
			// 有查询结果缓存
			if (list.size() != 0) {
				return list.remove(0);
			} else {
				// 没有说明要新的一页了
				page = pageCache.get(word) + 1;
				pageCache.put(word, page);
			}
		}
		// 没有查询过关键字
		String json = HttpUtil.get(Constants.PIXIV_API, "type=search&page=" + page + "&word=" + word);
		JSONObject jsonObject = JSONObject.parseObject(json);
		JSONArray illusts = jsonObject.getJSONArray("illusts");
		if(illusts == null) {
			return ImageSearchResult.DEFAULT_RESULT;
		}
		List<ImageSearchResult> list = new ArrayList<>();
		illusts.stream()
				// 过滤
				.filter(o -> {
					for (Object tag : ((JSONObject) o).getJSONArray("tags")) {
						String name = ((JSONObject) tag).getString("name");
						// 如果tag是任何过滤字段的一个就不放行
						if (StringUtils.contains(filterTags, name)) {
							return false;
						}
					}
					// 不包含过滤tag
					return true;
					// 排序 输出收藏的DESC
				}).collect(Collectors.toList()).stream().sorted((a, b) -> {
					int bookmark1 = ((JSONObject) a).getInteger("total_bookmarks");
					int bookmark2 = ((JSONObject) b).getInteger("total_bookmarks");
					return bookmark2 - bookmark1;
					// 获取所有结果保存到list
				}).forEach(o -> {
					String url = ((JSONObject) o).getJSONObject("image_urls").getString("large");
					ImageSearchResult result = ImageSearchResult.builder().url(url).build();
					list.add(result);
				});
		if (list.size() == 0) {
			return ImageSearchResult.DEFAULT_RESULT;
		}
		// 缓存结果
		cache.put(word, list);
		// 页缓存
		pageCache.put(word, page);
		return cache.get(word).remove(0);
	}

}
