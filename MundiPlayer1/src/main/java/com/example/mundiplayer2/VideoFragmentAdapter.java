package com.example.mundiplayer2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mundiplayer2.ijk.IjkFragment;

import java.util.ArrayList;

public class VideoFragmentAdapter extends FragmentPagerAdapter {

    private int                 count;
    private ArrayList<String>   mUrl;

    public VideoFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }

    public void setURL(ArrayList<String> url) {
        mUrl = url;
        count = mUrl.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position < count) {
            return IjkFragment.newInstance(mUrl.get(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}
