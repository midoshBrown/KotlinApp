package com.brown.moha.kotlinapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.util.*


/**
 *
 */
class AlarmDeviceBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val serviceIntent = Intent(context, ParseUrlIntentService::class.java)
            val alarmIntent= PendingIntent.getService(context,0,serviceIntent,0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val interval= (Random().nextInt(3_600_000) + 14_400_000).toLong()
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    interval,
                    alarmIntent)
        }
    }
}