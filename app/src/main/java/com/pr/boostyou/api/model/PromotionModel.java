package com.pr.boostyou.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromotionModel implements Serializable {

	@SerializedName("10")
	private int tenLikes ;
	@SerializedName("20")
	private int twentyLikes;
	@SerializedName("50")
	private int fiftyLikes;
	@SerializedName("100")
	private int houndredLikes;
	@SerializedName("150")
	private int houndFiftLikes;
	@SerializedName("300")
	private int threHoundredLikes;
	@SerializedName("1000")
	private int thousendLikes;


	public int getTenLikes() {
		return tenLikes;
	}

	public int getTwentyLikes() {
		return twentyLikes;
	}

	public int getFiftyLikes() {
		return fiftyLikes;
	}

	public int getHoundredLikes() {
		return houndredLikes;
	}

	public int getHoundFiftLikes() {
		return houndFiftLikes;
	}

	public int getThreHoundredLikes() {
		return threHoundredLikes;
	}

	public int getThousendLikes() {
		return thousendLikes;
	}

}
