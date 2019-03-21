package com.example.zhangh.appsize.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhangh.appsize.fontseekbar.Utils;
import com.example.zhangh.appsize.util.CommonString;
import com.example.zhangh.appsize.util.SharePrefUtil;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewById());
        bindView();
        afterView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //重写字体缩放比例  api>25
    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            final Resources res = newBase.getResources();
            final Configuration config = res.getConfiguration();
            config.fontScale= Utils.getFontScale(SharePrefUtil.getInt(mContext, CommonString.CURRENT_INDEX_KEY, 1));//1 设置正常字体大小的倍数
            final Context newContext = newBase.createConfigurationContext(config);
            super.attachBaseContext(newContext);
        }else{
            super.attachBaseContext(newBase);
        }
    }

    //重写字体缩放比例 api<25
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration config = res.getConfiguration();
            config.fontScale= Utils.getFontScale(SharePrefUtil.getInt(mContext, CommonString.CURRENT_INDEX_KEY, 1));//1 设置正常字体大小的倍数
            res.updateConfiguration(config,res.getDisplayMetrics());
        }
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @LayoutRes
    public abstract int viewById();

    @UiThread
    public abstract void afterView();

    public void bindView() {

    }

    @SuppressWarnings("unchecked")
    public final <E extends View> E $(int id) {
        return (E) findViewById(id);
    }

    /**
     * 显示短Toast
     */
    public void showToastShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长Toast
     */
    public void showToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }



    /**
     * Intent
     */
    protected Intent _getIntent(Class<?> cls) {
        return new Intent(mContext, cls);
    }

    protected Intent _getIntent(Class<?> cls, int flags) {
        Intent intent = _getIntent(cls);
        intent.setFlags(flags);
        return intent;
    }

    protected void _startActivity(Class<?> cls) {
        startActivity(_getIntent(cls));
    }

    protected void _startActivity(Intent intent) {
        startActivity(intent);
    }

    protected void _startActivity(Class<?> cls, int flags) {
        startActivity(_getIntent(cls, flags));
    }

    protected void _startActivityForResult(Class<?> cls, int code) {
        startActivityForResult(_getIntent(cls), code);
    }

    protected void _startActivityForResult(Intent intent, int code) {
        startActivityForResult(intent, code);
    }


}
