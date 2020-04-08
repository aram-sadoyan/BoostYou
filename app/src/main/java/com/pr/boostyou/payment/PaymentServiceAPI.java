package com.pr.boostyou.payment;

import android.app.Activity;
import android.content.Context;


import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.callback.PaymantIsAvailable;
import com.pr.boostyou.callback.PaymentPurchaseCallBack;
import com.pr.boostyou.callback.PurchaseSkusDetailsCallBack;

import java.util.List;

public abstract class PaymentServiceAPI {

	public static PaymentServiceAPI getPaymentService(Context context) {
		return GoogleInAppBillingPaymentService.getInstance(context);
	}



	public abstract void onPaimantValid(PaymantIsAvailable paymantIsAvailable);

	public abstract void requestPurchase(Activity activity,final String itemType, PaymentPurchaseCallBack callBack);



	public abstract void getShopItemsPrices(List<PurchaseModel> purchaseModels, PurchaseSkusDetailsCallBack callBack);

}
