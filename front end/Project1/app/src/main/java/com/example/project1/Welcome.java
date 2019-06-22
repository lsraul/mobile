package com.example.project1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class Welcome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    /**
     * Declaration of the variables
     */
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener listener;
    private ProgressDialog progressDialog;
    private static String userName;
    private static String userID;

    private static CircleImageView imageView;
    private int PHOTO_PERFIL = 0;

    TextView textUser;

    /**
     * Charges everithing when we start the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        /**
         * Listener to know if the user is logged
         * If the user is not logged we go to the main activity
         * If he is logged we charge the image profile and the name
         */
        listener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    //The user is not loged
                    Toast.makeText(Welcome.this, "Logged out", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(Welcome.this, MainActivity.class));

                }else{
                    //The user is loged

                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                           userName = String.valueOf(dataSnapshot.child("name").getValue());
                            textUser.setText(userName);
                            String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                            if (URLUtil.isValidUrl(imageUrl))
                               // Picasso.with(getContext()).load(Uri.parse(imageUrl)).into(imageProfile);
                                Glide.with(Welcome.this).load(Uri.parse(imageUrl)).into(imageView);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        };



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * Load default fragment
         */
        loadFragment(new fragment_map());
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        textUser =(TextView)findViewById(R.id.textUser);
        //Image profile
        imageView = (CircleImageView) findViewById(R.id.imageView);


        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_map) {
            loadFragment(new fragment_map());
        } else if (id == R.id.nav_places) {
            loadFragment(new fragment_listplaces());
        } else if (id == R.id.nav_chat) {
            loadFragment(new fragment_chat());
        } else if (id == R.id.nav_edit) {
            loadFragment(new fragment_edit());
        } else if (id == R.id.nav_log_out) {
            //Log out
            FirebaseAuth.getInstance().signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * loads the fragment
     * @param fragment
     */
    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }



    public static String getUserName() {
        return userName;
    }

    public void userID(){
        if(firebaseAuth.getCurrentUser() != null){
            userID= firebaseAuth.getCurrentUser().getUid();
        }
    }
    public static String getUserID(){
        return userID;
    }

    public static CircleImageView getUserImage(){
        return imageView;
    }



}
