package com.android.vodmobileapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    Video video;
    LinearLayout activity_movie_detail;
    ImageView backImgView;
    TextView title;
    ImageView coverImgView;
    TextView director;
    TextView producer, time, rating;
    ProgressBar pbar;
    int per;

    MyFileDownloadReceiver broadcastreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        activity_movie_detail = (LinearLayout) findViewById(R.id.activity_movie_detail);
        title = (TextView) findViewById(R.id.title);
        director = (TextView) findViewById(R.id.director);
        producer = (TextView) findViewById(R.id.producer);
        time = (TextView) findViewById(R.id.time);
        rating = (TextView) findViewById(R.id.rating);

        coverImgView = (ImageView) findViewById(R.id.coverImgView);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        pbar = (ProgressBar) findViewById(R.id.pbar);

        broadcastreceiver= new MyFileDownloadReceiver();
        //Java Bean for Videos Object
        video = new Video();

        Intent in = getIntent();
        int movieId = in.getIntExtra("movieId",0);

        GetMovieDetailById  movDetail = new GetMovieDetailById(movieId);
        new Thread(movDetail).start();
    }

    public void playVideo(View view){
        Intent in = new Intent(this,PlayVideoActivity.class);
        in.putExtra("videopath", video.getVideopath().substring(1));
        startActivity(in);
    }

    public void downloadVideo(View view){
        Intent in = new Intent(this, DownloadService.class);
        in.putExtra("path",video.getVideopath());
        startService(in);



      /*  Thread t=new Thread(new DownloadJob1());
        t.start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter inf = new IntentFilter();
        inf.addAction("my.download.broadcast");

        registerReceiver(broadcastreceiver,inf);
        Toast.makeText(getApplicationContext(),"BR Registered",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(broadcastreceiver);
        Toast.makeText(getApplicationContext(),"BR UN-Registered",Toast.LENGTH_SHORT).show();
    }

    public class GetMovieDetailById implements Runnable
    {
        int movieId;
        GetMovieDetailById(int id){
            this.movieId = id;
        }
        @Override
        public void run() {
            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesDetailServlet?id="+movieId;

            try {
                final URL url = new URL(path);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int resCode = httpConn.getResponseCode();

                Log.d("VODMSG", "Response Code = " + resCode);
                String data = new String();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    while (true) {
                        String s = br.readLine();
                        if (s == null) {
                            break;
                        }
                        data = data + s;
                    }

                    Log.d("VODMSG", "-------Json response from server------:" + data);
                    JSONObject obj = new JSONObject(data);
                    JSONObject movieDetObj = (JSONObject) obj.getJSONObject("movieDeatil");

                    video.setId(movieDetObj.get("id").toString());
                    video.setTitle(movieDetObj.get("title").toString());
                    video.setDirector(movieDetObj.get("director").toString());
                    video.setReleaseyear(movieDetObj.get("releaseyear").toString());
                    video.setProduction(movieDetObj.get("production").toString());
                    video.setTime(movieDetObj.get("time").toString());
                    video.setRating(movieDetObj.get("rating").toString());
                    video.setVideopath(movieDetObj.get("videopath").toString());
                    video.setTrailorpath(movieDetObj.get("trailorpath").toString());
                    video.setLanguage(movieDetObj.get("language").toString());
                    video.setBigimage(movieDetObj.get("bigimage").toString());
                    video.setCoverphoto(movieDetObj.get("coverphoto").toString());

                    final Uri backImgUri = Uri.parse(video.getBigimage().substring(1));
                    final Uri coverImgUri = Uri.parse(video.getBigimage().substring(1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Picasso.with(getApplicationContext())
                                    .load(MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME + backImgUri).into(backImgView);
                            Picasso.with(getApplicationContext())
                                    .load(MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME + coverImgUri).into(coverImgView);
                            title.setText(video.getTitle());
                            director.setText("director: "+video.getDirector());
                            producer.setText("producer:"+video.getProduction());
                            time.setText("Duration:"+video.getTime());
                            rating.setText("Rating:"+video.getRating());
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }




    class MyFileDownloadReceiver extends BroadcastReceiver {
        public MyFileDownloadReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("my.download.broadcast")){
                Toast.makeText(context, "BroadCast receiving Start", Toast.LENGTH_SHORT).show();
                popUpNotification(context,intent);
            }
        }
        public void popUpNotification(Context context,Intent intent){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("VOD File Downloading.....");
            builder.setContentText("downloading in progress");

            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setOngoing(true);
            int  per = intent.getIntExtra("per",0);
            builder.setProgress(100,per,false);
            builder.setContentInfo(per+" %");
            pbar.setProgress(per);
            //builder.setAutoCancel(true);
            Notification n = builder.build();

            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify(1,n);
        }
    }

}

