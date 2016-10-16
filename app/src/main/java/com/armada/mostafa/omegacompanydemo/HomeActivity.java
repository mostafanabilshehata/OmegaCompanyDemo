package com.armada.mostafa.omegacompanydemo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pc on 30/09/2016.
 */
public class HomeActivity extends AppCompatActivity {
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    AlertDialog.Builder alertDialogBuilder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            navigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        View hView =  navigationView.getHeaderView(0);
        ImageView headerIV = (ImageView)hView.findViewById(R.id.ivNavigationdrawerHeader);
        ImageView ivGoToHome = (ImageView)hView.findViewById(R.id.ivGoToHome);
        TextView headerTV = (TextView)hView .findViewById(R.id.tvNavigationDrawerHeader);
        TextView headerTV2 = (TextView)hView.findViewById(R.id.tvNavigationDrawerHeaderLine2);
        headerTV.setTypeface(custom_font);
        headerTV2.setTypeface(custom_font);


        headerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        alertDialogBuilder = new AlertDialog.Builder(this);

        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        //putting navigation view icon to the right of action bar
        ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.menu);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        getSupportActionBar().setCustomView(imageView);

        //putting logout icon to the left of action bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        drawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logout);


        TextView tvUserName = (TextView) findViewById(R.id.tvHomeUserName);
        tvUserName.setTypeface(custom_font);
//        setTitle("نظام إدارة الموارد البشرية          ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#109c56"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#109c56")));


        navigationView.setItemIconTintList(null);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/JF-Flat-regular.ttf");
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mi.setTitle(mNewTitle);
            if(i%2==0) {
                mNewTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#f58913")), 0, mNewTitle.length(), 0);
            }else{
                mNewTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#109c56")), 0, mNewTitle.length(), 0);
            }
        }



//        Bundle getBundle = null;
//        getBundle = this.getIntent().getExtras();
        final String name = (this.getIntent().getStringExtra("username"));
        final String code = (this.getIntent().getStringExtra("usercode"));
        final String userAddress = (this.getIntent().getStringExtra("address"));
        final String userPhone1 = (this.getIntent().getStringExtra("phone1"));
        final String userPhone2 = (this.getIntent().getStringExtra("phone2"));
//        Log.d("name",name);
        tvUserName.setText("مرحبا! "+name);
        ivGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,HomeActivity.class);
                i.putExtra("usercode",code );
                i.putExtra("address", userAddress);
                i.putExtra("phone1", userPhone1);
                i.putExtra("phone2", userPhone2);
                i.putExtra("username", name);
                startActivity(i);
            }
        });


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();


                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.item1:

                        Bundle b = new Bundle();
                        b.putString("usercode",code);
                        b.putString("username",name);
                        b.putString("address",userAddress);
                        b.putString("phone1",userPhone1);
                        b.putString("phone2",userPhone2);
                        Intent i = new Intent(HomeActivity.this,VacancyInquiryActivity.class);
                        i.putExtras(b);
                        startActivity(i);
                        return true;

                    case R.id.item2:
                        Bundle bun = new Bundle();
                        bun.putString("usercode",code);
                        bun.putString("username",name);
                        bun.putString("address",userAddress);
                        bun.putString("phone1",userPhone1);
                        bun.putString("phone2",userPhone2);
                        Intent in = new Intent(HomeActivity.this,SalaryEnquiryActivity.class);
                        in.putExtras(bun);
                        startActivity(in);
                        return true;
                    case R.id.item3:
                        Bundle bundle = new Bundle();
                        bundle.putString("username",name);
                        bundle.putString("usercode",code);
                        bundle.putString("address",userAddress);
                        bundle.putString("phone1",userPhone1);
                        bundle.putString("phone2",userPhone2);
                        Intent intent = new Intent(HomeActivity.this,VacancyRequestActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return true;
                    case R.id.item4:
                        Bundle b4 = new Bundle();
                        Intent intent4 = new Intent(HomeActivity.this,ViewEmployeeVacationsActivity.class);
                        b4.putString("username",name);
                        b4.putString("usercode",code);
                        b4.putString("address",userAddress);
                        b4.putString("phone1",userPhone1);
                        b4.putString("phone2",userPhone2);
                        intent4.putExtras(b4);
                        startActivity(intent4);
                        return true;


                }
                return false;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                getSupportActionBar().hide();


                drawerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
//        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            alertDialogBuilder.setMessage("هل تريد تسحيل الخروح !");
            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
