package com.pr.boostyou.api.model;

public class OfferCardItem {


	String offerCount = "";
	int costCoinCount = -1;


	public String getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(String offerCount) {
		this.offerCount = offerCount;
	}

	public int getCostCoinCount() {
		return costCoinCount;
	}

	public void setCostCoinCount(int costCoinCount) {
		this.costCoinCount = costCoinCount;
	}

	public OfferCardItem(String offerCount, int costCoinCount) {
		this.offerCount = offerCount;
		this.costCoinCount = costCoinCount;
	}
}
