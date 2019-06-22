package com.example.project1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project1.chat.MsnSend;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText TextEmail;
    private EditText TextPassword;
    private Button btnRegister, btnLogin;
    private ProgressDialog progressDialog;

    private int REQUEST_PERMISSION = 200;
    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    //Firebase database and storage
    //private FirebaseDatabase database;
    //private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        requests();
        //database = FirebaseDatabase.getInstance();
       // databaseReference = database.getReference("users");
        storage = FirebaseStorage.getInstance();

        //Referenciamos los views
        TextEmail = (EditText) findViewById(R.id.TxtEmail);
        TextPassword = (EditText) findViewById(R.id.TxtPassword);

        btnRegister = (Button) findViewById(R.id.botonRegistrar);
        btnLogin = (Button) findViewById(R.id.botonLogin);


        progressDialog = new ProgressDialog(this);

        //asociamos un oyente al evento clic del botón
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    /**
                     * The user is logged
                     */
                    //int pos = firebaseAuth.getCurrentUser().getEmail().indexOf("@");
                    //String user = firebaseAuth.getCurrentUser().getEmail().substring(0, pos);
                    Intent intention = new Intent(MainActivity.this,Welcome.class);
                    startActivity(intention);
                    finish();
                }else{
                    /**
                     * The user is not logged
                     */
                    firebaseAuth = FirebaseAuth.getInstance();
                }
            }
        };
    }


    /**
     * Register user
     */
    private void registerUser() {
        /**
         * We obtain the email and password from the edit text boxes
         */
        final String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        /**We verify that the text boxes are not empty
         */
         if(email == null || email.trim().isEmpty()) {
            Toast.makeText(this, "You must enter an email", Toast.LENGTH_LONG).show();
            return;
        }
        if(password == null || password.trim().isEmpty()){
            Toast.makeText(this, "You must enter a password", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.trim().length() < 6) {
            Toast.makeText(this, "The password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Making online registration...");
        progressDialog.show();

        /**
         * Register a new user
         */
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /**
                         * Checking if success
                         */
                        if (task.isSuccessful()) {
                            int pos = firebaseAuth.getCurrentUser().getEmail().indexOf("@");

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                            DatabaseReference currentUserDB = mDatabase.child(firebaseAuth.getCurrentUser().getUid());
                            currentUserDB.child("name").setValue(firebaseAuth.getCurrentUser().getEmail().substring(0, pos));
                            currentUserDB.child("image").setValue("default");

                            Toast.makeText(MainActivity.this, "The user has successfully registered with the email: " + TextEmail.getText(), Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(MainActivity.this, "There is already an user with that email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "The email is not correct ", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    /**
     * Login user function
     */
    private void logUser() {
        /**
         * We obtain the email and the poassword from the edit text boxes
         */
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();
        /**
         * We verify that the text boxes are not empty
         */
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You have to enter an email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "You have to enter a password", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Making online registration...");
        progressDialog.show();

        /**
         * Making the user log
         */
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Welcome: " + TextEmail.getText(), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this, "The email or password is not correct ", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    /**
     * The methods onStart and onStop are needed for the listener
     */
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null){
            firebaseAuth.removeAuthStateListener(listener);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.botonRegistrar:
                registerUser();
                break;
            case R.id.botonLogin:
                logUser();
                break;
        }


    }

    /**
     * Check permission if run higher API 23
     * @return true if we have permissions
     */
    private boolean requests() {

        try{
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                },REQUEST_PERMISSION);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
}