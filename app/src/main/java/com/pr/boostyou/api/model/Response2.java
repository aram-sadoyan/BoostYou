package com.pr.boostyou.api.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class Response2 extends Status {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	private static Gson defaultGson = null;

	@SerializedName("message")
	public String message = "";
	@SerializedName("reason")
	public String reason = "";

	@Override
	public String toString() {
		return getDefaultGson().toJson(this);
	}

	private Gson getDefaultGson() {
		if (defaultGson == null)
			defaultGson = new GsonBuilder()
					.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
					.setDateFormat(DEFAULT_DATE_FORMAT)
					.serializeSpecialFloatingPointValues()
					.disableHtmlEscaping()
					.registerTypeAdapter(Double.class, DOUBLE_JSON_SERIALIZER)
					.create();
		return defaultGson;
	}

	private static final JsonSerializer<Double> DOUBLE_JSON_SERIALIZER = new JsonSerializer<Double>() {
		@Override
		public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
			if (src == (double) src.longValue())
				return new JsonPrimitive(src.longValue());
			return new JsonPrimitive(src);
		}
	};


}