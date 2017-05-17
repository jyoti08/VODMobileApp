package com.android.vodmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MoviesFragment extends Fragment  {

    CarouselView caro;
    String imagesArr[],horrorImgArr[],comedyImgArr[],romanticImgArr[],actionImgArr[],familyImgArr[],crimeImgArr[],thrillerImgArr[];
    int horrorIdArr[],comedyIdArr[],romanticIdArr[],actionIdArr[],familyIdArr[],crimeIdArr[],thrillerIdArr[];
    LinearLayout dynamicLinLayoutForHorror,dynamicLinLayoutForComedy,dynamicLinLayoutForRomance,dynamicLinLayoutForAction,dynamicLinLayoutForFamily,dynamicLinLayoutForCrime,dynamicLinLayoutForThriller;
    //String[] typesOfMovies = {"HORROR","COMEDY","ROMANTIC","ACTION","FAMILY","CRIME","THRILLER"};
    public static final String HORROR = "HORROR";
    public static final String COMEDY = "COMEDY";
    public static final String ROMANTIC = "ROMANTIC";
    public static final String ACTION = "ACTION";
    public static final String FAMILY = "FAMILY";
    public static final String CRIME = "CRIME";
    public static final String THRILLER = "THRILLER";

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        caro = (CarouselView) (getActivity().findViewById(R.id.caro));
        dynamicLinLayoutForHorror = (LinearLayout) getActivity().findViewById(R.id.dynamicLinLayoutForHorror);
        dynamicLinLayoutForComedy = (LinearLayout)  getActivity().findViewById(R.id.dynamicLinLayoutForComedy);
        dynamicLinLayoutForRomance = (LinearLayout)  getActivity().findViewById(R.id.dynamicLinLayoutForRomance);
        dynamicLinLayoutForAction = (LinearLayout)  getActivity().findViewById(R.id.dynamicLinLayoutForAction);
        dynamicLinLayoutForFamily = (LinearLayout)  getActivity().findViewById(R.id.dynamicLinLayoutForFamily);
        dynamicLinLayoutForCrime = (LinearLayout)  getActivity().findViewById(R.id.dynamicLinLayoutForCrime);
        dynamicLinLayoutForThriller = (LinearLayout)  getActivity().findViewById(R.id.dynamicLinLayoutForThriller);

        new Thread(new GetJsonVideoImg()).start();
        new Thread(new GetHorrorMovies()).start();
        new Thread(new GetComedyMovies()).start();
        new Thread(new GetRomanceMovies()).start();
        new Thread(new GetFamilyMovies()).start();
        new Thread(new GetActionMovies()).start();
        new Thread(new GetCrimeMovies()).start();
        new Thread(new GetThrillerMovies()).start();

    }
    public class GetJsonVideoImg implements Runnable{
        @Override
        public void run() {
            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=COMMON";

            try {
                URL url = new URL(path);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int resCode = httpConn.getResponseCode();

                Log.d("VODMSG", "Response Code = " + resCode);
                String data = new String();
                if (resCode == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    while(true)
                    {
                        String s = br.readLine();
                        if(s==null){
                            break;
                        }
                        data = data + s;
                    }

                    Log.d("VODMSG","Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    final JSONArray jsonArr = (JSONArray) json.getJSONArray("getLatesVideos");
                    imagesArr=new String[jsonArr.length()];

                    for (int i=0;i<jsonArr.length();i++){
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        imagesArr[i]= obj.get("bigimage").toString();
                    }

                    caro.setImageListener(new ImageListener() {
                        @Override
                        public void setImageForPosition(int position, ImageView imageView) {
                            Picasso.with(getActivity().getApplicationContext())
                                    .load(MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME + imagesArr[position].substring(1)).into(imageView);
                        }
                    });

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            caro.setPageCount(jsonArr.length());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class GetRomanceMovies implements Runnable{

        @Override
        public void run() {

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=ROMANCE";

            try {
                URL url = new URL(path);
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
                    Log.d("VODMSG", "Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    JSONArray jsonArr = (JSONArray) json.getJSONArray("getRomanticVideos");
                    romanticImgArr = new String[jsonArr.length()];
                    romanticIdArr = new int[jsonArr.length()];

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        romanticImgArr[i] = obj.get("coverphoto").toString();
                        romanticIdArr[i] = Integer.parseInt(obj.get("id").toString());
                    }

                    for(int j=0;j<romanticImgArr.length;j++){
                        final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        imageView.setId(j*1);
                        final int id=j;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedActivity(id,ROMANTIC);
                            }
                        });
                        final int k=j;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicLinLayoutForRomance.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                        + MyClient.APPLICATION_NAME
                                        + romanticImgArr[k].substring(1)).into(imageView);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class GetComedyMovies implements Runnable{

        @Override
        public void run() {

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=COMEDY";

            try {
                URL url = new URL(path);
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

                    Log.d("VODMSG", "Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    JSONArray jsonArr = (JSONArray) json.getJSONArray("getComedyVideos");
                    comedyImgArr = new String[jsonArr.length()];
                    comedyIdArr = new int[jsonArr.length()];

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        //int id = Integer.parseInt(obj.get("id").toString());
                        comedyIdArr[i] = Integer.parseInt(obj.get("id").toString());
                        comedyImgArr[i] = obj.get("coverphoto").toString();
                    }

                    for(int j=0;j<comedyImgArr.length;j++)
                    {
                        final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        imageView.setId(j*1);
                        final int comedyId=j;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedActivity(comedyId,COMEDY);
                            }
                        });

                        final int k=j;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicLinLayoutForComedy.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                        + MyClient.APPLICATION_NAME
                                        + comedyImgArr[k].substring(1)).into(imageView);
                            }
                        });
                        Log.d("VODMSG","dynamicLinLayoutForComedy view Added"+j);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class GetHorrorMovies implements Runnable{

        @Override
        public void run() {
        final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                + "/MoviesFregmentServlet?getListofType=HORROR";

        try {
            URL url = new URL(path);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int resCode = httpConn.getResponseCode();

            Log.d("VODMSG", "Response Code = " + resCode);
            String data = "";

            if (resCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                while (true) {
                    String s = br.readLine();
                    if (s == null) {
                        break;
                    }
                    data = data + s;
                }

                Log.d("VODMSG", "Json response from server:" + data);
                JSONObject json = new JSONObject(data);
                JSONArray jsonArr = (JSONArray) json.getJSONArray("getHorrorVideos");
                horrorImgArr = new String[jsonArr.length()];
                horrorIdArr = new int[jsonArr.length()];

                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                    horrorIdArr[i] = Integer.parseInt(obj.get("id").toString());
                    horrorImgArr[i] = obj.get("coverphoto").toString();
                }

                for(int j=0;j<horrorImgArr.length;j++)
                {
                    final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setPadding(5,2,5,2);
                    imageView.setId(j*1);

                    final int id=j;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDetailedActivity(id,HORROR);
                        }
                    });

                    final int k=j;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dynamicLinLayoutForHorror.addView(imageView);
                            Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                    + MyClient.APPLICATION_NAME
                                    + horrorImgArr[k].substring(1)).into(imageView);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
    public class GetActionMovies implements Runnable{

        @Override
        public void run() {

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=ACTION";

            try {
                URL url = new URL(path);
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
                    Log.d("VODMSG", "Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    JSONArray jsonArr = (JSONArray) json.getJSONArray("getActionVideos");
                    actionImgArr = new String[jsonArr.length()];
                    actionIdArr = new int[jsonArr.length()];

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        actionImgArr[i] = obj.get("coverphoto").toString();
                        actionIdArr[i] = Integer.parseInt(obj.get("id").toString());
                    }

                    for(int j=0;j<actionImgArr.length;j++){
                        final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        imageView.setId(j*1);
                        final int id=j;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedActivity(id,ACTION);
                            }
                        });
                        final int k=j;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicLinLayoutForAction.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                        + MyClient.APPLICATION_NAME
                                        + actionImgArr[k].substring(1)).into(imageView);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class GetFamilyMovies implements Runnable{

        @Override
        public void run() {

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=FAMILY";

            try {
                URL url = new URL(path);
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
                    Log.d("VODMSG", "Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    JSONArray jsonArr = (JSONArray) json.getJSONArray("getFamilyVideos");
                    familyImgArr = new String[jsonArr.length()];
                    familyIdArr = new int[jsonArr.length()];

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        familyImgArr[i] = obj.get("coverphoto").toString();
                        familyIdArr[i] = Integer.parseInt(obj.get("id").toString());
                    }

                    for(int j=0;j<familyImgArr.length;j++){
                        final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        imageView.setId(j*1);
                        final int id=j;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedActivity(id,FAMILY);
                            }
                        });
                        final int k=j;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicLinLayoutForFamily.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                        + MyClient.APPLICATION_NAME
                                        + familyImgArr[k].substring(1)).into(imageView);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class GetCrimeMovies implements Runnable{

        @Override
        public void run() {

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=CRIME";

            try {
                URL url = new URL(path);
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
                    Log.d("VODMSG", "Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    JSONArray jsonArr = (JSONArray) json.getJSONArray("getCrimeVideos");
                    crimeImgArr = new String[jsonArr.length()];
                    crimeIdArr = new int[jsonArr.length()];

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        crimeImgArr[i] = obj.get("coverphoto").toString();
                        crimeIdArr[i] = Integer.parseInt(obj.get("id").toString());
                    }

                    for(int j=0;j<crimeImgArr.length;j++){
                        final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        imageView.setId(j*1);
                        final int id=j;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedActivity(id,CRIME);
                            }
                        });
                        final int k=j;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicLinLayoutForCrime.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                        + MyClient.APPLICATION_NAME
                                        + crimeImgArr[k].substring(1)).into(imageView);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class GetThrillerMovies implements Runnable{

        @Override
        public void run() {

            final String path = MyClient.SERVER_DOMAIN + MyClient.APPLICATION_NAME
                    + "/MoviesFregmentServlet?getListofType=Thriller";

            try {
                URL url = new URL(path);
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
                    Log.d("VODMSG", "Json response from server:" + data);
                    JSONObject json = new JSONObject(data);
                    JSONArray jsonArr = (JSONArray) json.getJSONArray("getThrillerVideos");
                    thrillerImgArr = new String[jsonArr.length()];
                    thrillerIdArr = new int[jsonArr.length()];

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArr.getJSONObject(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        thrillerImgArr[i] = obj.get("coverphoto").toString();
                        thrillerIdArr[i] = Integer.parseInt(obj.get("id").toString());
                    }

                    for(int j=0;j<thrillerImgArr.length;j++){
                        final ImageView imageView = new ImageView(getActivity().getApplicationContext());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(300,300));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setPadding(5,2,5,2);
                        imageView.setId(j*1);
                        final int id=j;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openDetailedActivity(id,THRILLER);
                            }
                        });
                        final int k=j;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dynamicLinLayoutForThriller.addView(imageView);
                                Picasso.with(getActivity().getApplicationContext()).load(MyClient.SERVER_DOMAIN
                                        + MyClient.APPLICATION_NAME
                                        + thrillerImgArr[k].substring(1)).into(imageView);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void openDetailedActivity(int id,String type){
        int newId = 0;
        if(type.equals(ROMANTIC)) {
            newId = romanticIdArr[id];

        }else if(type.equals(COMEDY)){
            newId = comedyIdArr[id];
        }else if(type.equals(HORROR)){
            newId = horrorIdArr[id];
        }else if(type.equals(ACTION)){
            newId = actionIdArr[id];
        }else if(type.equals(FAMILY)){
            newId = familyIdArr[id];
        }else if(type.equals(CRIME)){
            newId = crimeIdArr[id];
        }else if(type.equals(THRILLER)){
            newId = thrillerIdArr[id];
        }

        Intent in = new Intent(getActivity().getApplicationContext(),MovieDetailActivity.class);
        in.putExtra("movieId", newId);
        Log.d("VODMSG",newId+"");
        startActivity(in);

    }

}
