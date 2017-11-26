package com.chatapp.Service;


import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * @author Annyce Davis
 */
public class Injector
{
    private static final String CACHE_CONTROL = "Cache-Control";
    private static String BASE_URL = "http://shippment.mabou7agar.com/Api/" ;

    public static Retrofit provideRetrofit (String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .client( provideOkHttpClient() )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }




    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                        Log.e("http", message );
                    }
                } );
        httpLoggingInterceptor.setLevel(  BODY  );
        return httpLoggingInterceptor;
    }



    private static OkHttpClient provideOkHttpClient ()
    {
        return new OkHttpClient.Builder()
                .addInterceptor( provideHttpLoggingInterceptor() )
                .connectTimeout(30,TimeUnit.SECONDS)
                .build();
    }




   public  static FcmApi provideFcmApi(){

       return provideRetrofit("https://fcm.googleapis.com").create(FcmApi.class) ;
   }



}
