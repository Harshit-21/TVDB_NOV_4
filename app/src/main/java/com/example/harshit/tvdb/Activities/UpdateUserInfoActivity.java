package com.example.harshit.tvdb.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harshit.tvdb.Pojo.Bean_UserInfo;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.example.harshit.tvdb.Utils.Preference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout txt_userName, txt_age, txt_fname, txt_lname;
    private EditText et_userName, et_age, et_fname, et_lname;
    private TextView tv_emailId;
    private Button btn_addUserInfo;
    private ImageView image;
    private String user_token = "";
    private String username, age;
    private String email;
    private final static int IMAGE_REQ_CODE = 10930;
    private DatabaseReference mDatabase;
    private Bitmap bitmap;
    private String fname;
    private String lname;
    private Uri pickedImage;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        mDatabase = FirebaseDatabase.getInstance().getReference("USER");

        //creating reference to firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_storage));

        AppUtil.setActionBar(this);

        initViews();
        getDataFromBundle();
        setListners();
    }

    private void getDataFromBundle() {
        user_token = getIntent().getStringExtra("USER_TOKEN");
        email = getIntent().getStringExtra("EMAIL");
        Log.i("USER_TOOKEN", user_token);
        if (email != null) {
            tv_emailId.setText(email);
        }
    }

    private void setListners() {
        btn_addUserInfo.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    private void initViews() {
        btn_addUserInfo = findViewById(R.id.btn_addUserInfo);
        image = findViewById(R.id.image);
        tv_emailId = findViewById(R.id.tv_emailId);
        txt_userName = findViewById(R.id.txt_userName);
        txt_age = findViewById(R.id.txt_age);
        et_age = findViewById(R.id.et_age);
        et_userName = findViewById(R.id.et_userName);

        txt_fname = findViewById(R.id.txt_fname);
        txt_lname = findViewById(R.id.txt_lname);
        et_lname = findViewById(R.id.et_lname);
        et_fname = findViewById(R.id.et_fname);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, IMAGE_REQ_CODE);
                break;

            case R.id.btn_addUserInfo:
                if (getDataOfLayout()) {
                    // here we have to stre boolean in shared prefernce
                    storeImage();



                } else {
                    AppUtil.openNonInternetActivity(UpdateUserInfoActivity.this, getResources().getString(R.string.something_went_wrong));
                    finish();
                }
                break;

        }
    }

    private boolean getDataOfLayout() {
        username = et_userName.getText().toString().trim();
        age = et_age.getText().toString().trim();
        fname = et_fname.getText().toString().trim();
        lname = et_lname.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            txt_userName.setError("Please enter username");
            return false;

        } else if (TextUtils.isEmpty(age)) {
            txt_age.setError("Please enter age");
            return false;
        } else if (TextUtils.isEmpty(fname)) {
            txt_fname.setError("Please enter First Name");
            return false;
        } else if (TextUtils.isEmpty(lname)) {
            txt_lname.setError("Please enter Last Name");
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
        if (pickedImage != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Information");
            progressDialog.show();

            StorageReference riversRef = storageRef.child(user_token);
            riversRef.putFile(pickedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying a success toast
                            AppUtil.showToast(getApplicationContext(), "Info has been successfully submitted");

                            Preference.writeBoolean(getApplicationContext(), Preference.is_User_Info_saved, true);
                            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                            intent.putExtra("USER_TOKEN", user_token);
                            startActivity(intent);
                            finish();

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
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
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

        if (requestCode == IMAGE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            pickedImage = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(pickedImage));
                image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
