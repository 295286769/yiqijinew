package com.yiqiji.money.modules.myModule.common;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.yiqiji.money.R;
import com.yiqiji.money.modules.homeModule.home.activity.HomeActivity;
import com.yiqiji.money.modules.homeModule.write.activity.ExpenditureActivity;

/**
 * Created by dansakai on 2017/5/15.
 */

public class RemindReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, HomeActivity.class), 0);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("记账提醒")
                .setContentText("在你忘记之前,一起记录今天的收支吧!")
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(1, notification);
    }
}
