package com.phrase.bit.ui.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
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

    /**
     * Uses the ButterKnife plugin by Jake Wharton to bind views so that calls
     * to findViewById aren't necessary to clutter code.
     */
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.empty)
    TextView empty;

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

        list.setEmptyView(empty);

        list.setAdapter(phraseAdapter);

        phraseService = ((PhraseBitApp) getApplication()).getPhraseService();


        /**
         * Setting up swipe to refresh functionality in case the request fails and it needs
         * to be restarted.
         */
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                phraseViewModelList.clear();
                phraseAdapter.notifyDataSetChanged();
                fetchPhrases();
            }
        });

        /**
         * Fixes a bug with pull to refresh where it starts refreshing once the user begins to
         * scroll up.
         */
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition =
                        (list == null || list.getChildCount() == 0) ?
                                0 : list.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        fetchPhrases();
    }

    /*
    * Makes the call to the Phrases API and returns a strongly typed list of the ids of the phrases.
    * */
    public void fetchPhrases() {

        /**
         * Check to ensure internet connectivity is available before making call to API.
         * */
        if (!Utils.isOnline(this)) {
            Toast.makeText(this, "No internet detected", Toast.LENGTH_SHORT).show();

            if (progressWheel.isSpinning())
                progressWheel.stopSpinning();

            if (swipeContainer.isRefreshing())
                swipeContainer.setRefreshing(false);

            return;
        }

        progressWheel.spin();
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

                                /*
                                * Stops the spinner and refresh when it reaches at the last item of the list.
                                * */
                                if (phraseListModel.getItems().indexOf(id) == phraseListModel.getItems().size() - 1) {

                                    if (progressWheel.isSpinning())
                                        progressWheel.stopSpinning();

                                    if (swipeContainer.isRefreshing())
                                        swipeContainer.setRefreshing(false);

                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                if (progressWheel.isSpinning())
                                    progressWheel.stopSpinning();

                                if (swipeContainer.isRefreshing())
                                    swipeContainer.setRefreshing(false);

                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                } else {
                    if (progressWheel.isSpinning())
                        progressWheel.stopSpinning();

                    if (swipeContainer.isRefreshing())
                        swipeContainer.setRefreshing(false);

                    Toast.makeText(MainActivity.this, "Uh Oh! No items are in the list.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if (progressWheel.isSpinning())
                    progressWheel.stopSpinning();

                if (swipeContainer.isRefreshing())
                    swipeContainer.setRefreshing(false);

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
}
