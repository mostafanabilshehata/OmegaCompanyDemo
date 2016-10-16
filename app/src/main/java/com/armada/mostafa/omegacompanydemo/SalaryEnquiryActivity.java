package com.armada.mostafa.omegacompanydemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

/**
 * Created by pc on 03/10/2016.
 */
public class SalaryEnquiryActivity extends DrawerTemp implements View.OnClickListener{
    private final String URL = "http://41.178.89.245:7101/ws/HRSalaryBusinessPort?wsdl";
    String requestXml = "";
    String responseString = null;
    DatePicker datePicker;
    EditText empCode,etSalaryInquiryEmpCode;
    TextView salaryValue,tvDate,tvEmpCode,tvSalary,tvHeaderText;
    Button submitSalaryEnquiry;
    int year,day;
    String month;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_salary_enquiry, null, false);
        drawerLayout.addView(contentView, 0);
        empCode = (EditText)findViewById(R.id.etSalaryInquiryEmpCode);
        etSalaryInquiryEmpCode = (EditText)findViewById(R.id.etSalaryInquiryEmpCode);
        salaryValue = (TextView) findViewById(R.id.tvSalaryInquiryValue) ;
        tvDate = (TextView)findViewById(R.id.tvSalaryInquiryDate);
        tvEmpCode = (TextView)findViewById(R.id.tvSalaryInquiryEmpCode);
        tvSalary = (TextView)findViewById(R.id.tvSalaryInquiry);
        tvHeaderText = (TextView)findViewById(R.id.tvVacancyRequest);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        empCode.setTypeface(custom_font);
        salaryValue.setTypeface(custom_font);
        tvDate.setTypeface(custom_font);
        tvEmpCode.setTypeface(custom_font);
        tvSalary.setTypeface(custom_font);
        tvHeaderText.setTypeface(custom_font);
        etSalaryInquiryEmpCode.setTypeface(custom_font);

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


        submitSalaryEnquiry = (Button)findViewById(R.id.btnSubmitSalaryInquiry);
        submitSalaryEnquiry.setOnClickListener(this);
        submitSalaryEnquiry.setTypeface(custom_font);

        datePicker = (DatePicker)findViewById(R.id.datePickerSalary);

        empCode.setText(this.getIntent().getExtras().getString("usercode"));

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnSubmitSalaryInquiry){
            year = datePicker.getYear();
            month = String.valueOf(datePicker.getMonth()+1);
            Log.d("MONTH",""+month);
            if(!month.equals("10")&&!month.equals("11")&&!month.equals("12")){
                Log.d("MONTH",""+month);
                month = "0"+month;
            }
            day = datePicker.getDayOfMonth();

            requestXml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    "    <soap:Header/>\n" +
                    "    <soap:Body>\n" +
                    "        <ns1:salary xmlns:ns1=\"http://salary.hr/\">\n" +
                    "            <date>"+month+"/"+year+"</date>\n" +
                    "            <empCode>"+empCode.getText().toString()+"</empCode>\n" +
                    "        </ns1:salary>\n" +
                    "    </soap:Body>\n" +
                    "</soap:Envelope>";
            new GetSalaryTask().execute();
        }
    }
    public class GetSalaryTask extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            HttpPost httpPost = new HttpPost(URL);
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
                            if(name.equals("netSal")) {
                                salaryValue.setText(myparser.nextText().replaceAll("\\D+","")+"  جنيه  ");
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
