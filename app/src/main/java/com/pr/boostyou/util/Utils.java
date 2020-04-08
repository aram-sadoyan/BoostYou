package com.pr.boostyou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.pr.boostyou.MainApplication;
import com.pr.boostyou.api.model.OfferCardItem;
import com.pr.boostyou.api.model.PromotionModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Utils {

	public static void saveMap(String name, Map<String, String> inputMap) {
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("mapPostValues", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			JSONObject jsonObject = new JSONObject(inputMap);
			String jsonString = jsonObject.toString();
			SharedPreferences.Editor editor = pSharedPref.edit();
			editor.remove(name).apply();
			editor.putString(name, jsonString);
			editor.commit();
		}
	}

	public static Map<String, String> loadMap(String name) {
		Map<String, String> outputMap = new HashMap<String, String>();
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("mapPostValues", Context.MODE_PRIVATE);
		try {
			if (pSharedPref != null) {
				String jsonString = pSharedPref.getString(name, (new JSONObject()).toString());
				JSONObject jsonObject = new JSONObject(jsonString);
				Iterator<String> keysItr = jsonObject.keys();
				while (keysItr.hasNext()) {
					String key = keysItr.next();
					String value = (String) jsonObject.get(key);
					outputMap.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputMap;
	}

	public static int convertDpToPixel(float dp) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		return (int) (dp * metrics.density);
	}


	public static void insertLoginCount() {
		SharedPreferences pSharedPref =MainApplication.getInstance(). getApplicationContext()
				.getSharedPreferences("loginValueCount", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			int c = pSharedPref.getInt("loginCount", 0);
			c++;
			pSharedPref.edit().putInt("loginCount", c).apply();
		}
	}


	public static int getLoginCount() {
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("loginValueCount", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			return pSharedPref.getInt("loginCount", 0);
		}
		return -1;
	}





	public static boolean isConnected(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo nInfo = cm.getActiveNetworkInfo();
				return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
			}
		}
		return false;
	}


	public static List<OfferCardItem> getPromotionCardItems(PromotionModel promotionModel){
		List<OfferCardItem> offerCardItems = new ArrayList<>();
		if (promotionModel == null){
			return offerCardItems;
		}

		offerCardItems.add(new OfferCardItem(
				"10",
				promotionModel.getTenLikes()
		));
		offerCardItems.add(new OfferCardItem(
				"20",
				promotionModel.getTwentyLikes()
		));
		offerCardItems.add(new OfferCardItem(
				"50",
				promotionModel.getFiftyLikes()
		));
		offerCardItems.add(new OfferCardItem(
				"100",
				promotionModel.getHoundredLikes()
		));
		offerCardItems.add(new OfferCardItem(
				"150",
				promotionModel.getHoundFiftLikes()
		));
		offerCardItems.add(new OfferCardItem(
				"300",
				promotionModel.getThreHoundredLikes()
		));
		offerCardItems.add(new OfferCardItem(
				"1000",
				promotionModel.getThousendLikes()
		));

		return offerCardItems;
	}

	public static String getUserName(){
		String userName = "";
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("instaUserName", Context.MODE_PRIVATE);
		userName = pSharedPref.getString("userName", "");
		return userName;
	}

	public static void saveUserName(String userName) {
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("instaUserName", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			SharedPreferences.Editor editor = pSharedPref.edit();
			editor.remove("userName").apply();
			editor.putString("userName", userName);
			editor.commit();
		}
	}


	public static void saveCoinsCount(int coins) {
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("coinsCount", Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			SharedPreferences.Editor editor = pSharedPref.edit();
			editor.remove("coin").apply();
			editor.putInt("coin", coins);
			editor.commit();
		}
	}

	public static int getCoinsCount(){
		int coinCount = -1;
		SharedPreferences pSharedPref = MainApplication.getInstance().getApplicationContext()
				.getSharedPreferences("coinsCount", Context.MODE_PRIVATE);
		coinCount = pSharedPref.getInt("coin", -2);

		return coinCount;
	}

}
