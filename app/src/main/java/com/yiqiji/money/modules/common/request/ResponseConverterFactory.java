package com.yiqiji.money.modules.common.request;

import java.lang.reflect.Type;

import retrofit.Converter;
import retrofit.Converter.Factory;

import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

public class ResponseConverterFactory extends Factory {
	private final Gson gson;

	private ResponseConverterFactory(Gson gson) {
		if (gson == null)
			throw new NullPointerException("gson == null");
		this.gson = gson;
	}

	public static ResponseConverterFactory create() {
		return create(new Gson());
	}

	public static ResponseConverterFactory create(Gson gson) {
		return new ResponseConverterFactory(gson);
	}

	@Override
	public Converter<ResponseBody, ?> fromResponseBody(Type type, java.lang.annotation.Annotation[] annotations) {
		return new MyConverterFactory<>(gson, type);
	}

	@Override
	public Converter<?, RequestBody> toRequestBody(Type type, java.lang.annotation.Annotation[] annotations) {
		return new MyConverterFactory<>(gson, type);
	}

}
