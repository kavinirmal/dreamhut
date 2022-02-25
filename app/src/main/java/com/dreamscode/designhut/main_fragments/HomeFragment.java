package com.dreamscode.designhut.main_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.adapters.PostAdapter;
import com.dreamscode.designhut.created_view.PopUpClass;
import com.dreamscode.designhut.dto.PostDto;
import com.dreamscode.designhut.ui.ForgetPasswordActivity;
import com.dreamscode.designhut.ui.ImageViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements PostAdapter.PostViewHolder.RecyclerViewClickListener {
    RecyclerView rv_post;
    PostAdapter postAdapter;
    List<PostDto> mData;
    FirebaseFirestore fStore;
    ProgressBar progress;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rv_post = view.findViewById(R.id.rv_post);
        progress = view.findViewById(R.id.progress);
        fStore = FirebaseFirestore.getInstance();

        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);
        loadPosts();
        return view;
    }

    private void loadPosts() {
        fStore.collection("Post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    mData = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        //description ok
                        String PostDescription = documentSnapshot.getData().get("PostDescription").toString();
                        String DateAndTime = documentSnapshot.getData().get("DateTime").toString();
                        String postId = documentSnapshot.getId().toString();
                        String UserId = documentSnapshot.getData().get("UserId").toString();
                        mData.add(new PostDto(PostDescription,postId,UserId,DateAndTime));
                    }
                    postAdapter = new PostAdapter(getContext(),mData,HomeFragment.this::onClickListener);
                    rv_post.setAdapter(postAdapter);
                    rv_post.setLayoutManager(new LinearLayoutManager(getActivity()));
                    progress.setVisibility(View.INVISIBLE);

                }else {
                    Toast.makeText(getContext(),"Error occurred...!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error occurred...!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickListener(int position) {

//        PopUpClass popUpClass = new PopUpClass();
//        popUpClass.showPopupWindow(getView());

    }
}