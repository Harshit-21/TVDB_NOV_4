package com.example.harshit.tvdb.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harshit.tvdb.Adapters.SliderPagerAdapter;
import com.example.harshit.tvdb.Interfaces.SliderClick;
import com.example.harshit.tvdb.Pojo.Bean_SliderImages;
import com.example.harshit.tvdb.Pojo.Bean_UserInfo;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SliderClick {
    private TextView tv_viewTv, tv_viewMovies, tv_search;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Bean_SliderImages> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    private ImageView user_image;
    private TextView username;
    private TextView tv_userEmail;

    private DatabaseReference mDatabase;
    private String user_token;
    private ProgressBar progress_userInfo;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    final long ONE_MEGABYTE = 1024 * 1024 *5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getDataFromBundle();
        initViews();
        mDatabase = FirebaseDatabase.getInstance().getReference("USER");
         storage = FirebaseStorage.getInstance();
         storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_storage));
        setSupportActionBar(toolbar);
        setDrawer();
        setListners();
        final Runnable r = new Runnable() {
            public void run() {
                setSlider();
            }
        };
        new Handler().postDelayed(r, 100);
    }

    private void getDataFromBundle() {
        user_token = getIntent().getStringExtra("USER_TOKEN");

    }

    private void setSlider() {
        getSliderImages();
        setSliderAdapter();
        addBottomDots(0);
        autoScrollSlider();
    }

    private void getTheUserInfo() {

        if (AppUtil.isNetworkAvailable(this) && user_token != null) {
            progress_userInfo.setVisibility(View.VISIBLE);
            mDatabase.child(user_token).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // this is to get the arraylisst
//                GenericTypeIndicator<ArrayList<Item>> t = new GenericTypeIndicator<ArrayList<Item>>() {};
//                ArrayList<Item> yourStringArray = snapshot.getValue(t);
//                Toast.makeText(getContext(),yourStringArray.get(0).getName(),Toast.LENGTH_LONG).show();
                    Bean_UserInfo user = dataSnapshot.getValue(Bean_UserInfo.class);
                    // here we get the complete info of user
                    setValueOfuser(user);
                    progress_userInfo.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    progress_userInfo.setVisibility(View.GONE);

                    Log.w("DETAILS", "Failed to read value.", error.toException());
                    AppUtil.openNonInternetActivity(DashBoardActivity.this, getResources().getString(R.string.something_went_wrong));
                    finish();
                }
            });
        } else {
            progress_userInfo.setVisibility(View.GONE);
            AppUtil.openNonInternetActivity(DashBoardActivity.this, getResources().getString(R.string.something_went_wrong));
            finish();
        }
    }

    private void setValueOfuser(Bean_UserInfo user) {
        if (user != null) {
            username.setText(user.getFname() + " " + user.getLname());
            tv_userEmail.setText(user.getEmail());
// now we need to download from firebase storage
            //download file as a byte array
            storageRef.child(user_token).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    user_image.setImageBitmap(bitmap);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // getTheuserInfo
        getTheUserInfo();
    }

    private void getSliderImages() {
        slider_image_list = new ArrayList<>();

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

        Integer images_array[] = new Integer[]{R.drawable.breaking_bad, R.drawable.originals, R.drawable.got, R.drawable.ironman};
        String images_title[] = new String[]{"", "", "", ""};


        for (int i = 0; i < 4; i++) {
            Bean_SliderImages bean_sliderImages = new Bean_SliderImages();
            bean_sliderImages.setId(images_array[i]);
            bean_sliderImages.setName(images_title[i]);
            slider_image_list.add(bean_sliderImages);
        }
    }

    private void setSliderAdapter() {


        sliderPagerAdapter = new SliderPagerAdapter(this, slider_image_list, this);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void autoScrollSlider() {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);

    }


    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initViews() {
        vp_slider = findViewById(R.id.vp_slider);
        ll_dots = findViewById(R.id.ll_dots);
        tv_viewTv = findViewById(R.id.tv_viewTv);
        tv_search = findViewById(R.id.tv_search);
        tv_viewMovies = findViewById(R.id.tv_viewMovies);
        tv_userEmail = findViewById(R.id.tv_userEmail);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_dash_board));
        progress_userInfo = findViewById(R.id.progress_userInfo);

        View headerView = navigationView.getHeaderView(0);
        user_image = headerView.findViewById(R.id.user_image);
        username = headerView.findViewById(R.id.tv_userName);


    }

    private void setListners() {
        tv_viewTv.setOnClickListener(this);
        tv_viewMovies.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_viewMovies:
                openMoviesActivity();
                break;
            case R.id.tv_viewTv:
                openTvActivity();
                break;
            case R.id.tv_search:
                openSearchActivity();
                break;
        }

    }

    private void openSearchActivity() {
        Intent search_intent = new Intent(this, SearchActivity.class);
        startActivity(search_intent);
    }


    private void openTvActivity() {
        Intent intent_tv = new Intent(this, TvListActivity.class);
        startActivity(intent_tv);
    }

    private void openMoviesActivity() {
        Intent intent_movies = new Intent(this, MoviesListActivity.class);
        startActivity(intent_movies);
    }

    @Override
    public void onClick(String name, Object object, String tag) {

    }
}
