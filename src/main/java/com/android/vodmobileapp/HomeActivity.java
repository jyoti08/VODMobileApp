package com.android.vodmobileapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeActivity extends AppCompatActivity {
    TabLayout tl;
    ViewPager vp;
   // CarouselView caro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tl = (TabLayout) findViewById(R.id.tl);
        vp = (ViewPager) findViewById(R.id.vp);


        tl.setupWithViewPager(vp);
        vp.setAdapter(new MyAdapter(getSupportFragmentManager()));

        /*caro = (CarouselView) findViewById(R.id.caro);

        final int imagesArr[] = {R.drawable.mov1, R.drawable.mov2, R.drawable.mov3,R.drawable.mov4};
        caro.setPageCount(4);
        caro.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(imagesArr[position]);
            }
        });
*/


    }


    public class MyAdapter extends FragmentPagerAdapter {

        String name[] = {"Movies", "TV"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return name[position];
        }


        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MoviesFragment();
            } else if (position == 1) {
                return new TvFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }


    }
}
