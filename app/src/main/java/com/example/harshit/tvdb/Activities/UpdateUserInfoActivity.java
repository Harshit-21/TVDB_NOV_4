package com.example.harshit.tvdb.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.UpdateAppearance;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harshit.tvdb.Pojo.Bean_Upload;
import com.example.harshit.tvdb.Pojo.Bean_UserInfo;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.example.harshit.tvdb.Utils.Preference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_userName, et_age, et_fname, et_lname, et_email;
    private Button btn_addUserInfo;
    private ImageView img_profile;
    private String user_token = "";
    private String username, age;
    private String email;
    private final static int PROFILE_IMAGE_CODE = 10930;
    private final static int COVER_IMAGE_CODE = 10920;
    private DatabaseReference mDatabase;
    private Bitmap bitmap;
    private String fname;
    private String lname;
    private Uri pickedImage;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<Bean_Upload> arr_images = new ArrayList<>();
    private String coming_from;
    private ProgressBar progress_user;
    final long ONE_MEGABYTE = 1024 * 1024 * 5;
    private String display_name;
    private String image_url;
    private ImageView img_cam;
    private String edit_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        mDatabase = FirebaseDatabase.getInstance().getReference("USER");

        //creating reference to firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_storage));

//        AppUtil.setActionBar(this);

        initViews();
        getDataFromBundle();
        setListners();
    }

    private void getDataFromBundle() {
        coming_from = getIntent().getStringExtra("COMING_FROM");
        user_token = getIntent().getStringExtra("USER_TOKEN");
        email = getIntent().getStringExtra("EMAIL");
        display_name = getIntent().getStringExtra("NAME");
        image_url = getIntent().getStringExtra("IMAGE");

        if (coming_from.equalsIgnoreCase("login")) {


            if (!TextUtils.isEmpty(display_name)) {
                et_fname.setText(display_name);
            }

            if (!TextUtils.isEmpty(image_url)) {
                Uri image_uri = Uri.parse(image_url);
//                img_profile.setImageURI(image_uri);
                Picasso.with(this).load(image_url).error(this.getResources().getDrawable(R.drawable.something_went_wrong)).into(img_profile);
                img_profile.setBackground(null);
            }


            Log.i("USER_TOOKEN", user_token);
            if (email != null) {
                et_email.setText(email);
            }
        } else {

            getTheUserInfo();

        }

    }


    private void getTheUserInfo() {

        if (AppUtil.isNetworkAvailable(this) && user_token != null) {
            progress_user.setVisibility(View.VISIBLE);
            mDatabase.child(user_token).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Bean_UserInfo user = dataSnapshot.getValue(Bean_UserInfo.class);
                    // here we get the complete info of user
                    setValueOfuser(user);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    progress_user.setVisibility(View.GONE);
                    Log.w("DETAILS", "Failed to read value.", error.toException());
                    AppUtil.openNonInternetActivity(UpdateUserInfoActivity.this, getResources().getString(R.string.something_went_wrong));

                }
            });
        } else {
            progress_user.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(UpdateUserInfoActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }


    private void setValueOfuser(Bean_UserInfo user) {
        if (user != null) {
            et_fname.setText(user.getFname());
            et_lname.setText(user.getLname());
            et_userName.setText(user.getUsername());
            et_email.setText(user.getEmail());
            et_age.setText(String.valueOf(user.getAge()));
// now we need to download from firebase storage
            //download file as a byte array
//            final String[] images_name = new String[]{"COVER", "Profile"};
            final String[] images_name = new String[]{"Profile"};
            for (int i = 0; i < images_name.length; i++) {

                final int finalI = i;
                storageRef.child(user_token + "/" + images_name[i]).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
//
//                    GenericTypedicator<ArrayList<Bean_Upload>> t = new GenericTypeIndicator<ArrayList<Bean_Upload>>() {};
//                ArrayList<Bean_Upload> yourStringArray = dataSnapshot.getValue(t);

                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        try {


//                            if (images_name[finalI].equalsIgnoreCase("COVER")) {
//
//
//                                bitmap1 = AppUtil.getResizedBitmap(bitmap1, img_cover.getWidth(), img_cover.getHeight());
//                                img_cover.setImageBitmap(bitmap1);
//                                tv_upload.setVisibility(View.GONE);
//                            } else {
                            bitmap1 = AppUtil.getResizedBitmap(bitmap1, img_profile.getWidth(), img_profile.getHeight());
                            img_profile.setImageBitmap(bitmap1);
//                                bitmap.recycle();
//                            }

                            if (bitmap1 != null && !bitmap1.isRecycled()) {
                                bitmap1.recycle();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    Bitmap bmImg = BitmapFactory.decodeStream(is);

                    }
                });
            }
            progress_user.setVisibility(View.GONE);

        }
    }


    private void setListners() {
        btn_addUserInfo.setOnClickListener(this);
        img_profile.setOnClickListener(this);
    }

    private void initViews() {
        btn_addUserInfo = findViewById(R.id.btn_addUserInfo);
        img_profile = findViewById(R.id.img_profile);
        et_email = findViewById(R.id.et_email);
        et_age = findViewById(R.id.et_age);
        et_userName = findViewById(R.id.et_userName);
        progress_user = findViewById(R.id.progress_user);
        img_cam = findViewById(R.id.img_cam);


        et_lname = findViewById(R.id.et_lname);
        et_fname = findViewById(R.id.et_fname);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_profile:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PROFILE_IMAGE_CODE);
                break;

            case R.id.btn_addUserInfo:
                AppUtil.hideKeypad(this);
                if (getDataOfLayout()) {
                    // here we have to stre boolean in shared prefernce
                    storeImage();

                }
                break;
        }
    }

    private boolean getDataOfLayout() {
        username = et_userName.getText().toString().trim();
        age = et_age.getText().toString().trim();
        fname = et_fname.getText().toString().trim();
        lname = et_lname.getText().toString().trim();
        edit_email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(fname)) {
            et_fname.setError("Please enter First Name");

            return false;

        } else if (TextUtils.isEmpty(lname)) {
            et_lname.setError("Please enter Last Name");

            return false;
        } else if (TextUtils.isEmpty(username)) {
            et_userName.setError("Please enter username");
            return false;
        } else if (TextUtils.isEmpty(age)) {
            et_age.setError("Please enter age");

            return false;
        } else if (TextUtils.isEmpty(edit_email) && AppUtil.emailValidator(edit_email)) {
            et_email.setError("Please enter valid email format");
            return false;
        } else {

            submitDataToFirebase();
        }
        return true;
    }


    private void submitDataToFirebase() {
//        progress_updateUser.setVisibility(View.VISIBLE);
        Bean_UserInfo userInfo = new Bean_UserInfo();
        userInfo.setAge(Integer.parseInt(age));
        userInfo.setEmail(TextUtils.isEmpty(email) ? "" : email);
        userInfo.setUsername(username);
        userInfo.setFname(fname);
        userInfo.setLname(lname);
        mDatabase.child(user_token).setValue(userInfo);

        // now store the image
//        progress_updateUser.setVisibility(View.GONE);
    }

    private void storeImage() {
        //if there is a file to upload

        // if there is url from facebook or google
        if (image_url != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading Information");
            progressDialog.setCancelable(false);
            progressDialog.show();


            StorageReference riversRef = storageRef.child(user_token).child("Profile");

            riversRef.putFile(Uri.parse(image_url))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            //and displaying a success toast


                            AppUtil.showToast(getApplicationContext(), "Info has been successfully submitted");
                            Preference.writeBoolean(getApplicationContext(), Preference.is_User_Info_saved, true);
                            if (coming_from.equalsIgnoreCase("login")) {
                                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                                intent.putExtra("USER_TOKEN", user_token);
                                startActivity(intent);
                                finish();
                            } else {
                                finishAffinity();

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploading in progress..." + ((int) progress) + "%...");
                        }
                    });


        } else if (arr_images != null && arr_images.size() > 0) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading Information");
            progressDialog.setCancelable(false);
            progressDialog.show();


            int arrSize = arr_images.size();
            for (int i = 0; i < arrSize; i++) {
                final Bean_Upload bean_upload = arr_images.get(i);
                StorageReference riversRef = storageRef.child(user_token).child(arr_images.get(i).getName());

                riversRef.putFile(arr_images.get(i).getImage_uri())
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                //and displaying a success toast

                                arr_images.remove(bean_upload);
                                if (arr_images.size() == 0) {
                                    progressDialog.dismiss();
                                    AppUtil.showToast(getApplicationContext(), "Info has been successfully submitted");


                                    Preference.writeBoolean(getApplicationContext(), Preference.is_User_Info_saved, true);
                                    if (coming_from.equalsIgnoreCase("login")) {
                                        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                                        intent.putExtra("USER_TOKEN", user_token);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        finishAffinity();
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();
                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploading in progress..." + ((int) progress) + "%...");
                            }
                        });


            }

        }
        //if there is not any file
        else {
            AppUtil.showToast(this, "You can add image By editing your profile...");
            Preference.writeBoolean(getApplicationContext(), Preference.is_User_Info_saved, true);
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            intent.putExtra("USER_TOKEN", user_token);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PROFILE_IMAGE_CODE:
                    // Let's read picked image data - its URI
                    pickedImage = data.getData();
                    Bean_Upload bean_upload = new Bean_Upload();
                    bean_upload.setImage_uri(pickedImage);
                    bean_upload.setName("Profile");
                    arr_images.add(bean_upload);
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pickedImage));
                        img_profile.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

//                case COVER_IMAGE_CODE:
//                    // Let's read picked image data - its URI
//                    pickedImage = data.getData();
//                    Bean_Upload bean_upload1 = new Bean_Upload();
//                    bean_upload1.setImage_uri(pickedImage);
//                    bean_upload1.setName("COVER");
//                    arr_images.add(bean_upload1);
//                    try {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pickedImage));
//                        img_cover.setImageBitmap(bitmap);
//                        tv_upload.setVisibility(View.GONE);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    break;

            }


        }
    }

    public void retrive() {

        try {

            mDatabase.child(user_token).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // this is to get the arraylisst
//                GenericTypeIndicator<ArrayList<Item>> t = new GenericTypeIndicator<ArrayList<Item>>() {};
//                ArrayList<Item> yourStringArray = snapshot.getValue(t);
//                Toast.makeText(getContext(),yourStringArray.get(0).getName(),Toast.LENGTH_LONG).show();
                    dataSnapshot.getKey();
                    Bean_UserInfo user = dataSnapshot.getValue(Bean_UserInfo.class);
                    Log.d("DETAILS", "User name: " + user.getEmail() + ", email " + user.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("DETAILS", "Failed to read value.", error.toException());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
