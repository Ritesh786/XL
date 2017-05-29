package com.extralarge.fujitsu.xl.ReporterSection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.R;
import com.extralarge.fujitsu.xl.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ReporterLogin extends AppCompatActivity implements View.OnClickListener {

    EditText msendotptext;
    String sendotptxt;

    AppCompatButton msendotpbtn;

    UserSessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter_login2);

        session = new UserSessionManager(getApplicationContext());

   msendotptext = (EditText) findViewById(R.id.mobileotp);

        msendotpbtn = (AppCompatButton) findViewById(R.id.sendotp_btn);
      //  hasPermissions();
       msendotpbtn.setOnClickListener(this);

    }


    @Override
    public void onClick(final View v) {

        sendotp();

        v.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                v.setClickable(true);
            }
        }, 5000);

    }

    private void sendotp(){


        final String KEY_mobile = "mobile";

        sendotptxt = msendotptext.getText().toString().trim();

        if (TextUtils.isEmpty(sendotptxt)) {
            msendotptext.requestFocus();
            msendotptext.setError("This Field Is Mandatory");
        }
        else{

            String url = null;
            String REGISTER_URL = "http://api.minews.in/request_sms.php";

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Log.d("jaba", usernsme);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {

                                    Intent registerintent = new Intent(ReporterLogin.this, Verifyotp.class);
                                    startActivity(registerintent);
                                    finish();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ReporterLogin.this);
                                    builder.setMessage("Registration Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(ReporterLogin.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Log.d("jabadi", usernsme);
                            Toast.makeText(ReporterLogin.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(KEY_mobile, sendotptxt);
                  
                    return params;

                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(ReporterLogin.this);
            requestQueue.add(stringRequest);
        }


    }

}
