package com.extralarge.fujitsu.xl;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.FCM.tokensave;
import com.extralarge.fujitsu.xl.NewsSection.MainNews;
import com.extralarge.fujitsu.xl.NewsSection.State;
import com.extralarge.fujitsu.xl.ReporterSection.BecomeReporter;
import com.extralarge.fujitsu.xl.ReporterSection.ReporterDashboard;
import com.extralarge.fujitsu.xl.ReporterSection.ReporterLogin;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AbsRuntimePermission {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    android.support.v7.widget.Toolbar toolbar;

    String name;

    UserSessionManager session;
    private static final int REQUEST_PERMISSION = 10;
    int i = 0;
    static boolean f = true;

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if(!prefs.getBoolean("firstTime", false)) {

            SendtokenofNews();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

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

        ImageView msearch = (ImageView) toolbar.findViewById(R.id.searchimage);
        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent searchint = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(searchint);
            }
        });


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
                    xfragmentTransaction.replace(R.id.containerView, new MainNews()).commit();
                    toolbar.setTitle("राष्ट्रीय");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }

                if (menuItem.getItemId() == R.id.nav_item_International) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
                    toolbar.setTitle("International");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }

                if (menuItem.getItemId() == R.id.nav_item_states) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
                    toolbar.setTitle("States");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }
                if (menuItem.getItemId() == R.id.nav_item_business) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
                    toolbar.setTitle("Business");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }

                if (menuItem.getItemId() == R.id.nav_item_city) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new State()).commit();
                    toolbar.setTitle("Cities");
                    toolbar.setTitleTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));

                }


                if (menuItem.getItemId() == R.id.nav_item_becomereporter) {

                    Intent becomereporterint = new Intent(MainActivity.this, BecomeReporter.class);
                    startActivity(becomereporterint);
                }

                if (menuItem.getItemId() == R.id.nav_item_reporterlogin) {

                   // session.createUserLoginSession(name);

                    Intent reporterloginint = new Intent(MainActivity.this, ReporterLogin.class);
//                    reporterloginint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    reporterloginint.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

        requestAppPermissions(new String[]{

                                Manifest.permission.READ_SMS,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        R.string.msg, REQUEST_PERMISSION);

//        if(i == 0){
//
//            Log.d("if00","abcd");
//            SendtokenofNews();
//        }
//
//        Log.d("ival001", String.valueOf(i));



        }




    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void SendtokenofNews() {


        final String token = tokensave.getInstance(MainActivity.this).getDeviceToken();
        final String LOGIN_URL = "http://api.minews.in/slimapp/public/api/readers/";

        String newurl = LOGIN_URL+token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("jaba",response.toString());
                        Log.d("tok00",token);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if(success){

                            }

                            Log.d("ival00", String.valueOf(i));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Log.d("jabadi",motptext);
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {

        };RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }


    }
