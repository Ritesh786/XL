package com.extralarge.fujitsu.xl;

import android.Manifest;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.extralarge.fujitsu.xl.ReporterSection.BecomeReporter;
import com.extralarge.fujitsu.xl.ReporterSection.ReporterDashboard;
import com.extralarge.fujitsu.xl.ReporterSection.ReporterLogin;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    android.support.v7.widget.Toolbar toolbar;

    String name;

    UserSessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        /**
         * Setup click events on the Navigation View Items.
         */

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("EXCEL");
        toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

        Intent intent = getIntent();
         name = intent.getStringExtra("session");

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_item_home) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                    toolbar.setTitle("EXCEL");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                }

                if (menuItem.getItemId() == R.id.nav_item_national) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();
                    toolbar.setTitle("राष्ट्रीय");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }

                if (menuItem.getItemId() == R.id.nav_item_International) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();
                    toolbar.setTitle("International");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }

                if (menuItem.getItemId() == R.id.nav_item_states) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();
                    toolbar.setTitle("States");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }
                if (menuItem.getItemId() == R.id.nav_item_business) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();
                    toolbar.setTitle("Business");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }

                if (menuItem.getItemId() == R.id.nav_item_city) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new PrimaryFragment()).commit();
                    toolbar.setTitle("Cities");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }


                if (menuItem.getItemId() == R.id.nav_item_becomereporter) {

                    Intent becomereporterint = new Intent(MainActivity.this, BecomeReporter.class);
                    startActivity(becomereporterint);
                }

                if (menuItem.getItemId() == R.id.nav_item_reporterlogin) {

                    session.createUserLoginSession(name);

                    Intent reporterloginint = new Intent(MainActivity.this, ReporterLogin.class);
                    reporterloginint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    reporterloginint.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(reporterloginint);
                }

                if (menuItem.getItemId() == R.id.nav_item_uploadnews) {

                    Intent dashboardintent = new Intent(MainActivity.this, ReporterDashboard.class);
                    startActivity(dashboardintent);
                    finish();
                }

                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }





    }
