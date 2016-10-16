package com.armada.mostafa.omegacompanydemo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pc on 04/10/2016.
 */
public class DrawerTemp extends AppCompatActivity {
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_temp);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
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

        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.menu_internal);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);


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

        Bundle getBundle = this.getIntent().getExtras();;
        final String name = getBundle.getString("username");
        final String code = getBundle.getString("usercode");
        final String userAddress = getBundle.getString("address");
        final String userPhone1 = getBundle.getString("phone1");
        final String userPhone2 = getBundle.getString("phone2");

        ivGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrawerTemp.this,HomeActivity.class);
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
                        Intent i = new Intent(DrawerTemp.this,VacancyInquiryActivity.class);
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
                        Intent in = new Intent(DrawerTemp.this,SalaryEnquiryActivity.class);
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
                        Intent intent = new Intent(DrawerTemp.this,VacancyRequestActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return true;
                    case R.id.item4:
                        Bundle b4 = new Bundle();
                        b4.putString("username",name);
                        b4.putString("usercode",code);
                        b4.putString("address",userAddress);
                        b4.putString("phone1",userPhone1);
                        b4.putString("phone2",userPhone2);
                        Intent intent4 = new Intent(DrawerTemp.this,ViewEmployeeVacationsActivity.class);
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
                getSupportActionBar().show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                getSupportActionBar().hide();
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

}
