package com.example.androidlogin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Set;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG="AlarmReceiver";
    private Context contexts;
    String notificationid;
    Intent mainIntent;
    String text;
    private int action;
    public NotificationManager notificationManager;
    public PendingIntent contentIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.contexts = context;
        notificationid = intent.getStringExtra("id");
        text = intent.getStringExtra("drug");

        Log.e("약번호 넘어오자...", notificationid);
        Log.e("약이름 넘어오자...", text);



        //푸쉬알람 해주는 부분
        mainIntent = new Intent(context, SetAlarm.class);
        mainIntent.putExtra("cancelId",notificationid);
        Log.e("mainactivity에 보내는 ID값: ", notificationid);
        contentIntent = PendingIntent.getActivity(context, Integer.parseInt(notificationid), mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "201821079");

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {

            //Toast.makeText(context, "누가버전", Toast.LENGTH_SHORT).show();
            builder.setSmallIcon(R.drawable.ic_drug_icon);

            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("약꾹")
                    .setContentText(text + "을(를) 복용할시간에요:)")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(contentIntent)
                    .setContentInfo("INFO")
                    .setDefaults(Notification.DEFAULT_VIBRATE);

            if (notificationManager != null) {


                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "My:Tag"
                );
                wakeLock.acquire(5000);
                notificationManager.notify(Integer.parseInt(notificationid), builder.build());

            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Toast.makeText(context, "오레오 이상", Toast.LENGTH_SHORT).show();

            builder.setSmallIcon(R.drawable.ic_drug_icon);

            String channelId = "201821079";
            String chanelName = "YAKSSOK";
            String description = "매일 정해진 시간에 알림합니다. ";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, chanelName, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            if (notificationManager.getNotificationChannel(channelId) == null) {
                notificationManager.createNotificationChannel(channel);
            }

            builder.setSmallIcon(R.drawable.ic_drug_icon);

            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("약꾹")
                    .setContentText(text + "을(를) 복용할시간에요:)")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(contentIntent)
                    .setContentInfo("INFO")
                    .setDefaults(Notification.DEFAULT_VIBRATE);


            //if(notificationManager !=null){

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "My:Tag"
            );
            wakeLock.acquire(5000);
            notificationManager.notify(Integer.parseInt(notificationid), builder.build());

        }

    }

}