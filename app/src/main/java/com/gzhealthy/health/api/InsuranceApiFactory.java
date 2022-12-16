package com.gzhealthy.health.api;


public class InsuranceApiFactory {

    private static final Object msync = new Object();
    private static ApiInterface mHomeApi = null;

    public static ApiInterface getmHomeApi() {
        synchronized (msync) {
            if (mHomeApi == null)
                mHomeApi = ApiService.init().create(ApiInterface.class);
        }
        return mHomeApi;
    }

}
