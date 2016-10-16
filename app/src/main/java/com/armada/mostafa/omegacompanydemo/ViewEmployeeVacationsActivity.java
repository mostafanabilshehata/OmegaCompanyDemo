package com.armada.mostafa.omegacompanydemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
 * Created by pc on 04/10/2016.
 */
public class ViewEmployeeVacationsActivity extends DrawerTemp {
    TableLayout t1;
    private final String URL = "http://41.178.89.245:7101/ws/HRVacBusinessPort?wsdl";
    String requestXml = "";
    String responseString = null;
    String empCode="";
    int count =0;
    TextView tvVacancyRequest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_view_employee_vacations, null, false);
        drawerLayout.addView(contentView, 0);

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

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        tvVacancyRequest = (TextView)findViewById(R.id.tvVacancyRequest);
        tvVacancyRequest.setTypeface(custom_font);


        t1 = (TableLayout) findViewById(R.id.tableResults);
        TableRow trHeader = new TableRow(this);
        trHeader.setId(10);
        trHeader.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView labelStartDate = new TextView(this);
        labelStartDate.setId(21);// define id that must be unique
        labelStartDate.setText("تاريخ البداية     "); // set the text for the header
        labelStartDate.setTextSize(25);
        labelStartDate.setTextColor(Color.parseColor("#f58913")); // set the color
        labelStartDate.setPadding(3, 5, 3, 5); // set the padding (if required)
        labelStartDate.setGravity(Gravity.LEFT);
        labelStartDate.setTypeface(custom_font);
        trHeader.addView(labelStartDate); // add the column to the table row here


        TextView labelPeriod = new TextView(this);
        labelPeriod.setId(20);
        labelPeriod.setText("مدة الاجازة  ");
        labelPeriod.setTextSize(25);
        labelPeriod.setTextColor(Color.parseColor("#f58913"));
        labelPeriod.setPadding(3, 5, 3, 5);
        labelPeriod.setGravity(Gravity.LEFT);
        labelPeriod.setTypeface(custom_font);
        trHeader.addView(labelPeriod);// add the column to the table row here



        TextView labelVacancyType = new TextView(this);
        labelVacancyType.setId(22);// define id that must be unique
        labelVacancyType.setText("     نوع الاجازة   "); // set the text for the header
        labelVacancyType.setTextSize(25);
        labelVacancyType.setTextColor(Color.parseColor("#f58913")); // set the color
        labelVacancyType.setPadding(3, 5, 3, 5); // set the padding (if required)
        labelStartDate.setGravity(Gravity.LEFT);
        labelStartDate.setTypeface(custom_font);
        trHeader.addView(labelVacancyType); // add the column to the table row here

        trHeader.setBackgroundResource(R.drawable.row_border);

        t1.addView(trHeader, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        empCode = this.getIntent().getExtras().getString("usercode");
        Log.d("EMPCODE",empCode);
        requestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <soap:Header/>\n" +
                "    <soap:Body>\n" +
                "        <ns1:viewEmpVac xmlns:ns1=\"http://vac.hr/\">\n" +
                "            <empCode>"+empCode+"</empCode>\n" +
                "        </ns1:viewEmpVac>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        new GetSVacations().execute();


    }

    public class GetSVacations extends AsyncTask<Void, Void, String> {
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
                String date="" ,period="" ,vacName="";
                while (event != XmlPullParser.END_DOCUMENT)
                {
                    String name= null;
                    switch (event){
                        case XmlPullParser.START_TAG:
                            name=myparser.getName();
                            Log.d("name",name);
                            if(name.equals("return")) {
                                Log.d("name1111", name);
                                myparser.next();
                            }
                                name = myparser.getName();
                                if (name.equals("periodDD")) {
                                    Log.d("name2222",name);
                                    date = (myparser.nextText());
                                    myparser.next();
                                    myparser.next();
//                                    name = myparser.getName();
                                } if( name.equals ("vacDate")) {
                                    Log.d("name3333",name);
                                    period = (myparser.nextText());
                                    myparser.next();
                                    name = myparser.getName();
                                }  if (name.equals("vacationName")) {
                                    Log.d("name4444",name);
                                    vacName = (myparser.nextText());
                            handleDisplayTableData(date,period,vacName);
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void handleDisplayTableData(String vacationDate , String vacationPeriod, String vacationType){
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        TableRow tr = new TableRow(ViewEmployeeVacationsActivity.this);
        tr.setId(100 + count);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView periodDD = new TextView(ViewEmployeeVacationsActivity.this);
        periodDD.setText(vacationPeriod);
        periodDD.setId(200 + count);
        periodDD.setTextSize(20);
        periodDD.setGravity(Gravity.RIGHT);
        periodDD.setTypeface(custom_font);
        periodDD.setTextColor(Color.parseColor("#000000")); // set the color
        tr.addView(periodDD);

        TextView vacDate = new TextView(ViewEmployeeVacationsActivity.this);
        vacDate.setText(vacationDate);
        vacDate.setId(200 + count);
        vacDate.setTextSize(20);
        vacDate.setTextColor(Color.parseColor("#000000")); // set the color
        vacDate.setGravity(Gravity.CENTER);
        vacDate.setTypeface(custom_font);
        tr.addView(vacDate);



        TextView vacationName = new TextView(ViewEmployeeVacationsActivity.this);
        vacationName.setText(vacationType);
        vacationName.setId(200 + count);
        vacationName.setTextSize(20);
        vacationName.setTextColor(Color.parseColor("#000000")); // set the color
        vacationName.setGravity(Gravity.CENTER);
        vacationName.setTypeface(custom_font);
        tr.addView(vacationName);
        tr.setBackgroundResource(R.drawable.row_border);

        if ((count&1) == 0) {
            tr.setBackgroundColor(Color.parseColor("#f5d8b8"));
        }
        else{
            tr.setBackgroundColor(Color.parseColor("#ececec"));
        }

        t1.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        count++;
    }
}
