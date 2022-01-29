package com.dreamscode.designhut.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.created_view.RoundCornerImageView;
import com.dreamscode.designhut.dto.PostDto;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context mContext;
    List<PostDto> mData;
    private PostViewHolder.RecyclerViewClickListener clicklistener;

    public PostAdapter(Context mContext, List<PostDto> mData, PostViewHolder.RecyclerViewClickListener listener){
        this.mContext = mContext;
        this.mData = mData;
        this.clicklistener =listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_post,viewGroup,false);
        return new PostViewHolder(layout,mContext,mData,clicklistener);    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.img_user.setImageResource(mData.get(position).getImg_user());
        holder.img_post.setImageResource(mData.get(position).getImg_post());
        holder.tv_user_name.setText(mData.get(position).getUser_name());
        holder.tv_time.setText(mData.get(position).getTime());
        holder.tv_description.setText(mData.get(position).getDescription());

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.img_like.setImageResource(R.drawable.ic_heart_select);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img_user;
        TextView tv_user_name,tv_time,tv_description;
        RoundCornerImageView img_post;
        ImageView img_like;
        RecyclerViewClickListener mrecyclerViewClickListener;
        public PostViewHolder(@NonNull View itemView, Context mContext, List<PostDto> mData, RecyclerViewClickListener clicklistener) {
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_description = itemView.findViewById(R.id.tv_description);
            img_post = itemView.findViewById(R.id.img_post);
            img_like = itemView.findViewById(R.id.img_like);
            this.mrecyclerViewClickListener = clicklistener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mrecyclerViewClickListener.onClickListener(getAdapterPosition());
        }
        public interface RecyclerViewClickListener {
            void onClickListener(int position);
        }
    }
}
