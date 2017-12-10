package com.example.harshit.tvdb.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.example.harshit.tvdb.Utils.Preference;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private EditText et_pwd, et_email;
    private Button btn_login, btn_register;
    private static final String TAG = "LOGIN_ACTIVITY";
    // this is the request code
    private static final int RC_SIGN_IN = 9001;


    // these are th buttons
    private SignInButton signInButton;
    private LoginButton login_button;

    // this is the firebase auth instnce
    private FirebaseAuth firebaseAuth;

    // this is the firebase auth listner
    private FirebaseAuth.AuthStateListener mAuthListener;

    // this is the callback manager for te facbook
    private CallbackManager mCallbackManager;

    // this is the google client
    private GoogleApiClient mGoogleApiClient;
    private String email, password;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialse the facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        // initialise the firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        initViews();
        setListners();
        getKeyHash();
        fbComponents();
        googleComponents();
    }

    private void getKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.harshit.tvdb", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void googleComponents() {
        // Google Sign-In
        // Assign fields
        // Set click listeners

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void fbComponents() {
        // Facebook Login
        mCallbackManager = CallbackManager.Factory.create();

//        login_button.setReadPermissions("email", "public_profile", "user_birthday", "user_friends");
        login_button.setReadPermissions("email", "public_profile");

        login_button.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

    }

    private void initViews() {

        et_pwd = findViewById(R.id.et_pwd);
        et_email = findViewById(R.id.et_email);
        btn_login = findViewById(R.id.btn_login);
        progress = findViewById(R.id.progress);
        btn_register = findViewById(R.id.btn_register);

        // intilaise the buttons for fb and google

        // this is for the fb
        signInButton = findViewById(R.id.sign_in_button);
        login_button = findViewById(R.id.login_button);

    }

    private void setListners() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                AppUtil.hideKeypad(this);
                if (getDataOfLayout()) {
                    loginWithFirebase();
                }
                break;
            case R.id.btn_register:
                AppUtil.hideKeypad(this);
                btn_login.setVisibility(View.GONE);
                btn_register.setVisibility(View.VISIBLE);
                login_button.setVisibility(View.GONE);
                signInButton.setVisibility(View.GONE);
                if (getDataOfLayout()) {
                    registerWithFirebase();
                }
                break;
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private boolean getDataOfLayout() {
        email = et_email.getText().toString().trim();
        password = et_pwd.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            et_email.setError("Please enter Email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            et_pwd.setError("Please enter Password");
            return false;
        }
        return true;

    }

    private void registerWithFirebase() {

        if (AppUtil.isNetworkAvailable(this)) {

            progress.setVisibility(View.VISIBLE);

            //creating a new user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            progress.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                //display some message here
                                Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();
                                btn_register.setVisibility(View.GONE);

                                // here we get the task and we can find the firebase user through it
                                FirebaseUser user = task.getResult().getUser();
                                if (user != null) {
                                    Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
                                    intent.putExtra("USER_TOKEN", user.getUid());
                                    intent.putExtra("COMING_FROM", "Login");

                                    intent.putExtra("EMAIL", user.getEmail());
                                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                    finish();

                                } else {
                                    AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
                                    finish();
                                }


                            } else {
                                progress.setVisibility(View.GONE);

                                //display some message here
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    et_email.setText("");
                                    et_pwd.setText("");
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    et_email.setText("");
                                    et_pwd.setText("");
                                } catch (Exception e) {
                                    Log.e("MESSAGE", e.getMessage());
                                }
                            }
                        }
                    });
        } else {
            AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        if (AppUtil.isNetworkAvailable(this)) {
            progress.setVisibility(View.VISIBLE);

            final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progress.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                FirebaseUser user = task.getResult().getUser();
                                if (user != null) {
                                    Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
                                    intent.putExtra("USER_TOKEN", user.getUid());
                                    intent.putExtra("EMAIL", user.getEmail());
                                    intent.putExtra("NAME", user.getDisplayName());
                                    intent.putExtra("IMAGE", user.getPhotoUrl().toString());
                                    intent.putExtra("COMING_FROM", "Login");

                                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                    finish();

                                } else {
                                    AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
                                    finish();
                                }
                            }
                        }
                    });
        } else {
            AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }


    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        if (AppUtil.isNetworkAvailable(this)) {

            progress.setVisibility(View.VISIBLE);

            final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progress.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = task.getResult().getUser();
                                if (user != null) {
                                    Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
                                    intent.putExtra("USER_TOKEN", user.getUid());
                                    intent.putExtra("EMAIL", user.getEmail());
                                    intent.putExtra("NAME", user.getDisplayName());
                                    intent.putExtra("IMAGE", user.getPhotoUrl().toString());
                                    intent.putExtra("COMING_FROM", "Login");
                                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                    finish();
                                } else {
                                    AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
                                    finish();
                                }
//
                            }
                        }
                    });
        } else {
            AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }


    private void loginWithFirebase() {

        //authenticate user
        if (AppUtil.isNetworkAvailable(this)) {
            progress.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progress.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                // there was an error
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            } else {
                                FirebaseUser user = task.getResult().getUser();
                                Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
                                intent.putExtra("USER_TOKEN", user.getUid());
                                intent.putExtra("EMAIL", user.getEmail());
                                intent.putExtra("COMING_FROM", "Login");

                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                finish();

                            }
                        }
                    });
        } else {
            AppUtil.openNonInternetActivity(LoginActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && Preference.readBoolean(this, Preference.is_User_Info_saved, false)) {
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            intent.putExtra("USER_TOKEN", user.getUid());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
