package qqbot.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeSearchResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2781700348454593489L;
	private String filename;
	private String anime;
	private String title;
	private Short from;
	private Short to;
	private Short at;
	private String season;
	private Object episode;
	private Short similarity;
}
