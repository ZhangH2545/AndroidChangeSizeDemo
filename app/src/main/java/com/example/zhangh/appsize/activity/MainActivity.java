package com.example.zhangh.appsize.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zhangh.appsize.R;
import com.example.zhangh.appsize.util.EventBusBundle;
import com.example.zhangh.appsize.util.EventBusCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button setBtn;

    @Override
    public int viewById() {
        return R.layout.activity_main;
    }

    @Override
    public void bindView() {
        super.bindView();
        setBtn = $(R.id.tv_setting);
    }

    @Override
    public void afterView() {
        EventBus.getDefault().register(this);
        setBtn.setOnClickListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverEventBus(EventBusBundle bundle) {
        if (bundle.getCode() == EventBusCode.getInstance().RESTART) {
            try {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() ==R.id.tv_setting) {
            _startActivity(ChangesizeActivity.class);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
