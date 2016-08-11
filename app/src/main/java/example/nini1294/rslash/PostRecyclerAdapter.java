package example.nini1294.rslash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
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
        return new PostViewHolder(itemView, new PostViewHolder.IPostViewHolderClicks() {
            @Override
            public void onClick(View v, int pos) {
                Logger.i("Post Clicked: " + pos);
                openLink(pos);
            }

            @Override
            public boolean onLongClick(View v, int pos) {
                Logger.i("Post long clicked " + pos);
                return true;
            }
        });
    }

    private void openLink(int pos) {
        Uri u = Uri.parse(data.get(pos).getURL().toString());
        Intent i = new Intent(Intent.ACTION_VIEW, u);
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
        if (data.get(position).isSelf()) {
            holder.thumbnail.setVisibility(View.GONE);
        } else {
            Picasso.with(context).load(post.getImageURL()).into(holder.thumbnail);
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.title_textView) TextView title;
        @BindView(R.id.user_textView) TextView userName;
        @BindView(R.id.upvotes_textView) TextView upvotes;
        @BindView(R.id.age_textView) TextView age;
        @BindView(R.id.url_textView) TextView url;
        @BindView(R.id.image_imageView) ImageView thumbnail;

        IPostViewHolderClicks click;

        public PostViewHolder(View view, IPostViewHolderClicks click) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            this.click = click;
        }

        @Override
        public void onClick(View v) {
            click.onClick(v, this.getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return click.onLongClick(v, this.getAdapterPosition());
        }

        public interface IPostViewHolderClicks {
            void onClick(View v, int position);
            boolean onLongClick(View v, int position);
        }
    }
}
