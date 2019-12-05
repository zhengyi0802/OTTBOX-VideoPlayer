package com.example.mundiplayer2.ijk;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mundiplayer2.R;

/**
 */
public class IjkFragment extends Fragment {

    private IjkPlayer                   ijkPlayer;
    private static String               mUrl;
    private static IjkFragment          mInstance;

    public static IjkFragment newInstance(String url) {
        mUrl = url;
        mInstance = new IjkFragment();
        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ijk, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ijkPlayer = (IjkPlayer) view.findViewById(R.id.ijkplayer);
        ijkPlayer.setPath(mUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        ijkPlayer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ijkPlayer.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ijkPlayer.release();
    }


}
