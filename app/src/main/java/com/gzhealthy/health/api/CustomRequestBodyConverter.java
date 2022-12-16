package com.gzhealthy.health.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.gzhealthy.health.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;


public class CustomRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private final String TAG = CustomRequestBodyConverter.class.getSimpleName();
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }


    @Override
    public RequestBody convert(T value) throws IOException {

        Logger.e(TAG, "CustomRequestBodyConverter: i come here 00");
        String request = gson.toJson(value);
        Logger.e(TAG, "CustomRequestBodyConverter: " + value.toString());
        Logger.e(TAG, "CustomRequestBodyConverter: i come here 11");
//        Logger.e(TAG, "CustomRequestBodyConverter  enCypto : " + "data=" + CyptoUtils.encode(Constants.CYPTO_KEY, request));
        Logger.e(TAG, "CustomRequestBodyConverter  enCypto : " + "data=" + request);
        Logger.e(TAG, "CustomRequestBodyConverter: i come here 22");
//        return RequestBody.create(MEDIA_TYPE, "data=" + CyptoUtils.encode(Constants.CYPTO_KEY, request));
        return RequestBody.create(MEDIA_TYPE, "data=" + request);

/*
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        Logger.e(TAG, "CustomRequestBodyConverter: i come here 00" );
        Logger.e(TAG, "CustomRequestBodyConverter: " + buffer.readString(Charset.forName("utf-8")));
        Logger.e(TAG, "CustomRequestBodyConverter: i come here 11" );
        Logger.e(TAG, "CustomRequestBodyConverter  enCypto : " + "data=" + CyptoUtils.encode(Constants.CYPTO_KEY, buffer.readByteArray()));
        Logger.e(TAG, "CustomRequestBodyConverter: i come here 22" );
        return RequestBody.create(MEDIA_TYPE, "data=" + CyptoUtils.encode(Constants.CYPTO_KEY, buffer.readByteArray()));*/
    }
}
