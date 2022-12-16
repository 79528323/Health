package com.gzhealthy.health.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.gzhealthy.health.logger.Logger;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SPCache {

    private static final String TAG = SPCache.class.getSimpleName();
    private static SPCache _instance;
    private ConcurrentMap<String, SoftReference<Object>> cache;
    private String prefFileName = "com.aiten.education";
    private Context context;

    public static final String KEY_TOKEN ="token";
    public static final String KEY_BAGE_COUNT ="key_bage_count";
    public static final String KEY_BAGE_COUNT_BACKGROUND_NO_READ ="key_bage_count_background_no_read";
    public static final String KEY_JUMP_SOS ="key_jump_sos";
    public static final String KEY_JPUSH_REG_ID = "key_jpush_reg_id";
    public static final String KEY_JPUSH_SOS_NOTIFICATION_ID = "key_jpush_sos_notification_id";
    public static final String KEY_JPUSH_ECG_NOTIFICATION_ID = "key_jpush_ecg_notification_id";
    public static final String KEY_APP_VER = "key_app_ver";
    public static final String KEY_HEALTH_CARD_LIST = "key_health_card_list";
    public static final String KEY_HEALTH_CARD_OTHER_LIST = "key_health_card_other_list";
    public static final String KEY_IS_ACCEPT_PRIVACY = "key_is_accept_privacy";
    public static final String KEY_BLUETOOTH_MAC = "key_bluetooth_mac";

    private SPCache(Context context, String prefFileName) {
        this.context = context.getApplicationContext();
        cache = new ConcurrentHashMap<String, SoftReference<Object>>();
        initDatas(prefFileName);

    }

    private void initDatas(String prefFileName) {
        if (null != prefFileName && prefFileName.trim().length() > 0) {
            this.prefFileName = prefFileName;
        } else {
            Logger.d(TAG, "prefFileName is invalid , we will use default value ");
        }
        //SharedPreferences sp = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        //Map<String, ?> alreadyDatas = sp.getAll();
        //Set<String> keys = alreadyDatas.keySet();
        //for (String key : keys)
        //{
        //    cache.put(key, new SoftReference<Object>(alreadyDatas.get(key)));
        //}
    }

    public static SPCache init(Context context, String prefFileName) {
        if (_instance == null) {
            synchronized (SPCache.class) {
                if (_instance == null) {
                    _instance = new SPCache(context, prefFileName);
                }
            }
        }
        return _instance;
    }

    public static SPCache init(Context context) {
        return init(context, null);
    }

    private static SPCache getInstance() {
        if (_instance == null)
            throw new NullPointerException("you show invoke SpCache.init() before you use it ");

        return _instance;
    }


    //put
    public static SPCache putArray(String key, List arrayList){
        return getInstance().put(key,arrayList);
    }

    public static SPCache putInt(String key, int val) {
        return getInstance().put(key, val);
    }

    public static SPCache putLong(String key, long val) {
        return getInstance().put(key, val);
    }

    public static SPCache putString(String key, String val) {
        return getInstance().put(key, val);
    }

    public static SPCache putBoolean(String key, boolean val) {
        return getInstance().put(key, val);
    }

    public static SPCache putFloat(String key, float val) {
        return getInstance().put(key, val);
    }


    //get
    public static List<Integer> getList(String key){
        return (List<Integer>) getInstance().get(key,new ArrayList<Integer>());
    }

    public static int getInt(String key, int defaultVal) {
        return (int) (getInstance().get(key, defaultVal));
    }

    public static long getLong(String key, long defaultVal) {
        return (long) (getInstance().get(key, defaultVal));
    }

    public static String getString(String key, String defaultVal) {
        return (String) (getInstance().get(key, defaultVal));
    }

    public static boolean getBoolean(String key, boolean defaultVal) {
        return (boolean) (getInstance().get(key, defaultVal));
    }

    public static float getFloat(String key, float defaultVal) {
        return (float) (getInstance().get(key, defaultVal));
    }

    //contains
    public boolean contains(String key) {
        return cache.get(key).get() != null ? true : getSharedPreferences().contains(key);
    }

    //remove
    public static SPCache remove(String key) {
        return _instance._remove(key);
    }

    private SPCache _remove(String key) {
        cache.remove(key);
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
        return _instance;
    }

    //clear
    public static SPCache clear() {
        return _instance._clear();
    }

    private SPCache _clear() {
        cache.clear();
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
        return _instance;
    }

    private <T> SPCache put(String key, T t) {
        cache.put(key, new SoftReference<Object>(t));
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if (t instanceof String) {
            editor.putString(key, (String) t);
        } else if (t instanceof Integer) {
            editor.putInt(key, (Integer) t);
        } else if (t instanceof Boolean) {
            editor.putBoolean(key, (Boolean) t);
        } else if (t instanceof Float) {
            editor.putFloat(key, (Float) t);
        } else if (t instanceof Long) {
            editor.putLong(key, (Long) t);
        } else {
            Logger.d(TAG, "you may be put a invalid object :" + t);
            editor.putString(key, t.toString());
        }

        SharedPreferencesCompat.apply(editor);
        return _instance;
    }


    private Object readDisk(String key, Object defaultObject) {
        Logger.e("TAG", "readDisk");
        SharedPreferences sp = getSharedPreferences();

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        Logger.e(TAG, "you can not read object , which class is " + defaultObject.getClass().getSimpleName());
        return null;

    }

    private Object get(String key, Object defaultVal) {
        SoftReference reference = cache.get(key);
        Object val = null;
        if (null == reference || null == reference.get()) {
            val = readDisk(key, defaultVal);
            cache.put(key, new SoftReference<Object>(val));
        }
        val = cache.get(key).get();
        return val;
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(final SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    editor.commit();
                    return null;
                }
            };
        }
    }


    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
}
