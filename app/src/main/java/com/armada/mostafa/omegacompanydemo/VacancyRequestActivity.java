package com.armada.mostafa.omegacompanydemo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pc on 02/10/2016.
 */
public class VacancyRequestActivity extends DrawerTemp implements View.OnClickListener{

    private final String URLVacancy = "http://41.178.89.245:7101/ws/HRVacBusinessPort?wsdl";
    String requestVacationsXml = "";
    String requestEmployeeXml ="";
    String responseString = null;
    Spinner spVacancyType,spEmployees ;
    EditText vacancyDaysNumber,phoneNumber,phoneCell,address,alterEmpCode;
    TextView tvHeaderText,tvVacansyType,tvStartDate,tvNumberOfDays,tvPhone1,tvPhone2,tvAddress,tvAlterEmpCode,tvSpinnerCustom;
    Button submitRequest;
    List<String> vacationsList = new ArrayList<String>();
    ArrayAdapter<String> adapterVacationsType;
    ArrayAdapter<String> adapterEmployeeType;
    int vacationCode;
    DatePicker datePicker;
    Calendar calendar;
    int year,month,day;
    String date ,username,empCode,alterEmployeeCode="",alterEmpName="";
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_request_vacancy, null, false);
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

        vacancyDaysNumber = (EditText)findViewById(R.id.etNumberOfVacationDays);
        phoneNumber  = (EditText)findViewById(R.id.etPhoneNumber);
        phoneCell  = (EditText)findViewById(R.id.etPhoneCell);
        address  = (EditText)findViewById(R.id.etAddress);
//        alterEmpCode  = (EditText)findViewById(R.id.etAlterEmpCode);

        tvHeaderText =(TextView)findViewById(R.id.tvVacancyRequest);
        tvVacansyType = (TextView)findViewById(R.id.tvVacancyTypeRequest);
        tvStartDate = (TextView)findViewById(R.id.textView3);
        tvNumberOfDays = (TextView)findViewById(R.id.textView4);
        tvPhone1 = (TextView)findViewById(R.id.textView5);
        tvPhone2 = (TextView)findViewById(R.id.textView6);
        tvAddress = (TextView)findViewById(R.id.textView7);
        tvAlterEmpCode = (TextView)findViewById(R.id.textView8);
        tvSpinnerCustom = (TextView)findViewById(R.id.tvSpinnerCustom);


        alertDialogBuilder = new AlertDialog.Builder(this);

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        calendar = Calendar.getInstance();

        submitRequest = (Button) findViewById(R.id.btnSubmitRequest);
        submitRequest.setOnClickListener(this);


        Bundle getBundle = null;
        getBundle = this.getIntent().getExtras();
        address.setText(this.getIntent().getExtras().getString("address"));
        phoneNumber.setText(this.getIntent().getExtras().getString("phone1"));
        phoneCell.setText(this.getIntent().getExtras().getString("phone2"));
        username = this.getIntent().getExtras().getString("username");
        empCode = this.getIntent().getExtras().getString("usercode");

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/JF-Flat-regular.ttf");
        address.setTypeface(custom_font);
        phoneNumber.setTypeface(custom_font);
        phoneCell.setTypeface(custom_font);
        tvHeaderText.setTypeface(custom_font);
        tvVacansyType.setTypeface(custom_font);
        tvStartDate.setTypeface(custom_font);
        tvNumberOfDays.setTypeface(custom_font);
        tvPhone1.setTypeface(custom_font);
        tvPhone2.setTypeface(custom_font);
        tvAddress.setTypeface(custom_font);
        tvAlterEmpCode.setTypeface(custom_font);
        submitRequest.setTypeface(custom_font);
//        tvSpinnerCustom.setTypeface(custom_font);


        spVacancyType = (Spinner)findViewById(R.id.spinnerVacancyTypeRequest);
        requestVacationsXml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <soap:Header/>\n" +
                "    <soap:Body>\n" +
                "        <ns1:vacations xmlns:ns1=\"http://vac.hr/\"/>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        new GetVacationListTask().execute();

        spEmployees = (Spinner)findViewById(R.id.spinnerAlterEmpCode);
        requestEmployeeXml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <soap:Header/>\n" +
                "    <soap:Body>\n" +
                "        <ns1:employees xmlns:ns1=\"http://vac.hr/\"/>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        new GetEmployeeListTask().execute();
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
           date = new StringBuilder().append(year).append("/")
                    .append(month).append("/").append(day).toString();


        }
    };


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmitRequest){
            int vacCode = vacationCode;
            final String numberOfVacDays = vacancyDaysNumber.getText().toString();
            if(numberOfVacDays==null){
                alertDialogBuilder.setMessage("برجاء إدخال عدد أيام الأجازة");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        vacancyDaysNumber.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(vacancyDaysNumber, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(numberOfVacDays.equals("")){
                alertDialogBuilder.setMessage("برجاء إدخال عدد أيام الأجازة");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        vacancyDaysNumber.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(vacancyDaysNumber, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(phoneNumber.getText().toString()==null){
                alertDialogBuilder.setMessage("برجاء تسجيل الدخول من جديد يوجد مشكله في تحميل التليفون الأول");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        phoneNumber.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(phoneNumber, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(phoneNumber.getText().toString().equals("")){
                alertDialogBuilder.setMessage("برجاء تسجيل الدخول من جديد يوجد مشكله في تحميل التليفون الأول");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        phoneNumber.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(phoneNumber, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(phoneCell.getText().toString()==null){
                alertDialogBuilder.setMessage("برجاء تسجيل الدخول من جديد يوجد مشكله في تحميل التليفون الثاني");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        phoneCell.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(phoneCell, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(phoneCell.getText().toString().equals("")){
                alertDialogBuilder.setMessage("برجاء تسجيل الدخول من جديد يوجد مشكله في تحميل التليفون الثاني");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        phoneCell.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(phoneCell, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(address.getText().toString()==null){
                alertDialogBuilder.setMessage("برجاء تسجيل الدخول من جديد يوجد مشكله في تحميل العنوان");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        address.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(address, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(address.getText().toString().equals("")){
                alertDialogBuilder.setMessage("برجاء تسجيل الدخول من جديد يوجد مشكله في تحميل العنوان");
                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        address.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(address, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
//            else if(alterEmpCode.getText().toString()==null){
//                alertDialogBuilder.setMessage("برجاء إدخال كود الموظف البديل");
//                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        alterEmpCode.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(alterEmpCode, InputMethodManager.SHOW_IMPLICIT);
//                    }
//                });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }else if(alterEmpCode.getText().toString().equals("")){
//                alertDialogBuilder.setMessage("برجاء إدخال كود الموظف البديل");
//                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        address.requestFocus();
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(alterEmpCode, InputMethodManager.SHOW_IMPLICIT);
//                    }
//                });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
            else {
                year = datePicker.getYear();
                month = datePicker.getMonth()+1;
                day = datePicker.getDayOfMonth();
                requestVacationsXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                        "    <soap:Header/>\n" +
                        "    <soap:Body>\n" +
                        "        <ns1:insertVacReq xmlns:ns1=\"http://vac.hr/\">\n" +
                        "            <vacationCode>" + vacCode + "</vacationCode>\n" +
                        "            <startYear>" + year + "</startYear>\n" +
                        "            <startMonth>" + month + "</startMonth>\n" +
                        "            <startDay>" + day + "</startDay>\n" +
                        "            <startDate>" + day + "/" + month + "/" + year + "</startDate>\n" +
                        "            <employeeName>" + username + "</employeeName>\n" +
                        "            <vacationDays>" + numberOfVacDays + "</vacationDays>\n" +
                        "            <empVacAddress>" + address.getText().toString() + "</empVacAddress>\n" +
                        "            <empVacPhone>" + phoneNumber.getText().toString() + "</empVacPhone>\n" +
                        "            <empVacMobile>" + phoneCell.getText().toString() + "</empVacMobile>\n" +
                        "            <alterEmpCode>" + alterEmployeeCode + "</alterEmpCode>\n" +
                        "            <empCode>" + empCode + "</empCode>\n" +
                        "        </ns1:insertVacReq>\n" +
                        "    </soap:Body>\n" +
                        "</soap:Envelope>";
                new SubmitVacationRequesTask().execute();
            }
        }
    }

    public class GetVacationListTask extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            HttpPost httpPost = new HttpPost(URLVacancy);
            StringEntity entity;
            try {
                entity = new StringEntity(requestVacationsXml, HTTP.UTF_8);
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
                adapterVacationsType = new ArrayAdapter<String>(VacancyRequestActivity.this,
                        android.R.layout.simple_spinner_item, spinnerData);
                adapterVacationsType.setDropDownViewResource(R.layout.spinner_custom_view);

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


    public class GetEmployeeListTask extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            HttpPost httpPost = new HttpPost(URLVacancy);
            StringEntity entity;
            try {
                entity = new StringEntity(requestEmployeeXml, HTTP.UTF_8);
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
            List<Employee> employeesData = new ArrayList<Employee>();
            final HashMap<String,Employee> employeesNameCode = new HashMap<String,Employee>();
            Employee employee ;
            int j=0;
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
                            employee = new Employee();
                            if(name.equals("return")) {
                                myparser.next();
                            }
                            name = myparser.getName();
                            if(name.equals("alterEmpCode")) {
                                employee.setCode(myparser.nextText());
                                myparser.next();
                            }name = myparser.getName();
                             if(name.equals("alterEmpName")){
                                 employee.setName(myparser.nextText());
                                 employeesData.add(employee);
                                 employeesNameCode.put(employee.getName(),employee);
                            }
                            break;

                        case XmlPullParser.END_TAG:

                            break;
                    }
                    event = myparser.next();
                }
                String[] spinnerData = new String[employeesData.size()];
                for(int i=0 ;i<employeesData.size();i++){
                    spinnerData[i] = employeesData.get(i).getName();
                }
                adapterEmployeeType = new ArrayAdapter<String>(VacancyRequestActivity.this,
                        android.R.layout.simple_spinner_item, spinnerData);
                adapterEmployeeType.setDropDownViewResource(R.layout.spinner_custom_view);

                spEmployees.setAdapter(adapterEmployeeType);
                spEmployees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        alterEmpName = parent.getItemAtPosition(position).toString();
                        Employee chosenAlterEmp = employeesNameCode.get(alterEmpName);
                        alterEmployeeCode = chosenAlterEmp.getCode().toString();
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





    public class SubmitVacationRequesTask extends AsyncTask<Void, Void, String> {
        public void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            HttpPost httpPost = new HttpPost(URLVacancy);
            StringEntity entity;
            try {
                entity = new StringEntity(requestVacationsXml, HTTP.UTF_8);
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
//                                String opNum = myparser.getText().replaceAll("\\D+","");

                                if(vacancyDaysNumber.getText().toString().equals("")||phoneNumber.getText().toString().equals("")||phoneCell.getText().toString().equals("")||address.getText().toString().equals("")){
                                    alertDialogBuilder.setMessage("طلبك خطأ");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                        }
                                    });
                                }
                                else {
                                    alertDialogBuilder.setMessage(myparser.nextText());
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            vacancyDaysNumber.setText("");
                                        }
                                    });
                                }

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
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
