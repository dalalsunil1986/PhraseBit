package com.phrase.bit.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.phrase.bit.PhraseBitApp;
import com.phrase.bit.R;
import com.phrase.bit.api.PhraseService;
import com.phrase.bit.api.models.PhraseListModel;
import com.phrase.bit.api.models.PhraseRootModel;
import com.phrase.bit.ui.adapters.PhraseAdapter;
import com.phrase.bit.ui.viewmodels.PhraseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    ListView list;

    private PhraseAdapter phraseAdapter;
    private PhraseService phraseService;
    private List<PhraseViewModel> phraseViewModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView)findViewById(R.id.list);

        phraseViewModelList=new ArrayList<>();

        phraseAdapter=new PhraseAdapter(this,phraseViewModelList);

        list.setAdapter(phraseAdapter);

        phraseService=((PhraseBitApp) getApplication()).getPhraseService();


        fetchPhrases();
    }

    public void fetchPhrases()
    {
        phraseService.GetPhraseIds(new Callback<PhraseListModel>() {
            @Override
            public void success(PhraseListModel phraseListModel, Response response) {

                final ArrayList<PhraseViewModel> phraseItems = new ArrayList<>();
                for(String id:phraseListModel.getItems())
                {
                    phraseService.GetPhrase(id, new Callback<PhraseRootModel>() {
                        @Override
                        public void success(PhraseRootModel phraseRootModel, Response response) {

                            PhraseViewModel model = new PhraseViewModel();
                            model.setKey(phraseRootModel.getModel().getKey());
                            model.setPhrase(phraseRootModel.getModel().getValue());

                            phraseItems.add(model);


                            phraseViewModelList.addAll(phraseItems);
                            phraseAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
