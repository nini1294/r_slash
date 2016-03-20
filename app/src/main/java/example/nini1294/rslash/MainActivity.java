package example.nini1294.rslash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SET_NAME = "SubList";
    private static CollectionPagerAdapter mCollectionPagerAdapter;
    @Bind(R.id.pager) ViewPager mViewPager;
    Set<String> titles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        titles = settings.getStringSet(SET_NAME, new TreeSet<String>());
        for(String title : titles) {
            Log.i("Data", title);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
        }

//        ActionBar actionBar = getActionBar();

        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mCollectionPagerAdapter.notifyDataSetChanged();

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
            NewSubDialog dialog = new NewSubDialog();
            dialog.show(getSupportFragmentManager(), "NewSubDialog");
            return true;
        } else if (id == R.id.action_remove) {
            RemoveSubDialog dialog = new RemoveSubDialog();
            dialog.show(getSupportFragmentManager(), "RemoveSubDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class NewSubDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstaceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Add a new subreddit");
            final EditText input = new EditText(getActivity());
            input.setSingleLine(true);
            input.requestFocus();
            builder.setView(input);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    Set<String> temp = settings.getStringSet(SET_NAME, new TreeSet<String>());
                    if (!input.getText().toString().equals("")) {
                        temp.add(input.getText().toString().toLowerCase());
                    }
                    editor.putStringSet(SET_NAME, temp);
                    editor.apply();
                    mCollectionPagerAdapter.notifyDataSetChanged();
//                    mCollectionPagerAdapter.startUpdate();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Nothing
                }
            });
            return builder.create();
        }
    }

    public static class RemoveSubDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Remove a subreddit");
            final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            Set<String> subs = settings.getStringSet(SET_NAME, new TreeSet<String>());
            Iterator<String> iterator = subs.iterator();
            final List<String> list = new ArrayList<>();
            for (int i = 0; i < subs.size(); i++) {
                list.add(iterator.next());
            }
            for (CharSequence chars : list) {
                Log.i("Lgo", chars.toString());
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, list);
            final ListView lv = new ListView(getActivity());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = adapter.getItem(position);
                    SharedPreferences.Editor editor = settings.edit();
                    Set<String> temp = settings.getStringSet(SET_NAME, new TreeSet<String>());
                    temp.remove(item);
                    editor.putStringSet(SET_NAME, temp);
                    editor.apply();
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    mCollectionPagerAdapter.notifyDataSetChanged();
                }
            });
            builder.setView(lv);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Nothing
                }
            });

            return builder.create();
        }
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
            args.putString(SubredditFragment.ARG_NAME, titles.toArray()[i].toString());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Object[] al = titles.toArray();
            if (titles.size() >= 1) {
                return al[position].toString();
            } else {
                return "NOPE";
            }
        }
    }
}