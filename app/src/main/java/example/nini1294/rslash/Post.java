package example.nini1294.rslash;

import android.text.format.Time;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Nishant on 26/12/14.
 */
public class Post {
    private String title;
    private String userName;
    private URL link;
    private String imageUrl;
    private String upvotes;
    private String age;
    private boolean isSelf;

    public Post(String title, String userName, String url, String imageUrl, String upvotes, double age, boolean isSelf) {
        this.title = title;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.upvotes = upvotes;
        this.isSelf = isSelf;
        Time ttime = new Time("UTC");
        ttime.set(((Double) age).longValue());
        ttime.switchTimezone(Time.getCurrentTimezone());
        long temp = System.currentTimeMillis() - ttime.toMillis(true) * 1000;
        if (temp < 60000) {
            this.age = "Just Now";
        } else if (temp / 60000 < 60) {
            this.age = temp / 60000 + " minutes ago";
        } else if (temp / (1000 * 60 * 60) < 24) {
            this.age = temp / (1000 * 60 * 60) + " hours ago";
        } else if (temp / (1000 * 60 * 60 * 24) < 30 ) {
            this.age = temp / (1000 * 60 * 60 * 24) + " days ago";
        } else {
            this.age = "A long time ago";
        }
        try {
            this.link = new URL(url);
//                Log.i("Lgo - Post", link.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getImageURL() {
        return this.imageUrl;
    }

    public URL getURL() {
        return this.link;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public String getAge() {
        return age;
    }

    public String getURLString() {
        if(isSelf) {
            return "self";
        } else {
            return getURL().getHost();
        }
    }

    public boolean getSelf() {
        return isSelf;
    }

    public String toString() {
        return this.getTitle() + ":" + this.getUserName() + "\nURL:" + this.getURL().toString()
                + "\nImage:" + this.getImageURL();
    }


}