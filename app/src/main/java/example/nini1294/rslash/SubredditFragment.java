package example.nini1294.rslash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SubredditFragment extends Fragment {

    public static final String ARG_POS = "pos";
    public static final String ARG_NAME = "subreddit_name";

    public View rootView;
    public ListView titles;
    public String subredditName;
    public List<Post> list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.subreddit_fragment, container, false);
        titles = (ListView) rootView.findViewById(R.id.titles_listView);
        Bundle args = getArguments();
        subredditName = args.getString(ARG_NAME);
        list = new ArrayList<>();
        JSONObject js = null;
        try {
            Toast.makeText(getActivity().getApplicationContext(), subredditName, Toast.LENGTH_SHORT).show();
            URL url = new URL("http://www.reddit.com/r/" + subredditName + "/hot.json");
//            js = new DownloadClass().execute(url).get();
            new DownloadClass().execute(url);
        } catch (MalformedURLException e) {
            Log.e("Error", e.getMessage());
        }
        if (js != null) {
            try {
                js = js.getJSONObject("data");
                JSONArray jsArray = js.getJSONArray("children");
                for (int i = 0; i < jsArray.length(); i++) {
                    JSONObject ob = jsArray.getJSONObject(i).getJSONObject("data");
                    list.add(new Post(ob.getString("title"), ob.getString("author"), ob.optString("url"),
                            ob.getString("thumbnail"), ob.getString("score"), ob.getDouble("created_utc"), ob.getBoolean("is_self")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("JSON Problem", e.getMessage());
            }
        }
        /*titles.setAdapter(new SampleAdapter(getActivity().getApplicationContext(), list));
        titles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.url_textView);
                tv.setTextColor(getResources().getColor(R.color.clicked_link_color));
                if (!list.get(position).getSelf()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(list.get(position).getURL().toString());
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });*/
        return rootView;
    }

    private class SampleAdapter extends ArrayAdapter<Post> {

        public SampleAdapter(Context context, List<Post> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Post post = getItem(position);
            PostViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new PostViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title_textView);
                viewHolder.userName = (TextView) convertView.findViewById(R.id.user_textView);
                viewHolder.upvotes = (TextView) convertView.findViewById(R.id.upvotes_textView);
                viewHolder.age = (TextView) convertView.findViewById(R.id.age_textView);
                viewHolder.url = (TextView) convertView.findViewById(R.id.url_textView);
//                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.image_ImageView);
                convertView.setTag(viewHolder);
                convertView.setBackgroundColor(this.getContext().getResources().getColor(R.color.post_unclicked_color));
            } else {
                viewHolder = (PostViewHolder) convertView.getTag();
            }

            viewHolder.title.setText(post.getTitle());
            viewHolder.userName.setText(post.getUserName());
            viewHolder.upvotes.setText(post.getUpvotes());
            viewHolder.age.setText(post.getAge());
            viewHolder.url.setText(post.getURLString());
            /*if ((post.getImageURL().equals("self")) || (post.getImageURL().equals(""))) {
                viewHolder.thumbnail.setImageResource(R.drawable.default_thumbnail);
            } else {
                try {
                    viewHolder.thumbnail.setImageBitmap(new ImageDownloadClass().execute(post.getImageURL()).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }*/

            return convertView;
        }

    }

    static class PostViewHolder {
        TextView title;
        TextView userName;
        TextView upvotes;
        TextView age;
        TextView url;
//        ImageView thumbnail;

    }

    private class DownloadClass extends AsyncTask<URL, Integer, JSONObject> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected JSONObject doInBackground(URL... urls) {

            HttpURLConnection urlConnection = null;
            JSONObject js = null;
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();

                InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(in);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                line = builder.toString();
                js = new JSONObject(line);


            } catch (IOException | JSONException e) {
                Log.e("Error", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return js;
        }

        @Override
        protected void onPostExecute(JSONObject js) {
            super.onPostExecute(js);
            if (js != null) {
                Log.i("Timing", "Set text somewhere");
                if (js != null) {
                    try {
                        js = js.getJSONObject("data");
                        JSONArray jsArray = js.getJSONArray("children");
                        for (int i = 0; i < jsArray.length(); i++) {
                            JSONObject ob = jsArray.getJSONObject(i).getJSONObject("data");
                            list.add(new Post(ob.getString("title"), ob.getString("author"), ob.optString("url"),
                                    ob.getString("thumbnail"), ob.getString("score"), ob.getDouble("created_utc"), ob.getBoolean("is_self")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("JSON Problem", e.getMessage());
                    }
                }
            } else {
                Log.i("Info", "List is null");
            }
            titles.setAdapter(new SampleAdapter(getActivity().getApplicationContext(), list));
            ((ArrayAdapter) titles.getAdapter()).notifyDataSetChanged();
            titles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(R.id.url_textView);
                    tv.setTextColor(getResources().getColor(R.color.clicked_link_color));
                    if (!list.get(position).getSelf()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(list.get(position).getURL().toString());
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            });
        }
    }

}
