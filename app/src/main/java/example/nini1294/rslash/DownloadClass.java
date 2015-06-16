/*
package example.nini1294.rslash;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Nishant on 19/12/14.
 *//*

public class DownloadClass extends AsyncTask<android.support.v4.app.Fragment, Integer, JSONObject>{

    public static final String ARG_NAME = "subreddit_name";
    public android.support.v4.app.Fragment f = null;
    @Override
    protected JSONObject doInBackground(android.support.v4.app.Fragment... fragments) {

        HttpURLConnection urlConnection = null;
        JSONObject js = null;
        List<String> list = new ArrayList<>();
        try {
            f = fragments[0];
            String subredditName = f.getArguments().getString(ARG_NAME);
            URL url = new URL("http://www.reddit.com/r/" + subredditName + "/hot.json");
            urlConnection = (HttpURLConnection) url.openConnection();

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

        final List<Post> list = new ArrayList<>();
        ListView titles = (ListView) f.getActivity().findViewById(R.id.titles_listView);
        if (js != null) {
            try {
                js = js.getJSONObject("data");
                JSONArray jsArray = js.getJSONArray("children");
                for (int i = 0; i < jsArray.length(); i++) {
                    JSONObject ob = jsArray.getJSONObject(i).getJSONObject("data");
                    Log.i("Lgo", ob.getString("title"));
                    list.add(new Post(ob.getString("title"), ob.getString("author"), ob.optString("url"),
                            ob.getString("thumbnail"), ob.getString("score"), ob.getDouble("created_utc"), ob.getBoolean("is_self")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("JSON Problem", e.getMessage());
            }
            super.onPostExecute(js);
        } else {
            Log.i("Info", "List is null");
        }
        titles.setAdapter(new PostListAdapter(f.getActivity().getApplicationContext(), list));
        titles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.url_textView);
                tv.setTextColor(f.getResources().getColor(R.color.clicked_link_color));
                if (!list.get(position).getSelf()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(list.get(position).getURL().toString());
                    intent.setData(uri);
                    f.startActivity(intent);
                }
            }
        });
    }
}
*/
