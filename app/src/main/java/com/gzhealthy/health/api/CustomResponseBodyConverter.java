package com.gzhealthy.health.api;

import com.google.gson.TypeAdapter;
import com.gzhealthy.health.logger.Logger;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class CustomResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final String TAG = CustomResponseBodyConverter.class.getSimpleName();

    private final TypeAdapter<T> adapter;

    CustomResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        String ressponse = value.string();
//        String decyptoRes = CyptoUtils.decode(Constants.CYPTO_KEY, ressponse);
        String decyptoRes = ressponse;
        Logger.json(TAG, decyptoRes);
        try {
            return adapter.fromJson(decyptoRes);
        } finally {
            value.close();
        }
    }
}
