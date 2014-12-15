package com.chat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.chat.ChatServiceProvider;
import com.chat.mobile.R;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends ChatFragmentActivity {
    private ArrayList<Fragment> fragments;
    @InjectView(R.id.tabpager)
    protected TabViewPager tabPager;
    @InjectView(R.id.img_main)
    protected FontAwesomeText img_main;
    @InjectView(R.id.img_contacts)
    protected FontAwesomeText img_contacts;
    @InjectView(R.id.img_detection)
    protected FontAwesomeText img_detection;
    @InjectView(R.id.img_me)
    protected FontAwesomeText img_me;
    @Inject
    protected ChatServiceProvider serviceProvider;
    private FontAwesomeText[] tabBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }

    private void init() {
        EMChatManager.getInstance().login("tommy123456", "tommy123456", new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
               /* new Thread(new Runnable() {
                    public void run() {
                        try {
                            // 调用sdk注册方法
                            EMChatManager.getInstance().createAccountOnServer("tommy123456", "tommy123456");
                        } catch (final Exception e) {
                        }
                    }
                }).start();*/
                // TODO Auto-generated method stub
            }
        });
        fragments = new ArrayList<Fragment>();
        fragments.add(new NotificationListFragment());
        fragments.add(new AddressListFragment());
        fragments.add(new FriendsFragment());
        fragments.add(new SetFragment());
        tabBtns = new FontAwesomeText[]{img_main, img_contacts, img_detection, img_me};
        tabPager.setAdapter(pageAdapter);
        tabPager.setOnPageChangeListener(onPageChangeListener);
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
        public void onPageSelected(int position) {
            setBottomTabSelected(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private boolean setBottomTabSelected(int position) {
        for (int i = 0; i < tabBtns.length; i++) {
            if (i == position) {
                tabBtns[i].setTextColor(getResources().getColor(R.color.bbutton_success));
            } else {
                tabBtns[i].setTextColor(getResources().getColor(R.color.Gray));
            }
        }
        return true;
    }

    @OnClick({R.id.bottom_tab_main, R.id.bottom_tab_contacts, R.id.bottom_tab_detection, R.id.bottom_tab_me})
    public void tabImgClick(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.bottom_tab_main:
                index = 0;
                break;
            case R.id.bottom_tab_contacts:
                index = 1;
                break;
            case R.id.bottom_tab_detection:
                index = 2;
                break;
            case R.id.bottom_tab_me:
                index = 3;
                break;
        }
        ;
        tabPager.setCurrentItem(index, true);
    }
}
