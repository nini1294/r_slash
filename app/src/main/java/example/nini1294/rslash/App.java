package example.nini1294.rslash;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nishant on 15/03/16.
 */
public class App extends android.app.Application {
    private static final String BASE_URL = "http://www.reddit.com/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static RedditInterface api = retrofit.create(RedditInterface.class);
}
