package com.example.mundiplayer2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String         TAG = "MainActivity";
    private ViewPager                   mViewPager;
    private VideoFragmentAdapter        mAdapter;
    private ArrayList<String>           mUrls;
    public static boolean               testNetWork = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrls = new ArrayList<String>();
        initVideosUrl();
        mViewPager = findViewById(R.id.viewpager);
        FragmentManager fm = getSupportFragmentManager();
        mAdapter =  new VideoFragmentAdapter(fm,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mAdapter.setURL(mUrls);
        mViewPager.setAdapter(mAdapter);
    }

    private void initVideosUrl() {
        String[] urls = getResources().getStringArray(R.array.array_urls);
        for(String str : urls) {
            String str1 = getString(R.string.default_web) + str + "_HD.mp4";
            mUrls.add(str1);
            Log.d(TAG, "initVideosUrl item = " + str);
        }
        return;
    }

    private String getUrl(String mUrlString) {
        if (testNetWork) {
            return mUrlString;
        } else {
            return App.getProxy(this).getProxyUrl(mUrlString, true);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int i = mViewPager.getCurrentItem();
        int count = mViewPager.getAdapter().getCount();
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                i--;
                if (i < 0) i = count-1;
                mViewPager.setCurrentItem(i, true);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                i++;
                if (i >= count) i = 0;
                mViewPager.setCurrentItem(i, true);
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
