package com.pr.boostyou.controller;

import android.content.Context;

import com.pr.boostyou.MainApplication;
import com.pr.boostyou.api.model.PurchaseModel;
import com.pr.boostyou.callback.PurchaseSkusDetailsCallBack;
import com.pr.boostyou.payment.PaymentServiceAPI;

import java.util.List;

public class CoinPurchaseController {

	private Context context;
	private static CoinPurchaseController instance;


	public static CoinPurchaseController getInstance() {
		if (instance == null) {
			instance = new CoinPurchaseController();
		}
		return instance;
	}

	public static CoinPurchaseController getInstance(Context context) {
		if (instance == null) {
			instance = new CoinPurchaseController();
			instance.setContext(context);
		}
		return instance;
	}

	private CoinPurchaseController() {
		if (context == null) {
			setContext(MainApplication.getInstance().getApplicationContext());
		}

	}


	public void reuestPurchases(List<PurchaseModel> purchaseModels, PurchaseSkusDetailsCallBack purchaseSkusDetailsCallBack){
		PaymentServiceAPI.getPaymentService(context).getShopItemsPrices(purchaseModels,purchaseSkusDetailsCallBack);
	}



	private void setContext(Context context) {
		this.context = context;
	}


}
