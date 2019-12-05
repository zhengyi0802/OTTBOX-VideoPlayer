package com.munditv.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;
import com.munditv.videoplayer.ijk.IjkPlayerActivity;
import com.munditv.videoplayer.vlc.VlcPlayerActivity;
import com.vlc.lib.listener.util.LogUtils;
import com.vlc.lib.listener.util.VLCInstance;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    public static boolean testNetWork       = true;
    private EditText        mURLEditText    = null;
    private Button          mVlcButton      = null;
    private Button          mIjkButton      = null;
    private Button          mVDoubleButton  = null;
    private Context         mContext        = null;

    private static String   mUrlString      = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mURLEditText = findViewById(R.id.utl_text);
        mVlcButton = findViewById(R.id.vlc_button);
        mVlcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrlString = mURLEditText.getText().toString();
                test();
                openVlcVideo();
            }
        });
        mIjkButton = findViewById(R.id.ijk_button);
        mIjkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrlString = mURLEditText.getText().toString();
                test();
                openIjkVideo();
            }
        });
        mVDoubleButton = findViewById(R.id.double_button);
        mVDoubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrlString = mURLEditText.getText().toString();
                test();
                openDoubleVideo();
            }
        });
        mContext = this;
        init();
    }

    private void openVlcVideo() {
        startActivity(new Intent(this, VlcPlayerActivity.class));
    }

    private void openIjkVideo() {
        startActivity(new Intent(this, IjkPlayerActivity.class));
    }

    private void openDoubleVideo() {
        startActivity(new Intent(this, TestActivity.class));
    }

    private void init() {
        //加载库文件
        if (VLCInstance.testCompatibleCPU(this)) {
            Log.i(TAG, "support   cpu");
        } else {
            Log.i(TAG, "not support  cpu");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        }
    }

    private void test() {
        App.getProxy(mContext).registerCacheListener(new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
                Log.i(TAG, "support   percentsAvailable=" + percentsAvailable);
            }
        }, mUrlString);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10){
            if(grantResults[0]!=0){
                Toast.makeText(this,"WRITE_EXTERNAL_STORAGE error", Toast.LENGTH_LONG).show();
            }
            LogUtils.i("Permissions","grantResults="+ Arrays.toString(grantResults));
        }
    }

    public static String getUrl(Context context) {
        if (testNetWork) {
            return mUrlString;
        } else {
            return App.getProxy(context).getProxyUrl(mUrlString, true);
        }
    }

}
