package com.android.vodmobileapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserLoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void checkUserLogin(View view) {
        new Thread(new CheckUserInServer()).start();
        //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
    }
    public void signUpUser(View view){
        Intent in = new Intent(this, SignUpUserActivity.class);
        startActivity(in);
    }






    public class CheckUserInServer implements Runnable {


        @Override
        public void run() {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME + "/CheckLoginServlet?username=" + username + "&password=" + password;
            // http://localhost:8084/VODProject/CheckLoginServlet?username=sss@ss&password=ss

            try {
                URL url = new URL(path);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int resCode = httpConn.getResponseCode();
                Log.d("VODMSG", "Connected Successfully");
                Log.d("VODMSG", "Response Code = " + resCode);
                final StringBuffer sb = new StringBuffer();
                String s;

                if (resCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));


                    while (true)
                    {
                        s = br.readLine();
                        Log.d("VODMSG", "servlet msg = " + s);
                        if (s != null)
                        {
                            sb.append(s);
                            Log.d("VODMSG", "servlet msg sb = " + sb);
                        }
                        else
                        {
                            break;
                        }
                    }

                    Log.d("VODMSG", "outside while = " + sb.toString());

                    //NOW GUI Logic
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(in);

                        }
                    });
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("VODMSG","-------Network Problem---------");
                Log.d("VODMSG","......Check Connection......");
            }

        }
    }




}
