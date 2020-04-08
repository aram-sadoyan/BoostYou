package com.pr.boostyou.callback;

public interface PaymentPurchaseCallBack {

	void onPaymentCallBack(String purchaseToken, String sku, String orderId);
}
