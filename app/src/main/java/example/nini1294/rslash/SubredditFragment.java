package example.nini1294.rslash;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.nini1294.rslash.POJOs.Post;
import example.nini1294.rslash.POJOs.PostWrapper;
import example.nini1294.rslash.POJOs.SubredditResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class SubredditFragment extends Fragment {

    public static final String ARG_POS = "pos";
    public static final String ARG_NAME = "subreddit_name";

    private String subredditName;
    private List<Post> list;
    private PostRecyclerAdapter adapter;
    @BindView(R.id.titles_swipeRefresh) public SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.titles_recycerView) public RecyclerView titles;
    @BindView(R.id.sub_progressBar) public ContentLoadingProgressBar progressBar;
    CompositeSubscription cs = new CompositeSubscription();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.subreddit_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Logger.init();
        subredditName = args.getString(ARG_NAME);
        callAPI();
        list = new ArrayList<>(30);
//        list.add(new Post("EMPTY", "EMPTY", "http://google.com", "EMPTY", "EMPTY", 2, false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new PostRecyclerAdapter(list, getActivity());
        titles.setAdapter(adapter);
        titles.setLayoutManager(layoutManager);
        swipeRefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2);
        cs.add(RxSwipeRefreshLayout.refreshes(swipeRefresh)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(aVoid -> {
                        progressBar.setVisibility(View.VISIBLE);
                        titles.setVisibility(View.GONE);
                        callAPI();
                    }));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        cs.unsubscribe();
        swipeRefresh.setRefreshing(false);
        super.onDestroyView();
    }

    private void callAPI() {
        Call<SubredditResponse> call = App.api.getSubredditHot(subredditName);
        call.enqueue(new Callback<SubredditResponse>() {
            @Override
            public void onResponse(Call<SubredditResponse> call, Response<SubredditResponse> response) {
                if (response.code() == 200) {
                    swipeRefresh.setRefreshing(false);
                    titles.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    refreshTitles(response.body());
                }
            }

            @Override
            public void onFailure(Call<SubredditResponse> call, Throwable t) {
                Logger.i("Failure: " + subredditName);
            }
        });
    }

    private void refreshTitles(SubredditResponse r) {
        List<PostWrapper> l = r.getData().getPosts();
        for (PostWrapper pw : l) {
            list.add(pw.getPost());
        }
        adapter.notifyDataSetChanged();
    }

    public String getSubredditName() {
        return subredditName;
    }
}
