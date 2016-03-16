package example.nini1294.rslash.POJOs;

import java.util.List;

/**
 * Created by Nishant on 15/03/16.
 */
public class SubredditData {
    private String modhash;
    private List<PostWrapper> children;
    private String after;
    private String before;

    public List<PostWrapper> getPosts() {
        return children;
    }
}
