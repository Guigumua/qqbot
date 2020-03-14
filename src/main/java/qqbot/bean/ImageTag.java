package qqbot.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@IdClass(ImageTag.ImageTagPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ImageTag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9093228928026925585L;
	@Id
	@Column(length = 64)
	private String file;
	@Id
	@Column(length = 64)
	private String tag;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ImageTagPK implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5677274264908998218L;
		private String file;
		private String tag;
	}

}
