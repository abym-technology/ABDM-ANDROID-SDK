package com.abym.abha.Network;

import android.content.Context;

import com.abym.abha.Util.PreferenceUtil;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static Context context;

    public static Retrofit getApiClient(Context mContext, String BASE_URL) {
        context = mContext;
        Retrofit retrofit = null;
        if (retrofit == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            HashMap<String, String> headers = new HashMap<>();

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(new HeaderIntercepter());
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(new NetworkConnectionInterceptor(mContext));
            clientBuilder.addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }
        return retrofit;
    }

    public static class HeaderIntercepter implements Interceptor {

        public Response intercept(Interceptor.Chain chain) throws IOException {
            return chain.proceed(chain.request().newBuilder().addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json").
                  //  addHeader("token", "60f291aa46ea447060f291aa46ea447019d83ba30be508e419d83ba30be508e4").build());
                    addHeader("token", PreferenceUtil.getStringPrefs(context,PreferenceUtil.CLIENT_TOKEN,"")).build());
        }
    }
}
