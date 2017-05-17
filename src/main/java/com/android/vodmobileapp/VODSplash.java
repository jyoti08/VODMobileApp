package com.android.vodmobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VODSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodsplash);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(4000);
                }catch(InterruptedException e){

                }

            }
        });*/

              new Thread(new SplashJob()).start();



    }

    class SplashJob implements Runnable{
        @Override
        public void run() {


            try {
                for(int i=0;i<3;i++) {
                    Thread.sleep(2000);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent in = new Intent(getApplicationContext(), UserLoginActivity.class);
                        startActivity(in);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
