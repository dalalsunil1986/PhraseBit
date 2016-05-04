package com.phrase.bit;

import android.app.Application;

import com.phrase.bit.api.PhraseService;

import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by Joel on 5/4/2016.
 */
public class PhraseBitApp extends Application {
    private PhraseService phraseService;
    @Override
    public void onCreate() {
        super.onCreate();

        RestAdapter restAdapter;

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(PhraseService.MAIN_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setConverter(new SimpleXMLConverter())
                    .build();


        phraseService = restAdapter.create(PhraseService.class);
    }


    public PhraseService getPhraseService() {
        return phraseService;
    }

    public void setPhraseService(PhraseService phraseService) {
        this.phraseService = phraseService;
    }


}
