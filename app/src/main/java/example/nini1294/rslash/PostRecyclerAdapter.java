package example.nini1294.rslash;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nishant on 15/03/16.
 */
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder> {

    private List<Post> data;

    public PostRecyclerAdapter(List<Post> items) {
        if (items == null) {
            throw new IllegalArgumentException("Empty data");
        } else {
            this.data = items;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.title.setText(post.getTitle());
        holder.userName.setText(post.getUserName());
        holder.upvotes.setText(post.getUpvotes());
        holder.age.setText(post.getAge());
        holder.url.setText(post.getURLString());
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title_textView)
        TextView title;
        @Bind(R.id.user_textView) TextView userName;
        @Bind(R.id.upvotes_textView) TextView upvotes;
        @Bind(R.id.age_textView) TextView age;
        @Bind(R.id.url_textView) TextView url;
        //        ImageView thumbnail;
        public PostViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
