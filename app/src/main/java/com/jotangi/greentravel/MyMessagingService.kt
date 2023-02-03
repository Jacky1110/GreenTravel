package com.jotangi.greentravel

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jotangi.greentravel.ui.main.MainActivity
import kotlin.random.Random


class MyMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "Coder"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        //喚醒手機
        wakeUpPhone()

        //O以上需要添加channel
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("msg", "消息", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        //檢查手機版本是否支援通知；若支援則新增"頻道"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )!!
            manager.createNotificationChannel(channel)
        }

        val s = remoteMessage.data


        //點擊跳轉畫面
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("data", s["key_1"])
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("豪豪", "onMessageReceived1: " + (remoteMessage.notification?.title))
        Log.d("豪豪", "onMessageReceived2: " + (remoteMessage.notification?.body))


        /**建置通知欄位的內容 */
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this@MyMessagingService, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_app)
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)


        /**發出通知 */
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(this@MyMessagingService)
        notificationManagerCompat.notify(Random.nextInt(5), builder.build())

//        notificationManager.notify(Random.nextInt(5), notification)

    }

    //第一次取得Token,後續取不到
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "裝置Token: " + token);
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun wakeUpPhone() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = powerManager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //開啟螢幕(10分鐘)
        wl.acquire(10 * 60 * 1000L)
        //釋放資源
        wl.release()
    }
}