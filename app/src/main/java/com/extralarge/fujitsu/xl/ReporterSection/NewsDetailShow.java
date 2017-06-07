package com.extralarge.fujitsu.xl.ReporterSection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.extralarge.fujitsu.xl.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class NewsDetailShow extends AppCompatActivity implements View.OnClickListener {

    TextView mtype,mheadline,mcontent,mcaption;
    ImageView mnewsimmage;
    String  type,headline,content,caption,image;
    URL url;
   Button mgobckbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_show);

        mtype = (TextView) findViewById(R.id.newstype);
        mheadline = (TextView) findViewById(R.id.newsheadline);
        mcontent = (TextView) findViewById(R.id.newscontent);
        mcaption = (TextView) findViewById(R.id.newsimgcaption);
        mnewsimmage = (ImageView) findViewById(R.id.newsimage);
        mgobckbtn = (Button) findViewById(R.id.back_btn);
        mgobckbtn.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        headline = intent.getStringExtra("headline");
        content = intent.getStringExtra("content");
       // caption = intent.getStringExtra("caption");
        image =  intent.getStringExtra("image");

           mtype.setText("News Type:" + type);
        mheadline.setText("News Headline:" +headline);
        mcontent.setText("News Content:"+ content);
     //   mcaption.setText(caption);

        try {
             url = new URL(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(String.valueOf(url)).resize(800, 800).into(mnewsimmage);

    }

    @Override
    public void onClick(View v) {

        NewsDetailShow.this.finish();

    }
}
