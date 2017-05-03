package com.extralarge.fujitsu.xl.ReporterSection;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BecomeReporter extends AppCompatActivity implements View.OnClickListener {

        EditText musertype, musername, mpassword, mname, memail, mmobile, mcity, marea, mstate, mlandmark, mpincode;
        Button mbtnregister;

        String   password="123456789", name, email, mobile, city, area, state, landmark, pincode;
        String usertype= "individual";
        String  usernsme;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.becomereporter);

//        musertype = (EditText) findViewById(R.id.reg_usertype);
        musername = (EditText) findViewById(R.id.reg_username);
//        mpassword = (EditText) findViewById(R.id.reg_password);
        mname = (EditText) findViewById(R.id.reg_name);
        memail = (EditText) findViewById(R.id.reg_email);
        // mmobile = (EditText) findViewById(R.id.reg_mobile);
        mcity = (EditText) findViewById(R.id.reg_city);
        marea = (EditText) findViewById(R.id.reg_area);
        mstate = (EditText) findViewById(R.id.reg_state);
        mlandmark = (EditText) findViewById(R.id.reg_landmark);
        mpincode = (EditText) findViewById(R.id.reg_pincode);


        mbtnregister = (Button) findViewById(R.id.btn_Register);
        mbtnregister.setOnClickListener(this);


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

//        usertype = musertype.getText().toString().trim();
        usernsme = musername.getText().toString().trim();
//        password = mpassword.getText().toString().trim();
        name = mname.getText().toString().trim();
        email = memail.getText().toString().trim();
        //    mobile = mmobile.getText().toString().trim();
        city = mcity.getText().toString().trim();
        area = marea.getText().toString().trim();
        state = mstate.getText().toString().trim();
        landmark = mlandmark.getText().toString().trim();
        pincode = mpincode.getText().toString().trim();



        if (TextUtils.isEmpty( usertype)) {
        musertype.requestFocus();
        musertype.setError("This Field Is Mandatory");
        }
        else if (TextUtils.isEmpty(usernsme)) {
        musername.requestFocus();
        musername.setError("This Field Is Mandatory");
        }else if(usernsme.length()<10 || isValidPhoneNumber(usernsme)==false){

        musername.requestFocus();
        musername.setError("PLease Fill Correct Number");
        }
        else if (TextUtils.isEmpty(password)) {
        mpassword.requestFocus();
        mpassword.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(name)) {
        mname.requestFocus();
        mname.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(city)) {
        mcity.requestFocus();
        mcity.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(area)) {
        marea.requestFocus();
        marea.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(state)) {
        mstate.requestFocus();
        mstate.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(landmark)) {
        mlandmark.requestFocus();
        mlandmark.setError("This Field Is Mandatory");
        } else if (TextUtils.isEmpty(pincode)) {
        mpincode.requestFocus();
        mpincode.setError("This Field Is Mandatory");
        }
        else if (pincode.length()<6) {
        mpincode.requestFocus();
        mpincode.setError("Please Fill Correct Pincode");
        }

        else {
        String url = null;
        String REGISTER_URL = "http://kabadiwalatest.azurewebsites.net/api/member/register?username=" + usernsme + "&password=" + password + "&area=" + area + "&city=" + city + "&landmark=" + landmark + "&state=" + state + "&pincode=" + pincode + "&name=" + name + "&mobileno=" + usernsme + "&email=" + email + "&usertype=" +  usertype + "";

        REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
        try {
        URL sourceUrl = new URL(REGISTER_URL);
        url = sourceUrl.toString();
        } catch (MalformedURLException e) {
        e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
@Override
public void onResponse(String response) {
        Log.d("jaba", usernsme);
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
        Log.d("jabadi", usernsme);
        Toast.makeText(BecomeReporter.this, error.toString(), Toast.LENGTH_LONG).show();

        }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(BecomeReporter.this);
        requestQueue.add(stringRequest);
        }
        }



        }