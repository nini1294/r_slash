package example.nini1294.rslash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.nini1294.rslash.POJOs.Post;

/**
 * Created by Nishant on 15/03/16.
 */
public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder> {

    private List<Post> data;
    Context context;

    public PostRecyclerAdapter(List<Post> items, Context context) {
        this.context = context;
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
        PostViewHolder vh = new PostViewHolder(itemView, new PostViewHolder.IPostViewHolderClicks() {
            @Override
            public void onClick(View v, int pos) {
                Log.i("Post Clicked: ", v + "");
                openLink(pos);
            }
        });
        return vh;
    }

    private void openLink(int position) {
        String link = data.get(position).getURLString();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(i);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.title.setText(post.getTitle());
        holder.userName.setText(post.getUsername());
        holder.upvotes.setText(post.getScore());
        holder.age.setText(post.getAge());
        holder.url.setText(post.getURLHost());
    }

    static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.title_textView)
        TextView title;
        @Bind(R.id.user_textView) TextView userName;
        @Bind(R.id.upvotes_textView) TextView upvotes;
        @Bind(R.id.age_textView) TextView age;
        @Bind(R.id.url_textView) TextView url;
        //        ImageView thumbnail;

        IPostViewHolderClicks click;

        public PostViewHolder(View view, IPostViewHolderClicks click) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            this.click = click;
        }

        @Override
        public void onClick(View v) {
            click.onClick(v, this.getAdapterPosition());
        }

        public static interface IPostViewHolderClicks {
            public void onClick(View v, int position);
        }
    }
}
