package example.nini1294.rslash.POJOs;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Nishant on 26/12/14.
 */
public class Post {
    private String title;
    private String author;
    private URL url;
    private String thumbnail;
    private String score;
    private long created;
    private boolean is_self;

    public Post(String title, String userName, String url, String thumbnail, String score, double created, boolean is_self) {
        this.title = title;
        this.author = author;
        this.thumbnail = thumbnail;
        this.score = score;
        this.is_self = is_self;
        try {
            this.url = new URL(url);
//                Log.i("Lgo - Post", url.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getUsername() {
        return this.author;
    }

    public String getImageURL() {
        return this.thumbnail;
    }

    public URL getURL() {
        return this.url;
    }

    public String getScore() {
        return score;
    }

    public long getCreated() {
        return created;
    }

    public String getAge() {
        String age;
        long temp = System.currentTimeMillis() - this.created * 1000;
        Log.i("Post", "Millis " + temp );
        if (temp < 60000) {
            age = "Just Now";
        } else if (temp / 60000 < 60) {
            age = temp / 60000 + " minutes ago";
        } else if (temp / (1000 * 60 * 60) < 24) {
            age = temp / (1000 * 60 * 60) + " hours ago";
        } else if (temp / (1000 * 60 * 60 * 24) < 90 ) {
            age = temp / (1000 * 60 * 60 * 24) + " days ago";
        } else {
            age = "A long time ago";
        }
        return age;
    }

    public String getURLString() {
        if(is_self) {
            return "self";
        } else {
            return getURL().getHost();
        }
    }

    public boolean getSelf() {
        return is_self;
    }

    public String toString() {
        return this.getTitle() + ":" + this.getUsername() + "\nURL:" + this.getURL().toString()
                + "\nImage:" + this.getImageURL();
    }


}