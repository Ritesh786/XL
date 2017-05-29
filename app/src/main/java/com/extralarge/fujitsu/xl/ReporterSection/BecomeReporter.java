package com.extralarge.fujitsu.xl.ReporterSection;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.AbsRuntimePermission;
import com.extralarge.fujitsu.xl.R;
import com.extralarge.fujitsu.xl.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BecomeReporter extends AbsRuntimePermission implements View.OnClickListener {

        EditText  mpassword, mname, memail, mmobile, mpincode;
        Button mbtnregister;

        String   password="123456789", name, email, mobile, city, district, state, landmark, pincode,gender;
        String usertype= "individual";
        String  usernsme;

        AutoCompleteTextView   mcity, mdistrict, mstate,mgender;

        AlertDialog.Builder builder;
        ArrayAdapter<CharSequence> adapter;

        private static final int REQUEST_PERMISSION = 10;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.becomereporter);


        requestAppPermissions(new String[]{

                        Manifest.permission.READ_SMS},
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                                Manifest.permission.WRITE_CONTACTS},
                R.string.msg, REQUEST_PERMISSION);


//        musertype = (EditText) findViewById(R.id.reg_usertype);
        mmobile = (EditText) findViewById(R.id.reg_mobile);
//        mpassword = (EditText) findViewById(R.id.reg_password);
        mname = (EditText) findViewById(R.id.reg_name);
        memail = (EditText) findViewById(R.id.reg_email);
        // mmobile = (EditText) findViewById(R.id.reg_mobile);
        mcity = (AutoCompleteTextView) findViewById(R.id.reg_city);
        mdistrict = (AutoCompleteTextView) findViewById(R.id.reg_area);
        mstate = (AutoCompleteTextView) findViewById(R.id.reg_state);
        mpincode = (EditText) findViewById(R.id.reg_pincode);
        mgender =(AutoCompleteTextView) findViewById(R.id.reg_gender);

        builder = new AlertDialog.Builder(getApplicationContext());

        String[] countries = getResources().getStringArray(R.array.CityNames);
        String[] genders = getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countries);


        ArrayAdapter<String> gendadapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, genders);

        mstate.setAdapter(adapter);
        mcity.setAdapter(adapter);
        mgender.setAdapter(gendadapter);

        mbtnregister = (Button) findViewById(R.id.btn_Register);
        mbtnregister.setOnClickListener(this);


        }

        @Override
        public void onPermissionsGranted(int requestCode) {

                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();

        }

        public boolean isValidPhoneNumber(String phoneNumber) {

        String expression ="^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
        CharSequence inputString = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
        return true;
        }
        else{
        return false;
        }

        }



@Override
public void onClick(View v) {

        registerUser();
        }

private void registerUser() {


        final String KEY_mobile = "mobile";
        final String KEY_name = "name";
        final String KEY_email = "email";
        final String KEY_gender = "gender";
        final String KEY_state = "state";
        final String KEY_city = "city";
        final String KEY_district = "district";
        final String KEY_pincode = "pincode";



//        usertype = musertype.getText().toString().trim();
        mobile = mmobile.getText().toString().trim();
//        password = mpassword.getText().toString().trim();
        name = mname.getText().toString().trim();
        email = memail.getText().toString().trim();
        gender = mgender.getText().toString().trim();
        state = mstate.getText().toString().trim();
        //    mobile = mmobile.getText().toString().trim();
        city = mcity.getText().toString().trim();
        district = mdistrict.getText().toString().trim();
        pincode = mpincode.getText().toString().trim();




         if (TextUtils.isEmpty(password)) {
        mpassword.requestFocus();
        mpassword.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(name)) {
        mname.requestFocus();
        mname.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(city)) {
        mcity.requestFocus();
        mcity.setError("This Field Is Mandatory");
        }
         else if (TextUtils.isEmpty(state)) {
        mstate.requestFocus();
        mstate.setError("This Field Is Mandatory");
        }
         else if (TextUtils.isEmpty(pincode)) {
        mpincode.requestFocus();
        mpincode.setError("This Field Is Mandatory");
        }
        else if (pincode.length()<6) {
        mpincode.requestFocus();
        mpincode.setError("Please Fill Correct Pincode");
        }

        else {
        String url = null;
             String REGISTER_URL = "http://api.minews.in/user_reg.php";

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

        Intent registerintent = new Intent(BecomeReporter.this, ReporterLogin.class);
        startActivity(registerintent);
        } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(BecomeReporter.this);
        builder.setMessage("Registration Failed")
        .setNegativeButton("Retry", null)
        .create()
        .show();

        }

        } catch (JSONException e) {
        e.printStackTrace();
        }

        Toast.makeText(BecomeReporter.this, response.toString(), Toast.LENGTH_LONG).show();
        }
        },
        new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
       // Log.d("jabadi", usernsme);
        Toast.makeText(BecomeReporter.this, error.toString(), Toast.LENGTH_LONG).show();

        }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request


                params.put(KEY_mobile, mobile);
                params.put(KEY_name, name);
                params.put(KEY_email, email);
                params.put(KEY_gender,gender);
                params.put(KEY_state, state);
                params.put(KEY_city, city);
                params.put(KEY_district, district);
                params.put(KEY_pincode, pincode);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(BecomeReporter.this);
        requestQueue.add(stringRequest);
        }
        }



        }