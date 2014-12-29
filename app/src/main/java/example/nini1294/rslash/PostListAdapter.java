package example.nini1294.rslash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nishant on 28/12/14.
 */
public class PostListAdapter extends ArrayAdapter<Post> {
    public PostListAdapter(Context context, List<Post> objects) {
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

    static class PostViewHolder {
        TextView title;
        TextView userName;
        TextView upvotes;
        TextView age;
        TextView url;
//        ImageView thumbnail;

    }
}
