package com.phrase.bit.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.phrase.bit.PhraseBitApp;
import com.phrase.bit.R;
import com.phrase.bit.api.PhraseService;
import com.phrase.bit.api.models.PhraseListModel;
import com.phrase.bit.api.models.PhraseRootModel;
import com.phrase.bit.ui.adapters.PhraseAdapter;
import com.phrase.bit.ui.viewmodels.PhraseViewModel;
import com.phrase.bit.util.Utils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private PhraseAdapter phraseAdapter;
    private PhraseService phraseService;
    private List<PhraseViewModel> phraseViewModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        phraseViewModelList = new ArrayList<>();

        phraseAdapter = new PhraseAdapter(this, phraseViewModelList);

        list.setAdapter(phraseAdapter);

        phraseService = ((PhraseBitApp) getApplication()).getPhraseService();


        if (Utils.isOnline(this))
            fetchPhrases();
        else
            Toast.makeText(this, "No internet detected", Toast.LENGTH_SHORT).show();
    }

    /*
    * Makes the call to the Phrases API and returns a strongly typed list of the ids of the phrases.
    * */
    public void fetchPhrases() {
        phraseService.GetPhraseIds(new Callback<PhraseListModel>() {
            @Override
            public void success(final PhraseListModel phraseListModel, Response response) {

                //Checks to ensure the list isn't empty.
                if (!phraseListModel.getItems().isEmpty()) {

                    /*
                    * Fetches each individual phrase using the download ids from the list.
                    * */
                    for (final String id : phraseListModel.getItems()) {
                        phraseService.GetPhrase(id, new Callback<PhraseRootModel>() {
                            @Override
                            public void success(PhraseRootModel phraseRootModel, Response response) {

                                PhraseViewModel model = new PhraseViewModel();
                                model.setKey(phraseRootModel.getModel().getKey());
                                model.setPhrase(phraseRootModel.getModel().getValue());

                                phraseViewModelList.add(model);

                                phraseAdapter.notifyDataSetChanged();
                                list.smoothScrollToPosition(phraseViewModelList.size());

                                if(phraseListModel.getItems().indexOf(id)==phraseListModel.getItems().size()-1)
                                {
                                    progressWheel.stopSpinning();

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressWheel.stopSpinning();

                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                } else {
                    progressWheel.stopSpinning();

                    Toast.makeText(MainActivity.this, "No items are in the list", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
}
