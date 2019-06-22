package com.example.project1.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.SecureRandom;


public class CurrentUser {

    // private String userID;
    private String name;
    private String photoPerfil;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener listener;
    private Uri u;

    public String getUserID(){
        if(firebaseAuth.getCurrentUser() != null){
           return firebaseAuth.getCurrentUser().getUid();
        }else return "default";
    }

    public String getUserName(){

        name="null";
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        if(firebaseAuth.getCurrentUser() != null){
            mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name= String.valueOf(dataSnapshot.child("name").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return name;
    }


    private void onCreate() {
        firebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name = String.valueOf(dataSnapshot.child("name").getValue());
                            photoPerfil = String.valueOf(dataSnapshot.child("image").getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
    }

    /* public String getUserID() {
         return userID;
     }

     public void setUserID(String userID) {
         this.userID = userID;
     }
 */
    public String getName() {
        firebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name = String.valueOf(dataSnapshot.child("name").getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final Uri getPhotoPerfil() {

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //text.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                if (URLUtil.isValidUrl(imageUrl))
                    // Picasso.with(getContext()).load(Uri.parse(imageUrl)).into(imageProfile);
                    u = Uri.parse(imageUrl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return u;
/*
        mStorage.child("Photos").child("djpl9b11nrrp0v59uutarjdket").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                progressDialog.dismiss();
                u = uri;

            }
        });
        mDatabase.child("profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png' in uri
                System.out.println(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        mDatabase.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri u = uri;
                fotoPerfilCadena=u.toString();
                MsnSend m = new MsnSend(welcome.getUserName() + "  ha actualizado su foto de perfil", u.toString(), nombre.getText().toString(),fotoPerfilCadena, "2", ServerValue.TIMESTAMP);
                databaseReference.push().setValue(m);
                Glide.with(fragment_chat.this).load(u.toString()).into(fotoPerfil);

            }
        });
        return u;

*/
    }

    public void setPhotoPerfil(final Uri fileUri,final Context context) {
        onCreate();
        if (firebaseAuth.getCurrentUser() == null)
            return;

        if (mStorage == null)
            mStorage = FirebaseStorage.getInstance().getReference();
        if (mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        final StorageReference filepath = mStorage.child("Photos").child(getRandomString());/*uri.getLastPathSegment()*/
        final DatabaseReference currentUserDB = mDatabase.child(firebaseAuth.getCurrentUser().getUid());

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
                                Toast.makeText(context, "Deleted image succesfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, "Deleted image failed", Toast.LENGTH_SHORT).show();
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
                                //Glide.with(context).load(currentuser.getPhotoPerfil()).into(imageView);
                                DatabaseReference currentUserDB = mDatabase.child(firebaseAuth.getCurrentUser().getUid());
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

    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}

