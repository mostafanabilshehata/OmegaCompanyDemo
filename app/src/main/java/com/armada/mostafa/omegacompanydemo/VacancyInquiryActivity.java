package com.armada.mostafa.omegacompanydemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 02/10/2016.
 */
public class VacancyInquiryActivity extends DrawerTemp implements View.OnClickListener{
    private final String URLVacancy = "http://41.178.89.245:7101/ws/HRVacBusinessPort?wsdl";
    String requestXml = "";
    String responseString = null;
    Spinner spVacancyType;
    TextView vacancyValue,vacancyValueText,vacancyTypeText,tvHeaderText;
    Button submitInquiry;
    List<String> vacationsList = new ArrayList<String>();
    ArrayAdapter<String> adapterVacationsType;
    String empCode;
    ImageView ivVacanyInquiry;
    int vacationCode;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_vacancy_inquiry, null, false);
        drawerLayout.addView(contentView, 0);

        ivVacanyInquiry = (ImageView) findViewById(R.id.ivVacanyInquiry);

//        getWindow().setStatusBarColor(R.drawable.internal_header_bg);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.internal_header_bg_actionbar));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.menu_internal);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        getSupportActionBar().setCustomView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.internal_header_bg));

        spVacancyType = (Spinner)findViewById(R.id.spinnerVacancyType);
        vacancyValue = (TextView) findViewById(R.id.tvVacancyValue);
        vacancyValueText = (TextView)findViewById(R.id.tvVacancy);
        vacancyTypeText = (TextView)findViewById(R.id.tvVacancyType);
        tvHeaderText = (TextView) findViewById(R.id.tvVacancyInquiry);
        submitInquiry = (Button) findViewById(R.id.btnSubmitInquiry);
        submitInquiry.setOnClickListener(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        vacancyValue.setTypeface(custom_font);
        vacancyValueText.setTypeface(custom_font);
        vacancyTypeText.setTypeface(custom_font);
        submitInquiry.setTypeface(custom_font);
        tvHeaderText.setTypeface(custom_font);

        requestXml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <soap:Header/>\n" +
                "    <soap:Body>\n" +
                "        <ns1:vacations xmlns:ns1=\"http://vac.hr/\"/>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        new MyTask().execute();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmitInquiry){
            Bundle getBundle = null;
            getBundle = this.getIntent().getExtras();
            empCode = getBundle.getString("usercode");
            Log.d("**************",""+vacationCode);
            requestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    "    <soap:Header/>\n" +
                    "    <soap:Body>\n" +
                    "        <ns1:vacationBalanace xmlns:ns1=\"http://vac.hr/\">\n" +
                    "            <vacationCode>"+vacationCode+"</vacationCode>\n" +
                    "            <empCode>"+empCode+"</empCode>\n" +
                    "        </ns1:vacationBalanace>\n" +
                    "    </soap:Body>\n" +
                    "</soap:Envelope>";
            new MyTask2().execute();
        }
    }
    public class MyTask extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            HttpPost httpPost = new HttpPost(URLVacancy);
            StringEntity entity;
            try {
                entity = new StringEntity(requestXml, HTTP.UTF_8);
                httpPost.setHeader("Content-Type","application/soap+xml; charset=UTF-8");
                httpPost.setEntity(entity);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(httpPost);
                responseString = EntityUtils.toString(response.getEntity());
                Log.d("request", responseString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onPostExecute(String res) {
            XmlPullParserFactory xmlFactoryObject = null;
            XmlPullParser myparser = null;
            try {
                xmlFactoryObject = XmlPullParserFactory.newInstance();
                myparser = xmlFactoryObject.newPullParser();
                myparser.setInput(new StringReader(responseString));
                int event = myparser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT)
                {
                    String name= null;
                    switch (event){

                        case XmlPullParser.START_TAG:
                            name=myparser.getName();
                            Log.d("name",name);
                            if(name.equals("vacName")) {
                                vacationsList.add(myparser.nextText());
                                Log.d("list size is :",""+vacationsList.size());
                            }
                            break;

                        case XmlPullParser.END_TAG:

                            break;
                    }
                    event = myparser.next();
                }
                String[] spinnerData = new String[vacationsList.size()];
                vacationsList.toArray(spinnerData);
//                Log.d("!@!2!@!@!@!@!@!@",spinnerData[1]);
                adapterVacationsType = new ArrayAdapter<String>(VacancyInquiryActivity.this,
                        android.R.layout.simple_spinner_item, spinnerData);
                adapterVacationsType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spVacancyType.setAdapter(adapterVacationsType);
                spVacancyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        vacationCode = position+1;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class MyTask2 extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            HttpPost httpPost = new HttpPost(URLVacancy);
            StringEntity entity;
            try {
                entity = new StringEntity(requestXml, HTTP.UTF_8);
                httpPost.setHeader("Content-Type","application/soap+xml; charset=UTF-8");
                httpPost.setEntity(entity);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(httpPost);
                responseString = EntityUtils.toString(response.getEntity());
                Log.d("request", responseString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onPostExecute(String res) {
            XmlPullParserFactory xmlFactoryObject = null;
            XmlPullParser myparser = null;
            try {
                xmlFactoryObject = XmlPullParserFactory.newInstance();
                myparser = xmlFactoryObject.newPullParser();
                myparser.setInput(new StringReader(responseString));
                int event = myparser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT)
                {
                    String name= null;
                    switch (event){

                        case XmlPullParser.START_TAG:
                            name=myparser.getName();
                            if(name.equals("return")){
                                vacancyValue.setText(myparser.nextText().replaceAll("\\D+","")+" يوم");
                            }

                            break;

                        case XmlPullParser.END_TAG:

                            break;
                    }
                    event = myparser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
