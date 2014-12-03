package com.chat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.chat.ChatServiceProvider;
import com.chat.mobile.R;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends ChatFragmentActivity {
    private ArrayList<Fragment> fragments;
    private TabViewPager tabpager;
    private FontAwesomeText img_main, img_classify, img_track, img_me;
    private FontAwesomeText[] tabOff;
    @Inject
    protected ChatServiceProvider serviceProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new WeixinFragment());
        fragments.add(new AddressListFragment());
        fragments.add(new FriendsFragment());
        fragments.add(new SetFragment());
        img_main = (FontAwesomeText) findViewById(R.id.img_main);
        img_main.setIcon("fa-music");
        img_classify = (FontAwesomeText) findViewById(R.id.img_classify);
        img_track = (FontAwesomeText) findViewById(R.id.img_track);
        img_me = (FontAwesomeText) findViewById(R.id.img_me);
        tabOff = new FontAwesomeText[]{img_main, img_classify, img_track, img_me};

        tabpager = (TabViewPager) findViewById(R.id.tabpager);
        tabpager.setAdapter(pageAdapter);
        tabpager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private FragmentPagerAdapter pageAdapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public int getCount() {

            return fragments.size();
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragments.get(arg0);
        }

    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}
