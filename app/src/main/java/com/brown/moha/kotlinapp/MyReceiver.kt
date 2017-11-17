package com.brown.moha.kotlinapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


/**
 *
 */
class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bcAction="android.intent.action.BOOT_COMPLETED"
        //val rebootAction="android.intent.action.REBOOT"
        if (bcAction == intent.action) {
            val pushIntent = Intent(context, ParseUrlIntentService::class.java)
            context.startService(pushIntent)
        }
    }
}