package com.gzhealthy.health.logger;

import android.util.Log;

import com.gzhealthy.health.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonLog extends BaseLog {

    public static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        message = headString + LINE_SEPARATOR + message;
        //  超过4000 换行
        int subNum = message.length() / 4000;
        if (subNum > 0) {
            int index = 0;
            for (int i = 0; i < subNum; i++) {
                int lastIndex = index + 4000;
                String sub = message.substring(index, lastIndex);
                if (BuildConfig.DEBUG)
                    Log.e(tag, sub);
                index = lastIndex;
            }
            if (BuildConfig.DEBUG)
                Log.e(tag, message.substring(index, message.length()));
        } else {
            if (BuildConfig.DEBUG)
                Log.e(tag, message);
        }

    }

}
