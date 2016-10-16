package com.armada.mostafa.omegacompanydemo;

import org.apache.http.*;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends  AppCompatActivity implements View.OnClickListener{
    private final String URL = "http://41.178.89.245:7101/ws/LoginBusinessPort?wsdl";
    EditText empCode,password;
    TextView forgetPassword;
    String employeeCode,employeePassword;
    String requestXml = "";
    String responseString = null;
    protected String userCode="",address="",phone1="",phone2="",userName="";
    AlertDialog.Builder alertDialogBuilder;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empCode = (EditText)findViewById(R.id.etPersonalCode);
        password = (EditText) findViewById(R.id.etPassword);
        Button login = (Button) findViewById(R.id.btnLogin);
        alertDialogBuilder = new AlertDialog.Builder(this);
        progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleSmall);
        empCode.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(empCode, InputMethodManager.SHOW_IMPLICIT);
        login.setOnClickListener(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        empCode.setTypeface(custom_font);
        password.setTypeface(custom_font);
        empCode.setTextColor(Color.parseColor("#000000"));
        password.setTextColor(Color.parseColor("#000000"));
        login.setTypeface(custom_font);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnLogin){
            employeeCode = empCode.getText().toString();
            employeePassword = password.getText().toString();
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(employeeCode==null){
                alertDialogBuilder.setMessage("برجاء إدخال الكود الخاص بك");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        empCode.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(empCode, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(employeeCode.equals("")){
                alertDialogBuilder.setMessage("برجاء إدخال الكود الخاص بك");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        empCode.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(empCode, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(employeePassword==null){
                alertDialogBuilder.setMessage("برجاء إدخال رمز المرور الخاص بك");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        password.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(employeePassword.equals("")){
                alertDialogBuilder.setMessage("برجاء إدخال رمز المرور الخاص بك");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        password.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(netInfo != null && netInfo.isConnected()){
                requestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        "    <soap:Header/>\n" +
                        "    <soap:Body>\n" +
                        "        <ns1:loginEmp xmlns:ns1=\"http://login/\">\n" +
                        "            <empCode>" + employeeCode + "</empCode>\n" +
                        "            <password>" + employeePassword + "</password>\n" +
                        "        </ns1:loginEmp>\n" +
                        "    </soap:Body>\n" +
                        "</soap:Envelope>";
                Log.d("ANA ABL EL TASK","hnaaaaaaaaaaaaaaa");
                new MyTask().execute();
            }else {
                alertDialogBuilder.setMessage("يوجد مشاكل فالإتصال بالإنترنت");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        password.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
    public class MyTask extends AsyncTask<Void, Void, String> {
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
                if(response.getStatusLine().getStatusCode()==HttpsURLConnection.HTTP_GATEWAY_TIMEOUT){
                    alertDialogBuilder.setMessage("يوجد مشكله في السيرفر");
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            new MyTask().execute();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
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
                            if(name.equals("ns0:loginEmpResponse")){
                                myparser.next();
                                name = myparser.getName();
                                if (!name.equals("return")){
                                    alertDialogBuilder.setMessage("هناك خطأ في الكود أو كلمة المرور");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            empCode.setText("");
                                            password.setText("");
                                            empCode.requestFocus();
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(empCode, InputMethodManager.SHOW_IMPLICIT);
                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    break;
                                }
                            }
                            else if(name.equals("return")) {
                            myparser.next();
                            name = myparser.getName();
                            }
                            else if (name.equals("address")) {
                                address = myparser.nextText();
                            }else if (name.equals("userCode")) {
                                userCode = myparser.nextText();
                            }else if (name.equals("phone1")) {
                                phone1 = myparser.nextText();
                            }else if (name.equals("phone2")) {
                                phone2 = myparser.nextText();
                            }else if (name.equals("userName")) {
                                userName = myparser.nextText();
                                Bundle b = new Bundle();
                                b.putString("address", address);
                                b.putString("phone1", phone1);
                                b.putString("phone2", phone2);
                                b.putString("username", userName);
                                b.putString("usercode", userCode);
                                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                i.putExtra("usercode", userCode);
                                i.putExtra("address", address);
                                i.putExtra("phone1", phone1);
                                i.putExtra("phone2", phone2);
                                i.putExtra("username", userName);
                                i.putExtras(b);
                                startActivity(i);
                                Log.d("Username", userName);
                                break;
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
