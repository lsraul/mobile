package com.example.project1;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.model.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

import static android.app.Activity.RESULT_OK;


/**
 * Edit the profile image, the email and the username
 */
public class fragment_edit extends Fragment {


    /**
     * Variables declaration
     */
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ImageView imageProfile;
    private EditText textName;
    private EditText textEmail;
    private EditText textPassword;
    private Button updateName;
    private Button updateEmail;
    private Button updatePassword;
    private FirebaseUser user;
    private DatabaseReference currentUserDB;
    private StorageReference filepath;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        /**
         * Inicialitation of variables
         */
        progressDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        imageProfile = (ImageView) view.findViewById(R.id.imageView);
        textName = (EditText) view.findViewById(R.id.txtName);
        textEmail = (EditText) view.findViewById(R.id.txtEmail);
        //textPassword = (EditText) view.findViewById(R.id.txtPassword);
        updateEmail = (Button) view.findViewById(R.id.updateEmail);
        updateName = (Button) view.findViewById(R.id.updateName);
        //updatePassword = (Button) view.findViewById(R.id.updatePassword);

        // Imagen profile listener
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent, "Select a picture for your profile"), CAMERA_REQUEST_CODE);
            }
        });
        //Name listener
        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            updateName(v);
            }
        });
        //Email listener
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            updateEmail(v);
            }
        });

        /*updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword(v);
            }
        });
        */

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser user = mAuth.getCurrentUser();
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    //textName.setText(user.getDisplayName());
                    textEmail.setText(user.getEmail());

                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            textName.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                            String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                            if (URLUtil.isValidUrl(imageUrl))
                                Picasso.with(getContext()).load(Uri.parse(imageUrl)).into(imageProfile);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        return view;
    }

    /**
     * Gets a ramdom string
     * @return
     */
    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri u = data.getData();
            uploadImage(u);

        }
        return;
    }

    /**
     * Update image profile
     * @param fileUri
     */
    public void uploadImage(final Uri fileUri) {
        if (mAuth.getCurrentUser() == null)
            return;

        if (mStorage == null)
            mStorage = FirebaseStorage.getInstance().getReference();
        if (mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        filepath = mStorage.child("Photos").child(getRandomString());/*uri.getLastPathSegment()*/
        currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());

        progressDialog.setMessage("Uploading image...");
        progressDialog.show();

        currentUserDB.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.getValue().toString();

                if (!image.equals("default") && !image.isEmpty()) {
                    Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(getContext(), "Deleted image succesfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Deleted image failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                currentUserDB.child("image").removeEventListener(this);

                filepath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                Uri downloadUri = uri;
                                //Picasso.with(getContext()).load(fileUri).fit().centerCrop().into(imageProfile);
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                currentUserDB.child("image").setValue(downloadUri.toString());
                            }
                        });
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * Update name
     * @param view
     */
    public void updateName(View view) {

        user = mAuth.getCurrentUser();
        currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
        String newName = textName.getText().toString();

        if (TextUtils.isEmpty(newName))
            return;

        currentUserDB.child("name").setValue(newName);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                //.setPhotoUri(Uri.parse("https://www.famouslogos.net/images/android-logo.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * Update email
     * @param view
     */
    public void updateEmail(View view) {
        String newEmail = textEmail.getText().toString();
        if (TextUtils.isEmpty(newEmail))
            return;

        user = mAuth.getCurrentUser();

        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getContext(), "email updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Update password. Not used
     * @param view
     */
    public void updatePassword(View view) {
        user = mAuth.getCurrentUser();
        String newPassword = textPassword.getText().toString();
        if (TextUtils.isEmpty(newPassword))
            return;

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getContext(), "password updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
