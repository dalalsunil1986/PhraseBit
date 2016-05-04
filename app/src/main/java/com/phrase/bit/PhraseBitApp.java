package com.phrase.bit;

import android.app.Application;

import com.phrase.bit.api.PhraseService;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by Joel on 5/4/2016.
 */
public class PhraseBitApp extends Application {
    private PhraseService phraseService;
    private RestAdapter restAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * The RestAdapter generates an implementation of the PhraseService interface.
     * It uses the Simple XML Converter so that that the XML being returned can be
     * converted to strong typed models as defined by the methods of the PhraseService.
     */

    public PhraseService generatePhraseService() {
        final OkHttpClient okHttpClient = new OkHttpClient();

        /*
        * The http client was timing out due to so many consecutive calls to fetch phrases
        * so I increased the timeout.
        * */
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(PhraseService.MAIN_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new SimpleXMLConverter())
                .build();


        return restAdapter.create(PhraseService.class);

    }


    public PhraseService getPhraseService() {

        if (phraseService == null) {
            phraseService = generatePhraseService();
        }

        return phraseService;
    }

    public void setPhraseService(PhraseService phraseService) {
        this.phraseService = phraseService;
    }


}
