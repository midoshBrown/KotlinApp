package com.brown.moha.kotlinapp

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat
import org.jsoup.Jsoup
import java.io.IOException

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions and extra parameters.
 */
class ParseUrlIntentService : IntentService("ParseUrlIntentService") {

    override fun onHandleIntent(intent: Intent?) {


       // val url=intent?.dataString //originl
        val url="https://midoshapp-76192.firebaseapp.com/"
       // val doc:Document
       // var websiteLastUpdateDate=""
        val date=getWebLastUpdateDate(url)
        val sharedPref = getSharedPreferences(getString(R.string.my_Prefs_File), Context.MODE_PRIVATE)
        val prefsLastUpdate=sharedPref.getString("prefsLastUpdate","noValue")
        println("myWebsite: "+prefsLastUpdate)

       // if ((prefsLastUpdate!=date)&&(prefsLastUpdate!="noValue")){
            notifyWithUpdate()
       // }

        if (date!="") {

            val editor = sharedPref.edit()
            editor.putString("prefsLastUpdate",date)
            editor.apply()
            val prefsLastUpdate2=sharedPref.getString("prefsLastUpdate","noValue")
            println("myWebsite: $date $prefsLastUpdate2")        }
    }

    private fun notifyWithUpdate(){

        val intent=Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http://ulfds1.ul.edu.lb/")
        val resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val mBuilder = NotificationCompat.Builder(this,"not_id_1")
                .setSmallIcon(R.drawable.navigation_empty_icon)
                .setContentTitle("New update")
                .setContentText("website has been Updated")
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)

        val mNotificationId = 1
        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(mNotificationId, mBuilder.build())
    }
    private fun getWebLastUpdateDate(url: String?):String{
        var websiteLastUpdateDate=""
        try {
            val doc = Jsoup.connect(url).get()
            val element=doc.getElementsByClass("modifydate")
            websiteLastUpdateDate=element.text()
            println("myText: "+websiteLastUpdateDate)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return websiteLastUpdateDate
    }


}
