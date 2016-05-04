package com.phrase.bit.api;

import com.phrase.bit.api.models.PhraseListModel;
import com.phrase.bit.api.models.PhraseRootModel;

import retrofit.Callback;
import retrofit.http.EncodedPath;
import retrofit.http.GET;

/**
 * Created by Joel on 5/4/2016.
 */
public interface PhraseService {

    String MAIN_URL = "https://glacial-sands-39825.herokuapp.com";


    @GET("/")
    void GetPhraseIds(Callback<PhraseListModel> phraseIds);

    @GET("/downloads/{query}")
    void GetPhrase(@EncodedPath("query") String itemId, Callback<PhraseRootModel> phrase);
}
