package com.dreamscode.designhut.main_fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.onboarding_ui.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFragment extends Fragment {
    TextView tv_add;
    TextView tv_user_name;
    EditText et_description;
    ImageView img_post,img_user;
    Button btn_upload;
    String Uid;
    String post_id;
    ProgressBar progress;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageReference;
    Bitmap image_file;
    UploadTask uploadTask;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewFragment newInstance(String param1, String param2) {
        AddNewFragment fragment = new AddNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED){
            switch (requestCode){
                case 0:
                    if (resultCode == RESULT_OK && data!= null){
                        image_file = (Bitmap) data.getExtras().get("data");
                        img_post.setImageBitmap(image_file);
                    }else {
                        Toast.makeText(getContext(), "Image not uploaded...!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null){
                        Uri path = data.getData();
                        try {
                            image_file = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                            img_post.setImageBitmap(image_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getContext(), "Image not uploaded...!", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }else {
            Toast.makeText(getContext(), "Image not uploaded...!", Toast.LENGTH_SHORT).show();
        }
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
        View view = inflater.inflate(R.layout.fragment_add_new, container, false);

        tv_add = view.findViewById(R.id.tv_add);
        img_post = view.findViewById(R.id.img_post);
        btn_upload = view.findViewById(R.id.btn_upload);
        et_description = view.findViewById(R.id.et_description);
        progress = view.findViewById(R.id.progress);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        img_user = view.findViewById(R.id.img_user);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Uid = mAuth.getUid();

        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);
        LoadDetails();
        LoadImage();

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] option = {"Take Photo","From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select your option");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture,0);
                                break;
                            case 1:
                                Intent pickPicture = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPicture,1);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    private void LoadImage() {
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + mAuth.getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img_user);
                progress.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void LoadDetails() {
        DocumentReference docRef = db.collection("User").document(Uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = (String)document.getData().get("UserName");
                        tv_user_name.setText(name);

                    } else {
                        Toast.makeText(getContext(),"Error occurred!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"Error occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error occurred!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validate() {
        String description;
        description = et_description.getText().toString();
        if (description.isEmpty() || image_file ==null){
            Toast.makeText(getContext(), "Missing Description or Image file...!!!", Toast.LENGTH_SHORT).show();
        }else {
            UploadPost();
        }
    }

    private void UploadPost() {
        progress.setVisibility(View.VISIBLE);
        String description = et_description.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        post_id = db.collection("Post").document().getId();
        DocumentReference documentReference = db.collection("Post").document(post_id);
        Map<String,Object> post = new HashMap<>();
        post.put("UserId",Uid);
        post.put("PostDescription",description);
        post.put("DateTime",currentDateandTime);
        documentReference.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                UploadImage(image_file,post_id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Post not uploaded...!", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void UploadImage(Bitmap image_file, String id) {
        //bitmap to bytearray
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image_file.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        storageReference = FirebaseStorage.getInstance().getReference().child("post_image/"+id);
        uploadTask = storageReference.putBytes(byteArray);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment()).commit();
                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Post not uploaded...!", Toast.LENGTH_SHORT).show();
                DocumentReference documentReference = db.collection("Post").document(post_id);
                documentReference.delete();
                progress.setVisibility(View.INVISIBLE);

            }
        });


    }
}