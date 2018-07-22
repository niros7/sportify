package com.example.nirhaviv.sportify.recycler.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nirhaviv.sportify.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    public List<PostForList> Posts;

    public PostAdapter(List<PostForList> posts) {
        if (posts == null) {
            Posts = new LinkedList<>();
        } else {
            Posts = posts;
        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);

        PostHolder ph = new PostHolder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        PostForList currentPost = this.Posts.get(position);
        holder.Background.setBackgroundDrawable(currentPost.Background);
        holder.PostText.setText(currentPost.PostText);
        holder.ProfileImage.setImageDrawable(currentPost.ProfileImage);
        holder.Timestamp.setText(new PrettyTime().format(currentPost.timestamp));
        holder.UserName.setText(currentPost.UserName);
    }

    @Override
    public int getItemCount() {
        return Posts.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {

        public CircleImageView ProfileImage;
        public TextView UserName;
        public TextView Timestamp;
        public AppCompatImageView Background;
        public TextView PostText;
        public CircleImageView RemoveButton;

        public PostHolder(View itemView) {
            super(itemView);
            ProfileImage = itemView.findViewById(R.id.post_profile_image);
            UserName = itemView.findViewById(R.id.post_user_name);
            Timestamp = itemView.findViewById(R.id.post_time_ago);
            Background = itemView.findViewById(R.id.post_backround);
            PostText = itemView.findViewById(R.id.post_text);
            RemoveButton = itemView.findViewById(R.id.remove_post_button);
        }
    }


}

