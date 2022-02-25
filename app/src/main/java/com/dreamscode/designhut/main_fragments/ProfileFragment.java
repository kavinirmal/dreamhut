package com.dreamscode.designhut.main_fragments;

import static android.app.Activity.RESULT_OK;
import static android.telephony.SmsManager.RESULT_CANCELLED;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamscode.designhut.R;
import com.dreamscode.designhut.onboarding_ui.SignUpActivity;
import com.dreamscode.designhut.ui.ForgetPasswordActivity;
import com.dreamscode.designhut.ui.MyHutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView tv_user_name,tv_email,tv_post_shared,tv_edit,tv_log_out,tv_my_hut;
    ImageView ic_edit_email,ic_edit_password,img_profile_photo;
    Button btn_new;
    String input_text;
    Bitmap image_file;
    String name, email,Uid;
    Long post;
    ProgressBar progress;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference storageReference;
    UploadTask uploadTask;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELLED){
            switch (requestCode){
                case 0:
                    if (resultCode == RESULT_OK && data != null){
                        image_file= (Bitmap) data.getExtras().get("data");
                        UploadImage(image_file);
                    }else {
                        Toast.makeText(getContext(),"Please try again...",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null){
                        Uri path = data.getData();
                        try {
                            image_file = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                            UploadImage(image_file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Please try again...",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"Please try again...",Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }

    private void UploadImage(Bitmap image_file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image_file.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + mAuth.getUid());
        uploadTask = storageReference.putBytes(byteArray);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                img_profile_photo.setImageBitmap(image_file);
                Toast.makeText(getContext(),"Profile picture uploaded successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Profile picture uploaded unsuccessfully...! Please try again",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_post_shared = view.findViewById(R.id.tv_post_shared);
        tv_edit = view.findViewById(R.id.tv_edit);
        tv_log_out = view.findViewById(R.id.tv_log_out);
        ic_edit_email = view.findViewById(R.id.ic_edit_email);
        ic_edit_password = view.findViewById(R.id.ic_edit_password);
        btn_new = view.findViewById(R.id.btn_new);
        img_profile_photo = view.findViewById(R.id.img_profile_photo);
        progress = view.findViewById(R.id.progress);
        tv_my_hut = view.findViewById(R.id.tv_my_hut);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);
        LoadUserDetails();
        LoadProfileImage();

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateName();
            }
        });
        ic_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReAuthAlert();
            }
        });
        ic_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetLink();
            }
        });
        tv_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutUser();
            }
        });
        tv_my_hut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MyHutActivity.class);
                intent.putExtra("UserId",Uid);
                startActivity(intent);
            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new AddNewFragment()).commit();

            }
        });
        img_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] option = {"Take Photo","From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose your option");
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

    private void LoadProfileImage() {
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images/" + mAuth.getUid());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img_profile_photo);
                progress.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void LogOutUser() {
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }
    private void sendResetLink() {
        String m_email = tv_email.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(m_email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Reset link sent. check your emails...!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Error occurred please try again...!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ReAuthAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Re Authentication");
        builder.setMessage("Please enter password");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                ReAuthnticate(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void ReAuthnticate(String m_text) {
        String m_email =tv_email.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(m_email, m_text);
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateEmail();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error occurred Try again...!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateEmail() {
        String title = "Update";
        String message = "Enter your new Email";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input_text = input.getText().toString();
                user.updateEmail(input_text).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.collection("User").document(Uid).update("UserEmail",input_text);
                        LoadUserDetails();
                        Toast.makeText(getContext(),"Email Updated!..",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void UpdateName() {
        String title = "Update";
        String message = "Enter your new user name";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input_text = input.getText().toString();
                db.collection("User").document(Uid).update("UserName",input_text);
                LoadUserDetails();
                Toast.makeText(getContext(),"User Name Updated!..",Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    private void LoadUserDetails() {
        DocumentReference docRef = db.collection("User").document(Uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = (String)document.getData().get("UserName");
                        email = (String)document.getData().get("UserEmail");
                        post = (Long) document.getData().get("PostCount");
                        String s_post = Long.toString(post);
                        setDetails(name,email,s_post);
                    } else {
                        System.out.println( "No such document");
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
    private void setDetails(String name, String email, String post) {
        tv_email.setText(email);
        tv_user_name.setText(name);
        tv_post_shared.setText(post);
    }
}