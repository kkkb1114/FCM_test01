package com.abacus.fcm_test01;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM_Log", token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null){ // 포어그라운드
            sendNotification(message.getNotification().getBody(), message.getNotification().getTitle());
        }else if (message.getData().size() > 0){ // 백그라운드
            sendNotification(message.getData().get("body"), message.getData().get("title"));
        }
    }

    private void sendNotification(String messageBody, String messageTitle)  {
        Log.d("FCM Log", "알림 메시지: " + messageBody);

        /* 알림의 탭 작업 설정 */
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = "Channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        /* 알림 만들기 */
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setFullScreenIntent(pendingIntent, true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* 새로운 인텐트로 앱 열기 */
        Intent newintent = new Intent(this, MainActivity.class);
        newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( newintent );

        /* 채널 만들기*/
        /* Android 8.0 이상에서 알림을 게시하려면 알림을 만들어야 함 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Channel Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
