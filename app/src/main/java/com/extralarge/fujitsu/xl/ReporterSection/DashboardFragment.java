package com.extralarge.fujitsu.xl.ReporterSection;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.extralarge.fujitsu.xl.MainActivity;
import com.extralarge.fujitsu.xl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_OK;
import static com.extralarge.fujitsu.xl.R.id.imageView;
import static com.extralarge.fujitsu.xl.R.id.verifyotp_btn;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

     EditText  mnewsheadline,mnewscontent,mnewsimagecaption;
     Spinner mnewstype;
     Button mchooseimagebtn,muploadnewsbtn;
    ImageView mnewsimage;

    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;
    Uri selectedImage;

    public static final String KEY_HHEADLINE = "headline";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CAPTION = "caption";
  //  public static final String UPLOAD_URL = "http://jigsawme.esy.es/picUpload/upload.php";


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mnewsheadline = (EditText) view.findViewById(R.id.news_heasdline);
        mnewscontent = (EditText) view.findViewById(R.id.news_content);
        mnewsimagecaption = (EditText) view.findViewById(R.id.news_caption);
        mnewstype = (Spinner) view.findViewById(R.id.news_type);
        mnewsimage = (ImageView) view.findViewById(R.id.news_Image);

        mchooseimagebtn = (Button) view.findViewById(R.id.chooseimage_btn);
        muploadnewsbtn = (Button) view.findViewById(R.id.uploadnews_btn);

        mchooseimagebtn.setOnClickListener(this);
        muploadnewsbtn.setOnClickListener(this);

        return view;
    }

    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//       startActivityForResult(intent,PICK_IMAGE_REQUEST);

try {
    if (android.os.Build.VERSION.SDK_INT >= 23) {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    } else {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST);

    }
}catch (Exception e){

    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
}

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
      //  Context context = getContext();
String str = "chala";
        selectedImage = data.getData();

        if (requestCode >= PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
//                mnewsimage.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
           Log.d("try1",str);
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mnewsimage.setImageURI(selectedImage);
        }


           try {
               Log.d("try2",str);
               bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
               e.printStackTrace();
           }}
        catch (Exception e){

            Toast.makeText(getContext(),"On Activity "+e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(){
        final String headline = mnewsheadline.getText().toString().trim();
        final String content = mnewscontent.getText().toString().trim();
        final String type = mnewstype.getSelectedItem().toString();
        final String caption = mnewsimagecaption.getText().toString().trim();
        final String image = getStringImage(bitmap);


        String url = null;
        String REGISTER_URL = "http://midigital.in/excel/picUpload/upload.php";

        REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
        try {
            URL sourceUrl = new URL(REGISTER_URL);
            url = sourceUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Uploading...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Log.d("jaba", usernsme);
//                        try {
//                            JSONObject jsonresponse = new JSONObject(response);
//                            boolean success = jsonresponse.getBoolean("success");
//
//                            if (success) {
//
//                                Intent registerintent = new Intent(getContext(), Verifyotp.class);
//                                startActivity(registerintent);
//
//                            } else {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(ReporterLogin.this);
//                                builder.setMessage("Registration Failed")
//                                        .setNegativeButton("Retry", null)
//                                        .create()
//                                        .show();
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        Log.d("jabadi", headline);
                        loading.dismiss();
                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Log.d("dabadi", headline);
                        loading.dismiss();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(KEY_HHEADLINE,headline);
                params.put(KEY_TYPE,type);
                params.put(KEY_CONTENT,content);
                params.put(KEY_IMAGE,image);
                params.put(KEY_CAPTION,caption);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);





//        class UploadImage extends AsyncTask<Void,Void,String> {
//            ProgressDialog loading;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(),"Please wait...","Your News Is Uploading",false,false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Log.d("upload",s);
//                Toast.makeText(getContext(),s, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                RequestHandler rh = new RequestHandler();
//                HashMap<String,String> param = new HashMap<String,String>();
//                param.put(KEY_HHEADLINE,headline);
//                param.put(KEY_TYPE,type);
//                param.put(KEY_CONTENT,content);
//                param.put(KEY_IMAGE,image);
//                param.put(KEY_CAPTION,caption);
//                String result = rh.sendPostRequest(UPLOAD_URL, param);
//                return result;
//            }
//        }
//        UploadImage u = new UploadImage();
//        u.execute();
    }

    @Override
    public void onDestroy() {
        String btr = "hogaya";

        Log.d("destr",btr);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if(v == mchooseimagebtn){
            showFileChooser();
        }
        if(v == muploadnewsbtn){
            uploadImage();
        }

    }
}
