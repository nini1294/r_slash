package example.nini1294.rslash;

import example.nini1294.rslash.POJOs.SubredditResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Nishant on 15/03/16.
 */
public interface RedditInterface {
    @GET("r/{subreddit}/hot.json")
    Call<SubredditResponse> getSubredditHot(@Path("subreddit") String subreddit);

    @GET("r/{subreddit}/{order}.json")
    Call<SubredditResponse> getSubreddit(@Path("subreddit") String subreddit,
                                         @Path("order") String order);
}
