package example.nini1294.rslash;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.pacoworks.rxpaper.*;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.nini1294.rslash.Dialogs.AddSubDialog;
import example.nini1294.rslash.Dialogs.RemoveSubDialog;
import example.nini1294.rslash.POJOs.SubredditResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends AppCompatActivity implements RemoveSubDialog.SubRemovedListener, AddSubDialog.SubAddedListener {

    private static final String PREFS_NAME = "MyPrefsFile";
    public static final String LIST_NAME = "SubList";
    private static CollectionPagerAdapter mCollectionPagerAdapter;
    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.main_toolbar) Toolbar mToolbar;
    ArrayList<String> titles;
    ArrayList<String> defaultTitles = new ArrayList<>();
    CompositeSubscription paperSubs;
    CompositeSubscription bindingSubs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        RxPaperBook.init(this);
        Logger.init();
        paperSubs = new CompositeSubscription();
        bindingSubs = new CompositeSubscription();
        setSupportActionBar(mToolbar);
        readTitles();
        mViewPager = (ViewPager) findViewById(R.id.pager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        }

//        ActionBar actionBar = getActionBar();
    }

    @Override
    protected void onDestroy() {
        paperSubs.unsubscribe();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            AddSubDialog dialog = new AddSubDialog();
            dialog.show(getSupportFragmentManager(), "NewSubDialog");
            return true;
        } else if (id == R.id.action_remove) {
            RemoveSubDialog dialog = new RemoveSubDialog();
            Bundle args = new Bundle();
            args.putStringArrayList(LIST_NAME, titles);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "RemoveSubDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Add Observer
    @Override
    public void addSub(String sub) {
        // Verify that the subreddit is valid
        App.api.getSubredditHot(sub).enqueue(new Callback<SubredditResponse>() {
            @Override
            public void onResponse(Call<SubredditResponse> call, Response<SubredditResponse> response) {
                if (response.isSuccessful() && (response.body().getData().getPosts().size() > 0)) {
                    titles.add(sub);
                    writeTitles();
                    mCollectionPagerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "That is an invalid subreddit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubredditResponse> call, Throwable t) {
            }
        });

    }

    // Remove Observer
    @Override
    public void removeSub(String sub) {
        titles.remove(sub);
        writeTitles();
        mCollectionPagerAdapter.notifyDataSetChanged();
    }

    private void readTitles() {
        paperSubs.add(RxPaperBook.with().read(LIST_NAME, defaultTitles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((list) -> {
                    titles = list;
                    initializeViewPager();
                }));
    }

    private void writeTitles() {
        paperSubs.add(RxPaperBook.with().write(LIST_NAME, titles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    private void initializeViewPager() {
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mCollectionPagerAdapter.notifyDataSetChanged();
    }

    class CollectionPagerAdapter extends FragmentStatePagerAdapter {
        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new SubredditFragment();
            Bundle args = new Bundle();
            args.putInt(SubredditFragment.ARG_POS, i + 1);
            args.putString(SubredditFragment.ARG_NAME, titles.get(i));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            SubredditFragment f = (SubredditFragment) object;
            String subName = f.getSubredditName();
            int position = titles.indexOf(subName);
            if (position >= 0) {
                return position;
            } else {
                return POSITION_NONE;
            }
//            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (titles.size() >= 1) {
                return titles.get(position);
            } else {
                return "NOPE";
            }
        }
    }
}