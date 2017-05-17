package com.android.vodmobileapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {
    String path;
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent in, int flag,int serviceId){
       path =  in.getStringExtra("path");
        new Thread(new DownloadJob1()).start();

       // Toast.makeText(this, "Downloading Complete..", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }
    class DownloadJob1 implements Runnable
    {
        public void run()
        {
            try
            {
                String pathoffileonserver="/myuploads/new.mp4";

               // final String urlpath=MyClient.SERVER_DOMAIN+MyClient.APPLICATION_NAME+"/FileDownloader?filepath="+path;
                final String urlpath=MyClient.SERVER_DOMAIN+MyClient.APPLICATION_NAME+"/FileDownloader?filepath="+pathoffileonserver;
                URL url = new URL(urlpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int resCode = connection.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK)
                {
                    Log.d("VODMSG","Content Type "+connection.getContentType());

                    if(connection.getContentType().contains("text/plain"))   //Logic for Text response
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        StringBuffer sb = new StringBuffer();

                        while (true)
                        {
                            String s = br.readLine();

                            if (s == null)
                            {
                                break;
                            }
                            sb.append(s);
                        }


                        //This time its simple String Data, But it can be JSON
                        final String ans=sb.toString();
                        Log.d("VODMSG","ANSWER FROM SERVER "+ans);
                    }
                    else                                             //Logic for non-text response ie file is coming
                    {
                        long filesize = connection.getContentLength();
                        String fileheadername = connection.getHeaderField("Content-Disposition");
                        Log.d("VODMSG", "C-D " + fileheadername);

                        String extractedstring=fileheadername.substring(fileheadername.indexOf("filename="));
                        int pos1=extractedstring.indexOf('\"');
                        int pos2=extractedstring.lastIndexOf('\"');

                        String incomingfilename = extractedstring.substring(pos1+1,pos2);

                        Log.d("VODMSG", "Incoming File Name "+incomingfilename);

                        long count = 0;
                        int r,per;
                        byte b[]=new byte[10000];
                        // Log.d("VODMSG", "declaration ok ");

                        File f=new File("/mnt/sdcard/vod");

                        if(!f.exists())
                        {
                            f.mkdir();
                        }

                        DataInputStream dis = new DataInputStream(connection.getInputStream());
                        FileOutputStream fos=new FileOutputStream(f.getPath()+incomingfilename);
                        // Log.d("VODMSG", "File name object ok ");

                        while(true)
                        {
                            //Log.d("VODMSG", "while start ok ");
                            r=dis.read(b,0,10000);
                            fos.write(b,0,r);

                            count=count+r;
                            per=(int)((count*100)/filesize);

                            if(per%2==0) {
                                Intent intent = new Intent("my.download.broadcast");
                                intent.putExtra("per", per);
                                sendBroadcast(intent);
                                // pbar.setProgress(per);
                            }

                            //Log.d("VODMSG", "while in ok ");
                            if(count==filesize)
                            {
                                break;
                            }
                        }

                        // Log.d("VODMSG", "end ok ");
                        fos.close();
                        dis.close();
                        Log.d("VODMSG", "......Download Complete........ ");
                        

                    }

                }
                else if(resCode==HttpURLConnection.HTTP_NOT_FOUND)
                {
                    Log.d("VODMSG","404 NOT FOUND");
                    Log.d("VODMSG","urlpath "+urlpath);
                    Toast.makeText(getApplicationContext(),"404 NOT FOUND\nCHECK Web Path\n"+urlpath,Toast.LENGTH_SHORT).show();

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
