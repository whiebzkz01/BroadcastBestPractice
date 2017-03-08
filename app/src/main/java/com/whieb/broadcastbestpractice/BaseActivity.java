package com.whieb.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhukongzhen on 2017/3/3.
 */

public class BaseActivity extends AppCompatActivity {

    private  ForceOfflineReceiver forceOfflineReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.whieb.broadcastbestpractice.FORCE_OFFLINE");
        forceOfflineReceiver = new ForceOfflineReceiver();
        registerReceiver(forceOfflineReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (forceOfflineReceiver != null){
            unregisterReceiver(forceOfflineReceiver);
            forceOfflineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class  ForceOfflineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, final Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning");
            builder.setMessage("You are forced to be offline.Please try to login again.");
            builder.setCancelable(false); //设置对话框不可取消
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){ //注册确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll(); //销毁所有活动
                    Intent intent1 = new Intent(context,LoginActivity.class);
                    context.startActivity(intent1); //重新启动LoginActivity
                }
            });
            builder.show();

        }
    }
}
