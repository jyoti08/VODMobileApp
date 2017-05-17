package com.android.vodmobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignUpUserActivity extends AppCompatActivity {
    EditText etEmail,etPassword,etName,etPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        etEmail = (EditText) (findViewById(R.id.etEmail));
        etPassword = (EditText) (findViewById(R.id.etPassword));
        etName = (EditText) (findViewById(R.id.etName));
        etPhone = (EditText) (findViewById(R.id.etPhone));
    }

    public  void saveNewUser(View view){

        new Thread(new SaveUserThread()).start();


    }
    public class SaveUserThread implements Runnable{

        @Override
        public void run() {

           String emailStr =  etEmail.getText().toString();
            String passwordStr = etPassword.getText().toString();
            String nameStr = etName.getText().toString();
            String phoneStr = etPhone.getText().toString();




            final String uripath = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/SignUpServlet?email=" + emailStr
                    + "&password=" + passwordStr
                    + "&name=" + nameStr
                    + "&phone=" + phoneStr
                    ;

            try {
                URL url = new URL(uripath);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");

                Log.d("VODMSG", "Sign up Connected Successfully");

                int resCode = httpConn.getResponseCode();

                Log.d("VODMSG", "Response Code = " + resCode);
                final StringBuffer sb = new StringBuffer();
                String s;

                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));


                    while (true) {
                        s = br.readLine();
                        Log.d("VODMSG", "servlet msg = " + s);
                        if (s != null) {
                            sb.append(s);
                            Log.d("VODMSG", "servlet msg sb = " + sb);
                            break;
                        }

                        //Now render data in GUI
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                            }
                        });


                    }

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }
}
