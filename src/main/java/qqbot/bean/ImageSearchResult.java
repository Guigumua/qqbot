package qqbot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageSearchResult {
	public static ImageSearchResultBuilder builder() {

		return new ImageSearchResultBuilder();
	}

	public static class ImageSearchResultBuilder {

		ImageSearchResult result = new ImageSearchResult();

		public ImageSearchResultBuilder url(String url) {
			result.setUrl(url);
			return this;
		}

		public ImageSearchResultBuilder title(String title) {
			result.setTitle(title);
			return this;
		}

		public ImageSearchResultBuilder pid(String pid) {
			result.setPid(pid);
			return this;
		}

		public ImageSearchResultBuilder member(String member) {
			result.setMember(member);
			return this;
		}

		public ImageSearchResult build() {
			return this.result;
		}
	}

	private String url;
	private String title;
	private String pid;
	private String member;

	public static ImageSearchResult DEFAULT_RESULT = new ImageSearchResult();

	@Override
	public String toString() {
		if (this.equals(ImageSearchResult.DEFAULT_RESULT))
			return "没有找到你想要的结果";
		return this.getUrl() + "\n标题：" + this.getTitle() + "\npid：" + this.getPid() + "\nmember：" + this.getMember();
	}
}
