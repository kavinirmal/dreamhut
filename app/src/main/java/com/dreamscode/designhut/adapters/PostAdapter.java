package com.dreamscode.designhut.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.created_view.RoundCornerImageView;
import com.dreamscode.designhut.dto.PostDto;
import com.dreamscode.designhut.main_fragments.HomeFragment;
import com.dreamscode.designhut.ui.ImageViewActivity;
import com.dreamscode.designhut.ui.MyHutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context mContext;
    List<PostDto> mData;
    FirebaseFirestore fStore;
    StorageReference user_storage;
    StorageReference post_storage;
    Boolean check;
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
        fStore = FirebaseFirestore.getInstance();

        String UserId = mData.get(position).getUserId().toString();
        String PostId = mData.get(position).getPostId().toString();
        String DateTime = mData.get(position).getDateTime().toString();
        String Description = mData.get(position).getPostDescription().toString();

        fStore = FirebaseFirestore.getInstance();

        fStore.collection("Likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        String postId = documentSnapshot.getData().get("PostId").toString();
                        String userId = documentSnapshot.getData().get("UserId").toString();
                        if (postId.equals(PostId) && userId.equals(UserId)){
                            check = true;
                            break;
                        }else {
                            check=false;
                        }

                    }
                    if (check){
                        holder.img_like.setImageResource(R.drawable.ic_heart_select);
                    }else {
                        holder.img_like.setImageResource(R.drawable.ic_heart_not_select);
                    }
                }else {
                    Toast.makeText(mContext,"Error occurred...!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Error occurred...!",Toast.LENGTH_SHORT).show();
            }
        });
        fStore.collection("User").document(UserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = (String) documentSnapshot.getData().get("UserName");
                holder.tv_user_name.setText(username);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        user_storage = FirebaseStorage.getInstance().getReference().child("profile_images/" + UserId);
        user_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.img_user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        post_storage = FirebaseStorage.getInstance().getReference().child("post_image/"+PostId);
        post_storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.img_post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fStore.collection("Likes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                String postId = documentSnapshot.getData().get("PostId").toString();
                                String userId = documentSnapshot.getData().get("UserId").toString();
                                String likesId= documentSnapshot.getId().toString();

                                if (postId.equals(PostId) && userId.equals(UserId)){
                                    //dislike
                                    holder.img_like.setImageResource(R.drawable.ic_heart_not_select);
                                    DocumentReference documentReference = fStore.collection("Likes").document(likesId);
                                    Toast.makeText(mContext, "Disliked", Toast.LENGTH_SHORT).show();
                                    documentReference.delete();
                                    check = false;
                                    break;
                                }else {
                                    check=true;
                                }
                            }
                            if (check){
                                holder.img_like.setImageResource(R.drawable.ic_heart_select);
                                String likes_id = fStore.collection("Likes").document().getId();
                                DocumentReference documentReference = fStore.collection("Likes").document(likes_id);
                                Map<String,Object> post = new HashMap<>();
                                post.put("UserId",UserId);
                                post.put("PostId",PostId);
                                documentReference.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(mContext, "Liked", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext, "Try Again...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else {
                            Toast.makeText(mContext,"Error occurred...!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext,"Error occurred...!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, ImageViewActivity.class);
                intent.putExtra("PostId",PostId);
                mContext.startActivity(intent);
            }
        });
        holder.img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, MyHutActivity.class);
                intent.putExtra("UserId",UserId);
                mContext.startActivity(intent);
            }
        });
        holder.tv_time.setText(DateTime);
        holder.tv_description.setText(Description);

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
