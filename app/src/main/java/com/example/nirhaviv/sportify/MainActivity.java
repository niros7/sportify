package com.example.nirhaviv.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.example.nirhaviv.sportify.activities.AuthActivity;
import com.example.nirhaviv.sportify.activities.FeedActivity;
import com.example.nirhaviv.sportify.drawer.DrawerUtils;
import com.example.nirhaviv.sportify.model.entities.Post;
import com.example.nirhaviv.sportify.model.entities.User;
import com.example.nirhaviv.sportify.model.firebase.PostsFirebase;
import com.example.nirhaviv.sportify.model.firebase.UsersFirebase;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
//        implements NavigationView.OnNavigationItemSelectedListener {


    private SignInButton signInBtn;
    private Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        User user = new User();
//        user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//        user.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//        user.setProfileUri(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
//        user.setUid(UUID.randomUUID().toString());
//        user.setUserUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        UsersFirebase.getInstance().saveUser(user);



        try {
            DrawerUtils.getDrawer(this, toolbar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        }

//
//        mAuth = FirebaseAuth.getInstance();
//
////        if (mAuth.getCurrentUser() == null) {
////            inflateAuthFragment();
////        } else {
////            inflateFeedFragment();
////        }
//
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    Toast.makeText(MainActivity.this,
//                            "User " + firebaseAuth.getCurrentUser().getEmail() + " already connected",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this,
//                            "User is disconnected",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
//                .enableAutoManage(this,
//                        new GoogleApiClient.OnConnectionFailedListener() {
//                            @Override
//                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                                Toast.makeText(MainActivity.this,
//                                        "Failed to connect to google",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        signInBtn = (SignInButton) findViewById(R.id.signInBtn);
//        signInBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });

//        signOutBtn = (Button) findViewById(R.id.signOutBtn);
//        signOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });
    }

//    private void inflateAuthFragment() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
////        mainFrag = new MainUserFragment();
//
//        fragmentTransaction.add(R.id.mainContent, mainFrag);
//        fragmentTransaction.commit();
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(MainActivity.this,
//                                    "User: " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(MainActivity.this,
//                                    "Failed",
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//                        // ...
//                    }
//                });
//    }
//
//
//    @OnClick(R.id.signInBtn)
//    public void signInClick(View view) {
//        signIn();
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
