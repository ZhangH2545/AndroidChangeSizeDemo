package com.example.zhangh.appsize.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.zhangh.appsize.R;
import com.example.zhangh.appsize.fontseekbar.FontSeekBar;
import com.example.zhangh.appsize.fontseekbar.Utils;
import com.example.zhangh.appsize.util.CommonString;
import com.example.zhangh.appsize.util.EventBusBundle;
import com.example.zhangh.appsize.util.EventBusCode;
import com.example.zhangh.appsize.util.SharePrefUtil;

import org.greenrobot.eventbus.EventBus;

public class ChangesizeActivity extends BaseActivity implements View.OnClickListener {

    private FontSeekBar fontSeekBar;
    private TextView tvContent1;
    private TextView tvBack;
    private TextView tvClose;
    private float currentSizef;
    ;//缩放比例
    private int currentIndex;
    private float textsize1;
    private static int textSizeIndex = -1;

    @Override
    public int viewById() {
        return R.layout.activity_changesize;
    }

    @Override
    public void bindView() {
        super.bindView();
        fontSeekBar = $(R.id.FontSeekBar);
        tvContent1 = $(R.id.tv_chatcontent);
        tvBack = $(R.id.normal_topbar_back);
        tvClose = $(R.id.normal_topbar_right2);
    }

    @Override
    public void afterView() {
//        EventBus.getDefault().register(this);
        tvBack.setOnClickListener(this);
        tvClose.setOnClickListener(this);

        currentIndex = SharePrefUtil.getInt(this, CommonString.CURRENT_INDEX_KEY, 1);
        currentSizef = Utils.getFontScale(currentIndex);
        textsize1 = tvContent1.getTextSize() / currentSizef;
        fontSeekBar.setTickCount(4).setTickHeight(Utils.dip2px(this, 15)).setBarColor(Color.GRAY)
                .setTextColor(Color.BLACK).setTextPadding(Utils.dip2px(this, 10)).setTextSize(Utils.dip2px(this, 14))
                .setThumbRadius(Utils.dip2px(this, 10)).setThumbColorNormal(Color.GRAY).setThumbColorPressed(Color.GRAY)
                .setOnSliderBarChangeListener(new FontSeekBar.OnSliderBarChangeListener() {
                    @Override
                    public void onIndexChanged(FontSeekBar rangeBar, int index) {
                        if (index > 3) {
                            return;
                        }
                        float textSizef = Utils.getFontScale(index);
                        setTextSize(textSizef);
                        textSizeIndex = index;
                    }
                }).setThumbIndex(currentIndex).withAnimation(false).applay();

    }

    private void setTextSize(float textSize) {
        //改变当前页面的字体大小
        tvContent1.setTextSize(Utils.px2dip(this, textsize1 * textSize));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int lastIndex = SharePrefUtil.getInt(getApplicationContext(), CommonString.CURRENT_INDEX_KEY, -1);
            if (textSizeIndex == -1 || textSizeIndex == lastIndex) {
                finish();
            } else {
                refresh();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_topbar_back:
            case R.id.normal_topbar_right2:
                int lastIndex = SharePrefUtil.getInt(getApplicationContext(), CommonString.CURRENT_INDEX_KEY, -1);
                if (textSizeIndex == -1 || textSizeIndex == lastIndex) {
                    finish();
                } else {
                    refresh();
                }
                break;
            default:
                break;
        }

    }

    private void refresh() {
        //存储标尺的下标
        SharePrefUtil.saveInt(getApplicationContext(), CommonString.CURRENT_INDEX_KEY, textSizeIndex);
        //通知主页面重启
        EventBus.getDefault().post(new EventBusBundle(EventBusCode.getInstance().RESTART));
        //2s后关闭  延迟执行任务 重启完主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                hideMyDialog();
                ChangesizeActivity.this.finish();
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        Log.d("ChangeSDestroy", "onDestroy");
    }

}
