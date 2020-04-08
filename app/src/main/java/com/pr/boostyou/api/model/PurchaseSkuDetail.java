package com.pr.boostyou.api.model;

public class PurchaseSkuDetail {

	private String sku = "";
	private String currencyCode = "";
	private String price = "";
	private long priceAmountMicros = 0;


	public PurchaseSkuDetail(String sku,
							 String currencyCode,
							 String price,
							 long priceAmountMicros) {
		this.sku = sku;
		this.currencyCode = currencyCode;
		this.price = price;
		this.priceAmountMicros = priceAmountMicros;
	}


	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public long getPriceAmountMicros() {
		return priceAmountMicros;
	}

	public void setPriceAmountMicros(long priceAmountMicros) {
		this.priceAmountMicros = priceAmountMicros;
	}
}
