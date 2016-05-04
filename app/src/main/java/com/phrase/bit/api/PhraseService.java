package com.phrase.bit.api;

import com.phrase.bit.api.models.PhraseModel;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Joel on 5/4/2016.
 */
public interface PhraseService {

     String MAIN_PHRASES="https://glacial-sands-39825.herokuapp.com";


    @GET("/")
    void GetPhraseIds(Callback<PhraseModel> phraseIds);
}
