package com.brown.moha.kotlinapp.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brown.moha.kotlinapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details_users.*

class DetailsUsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_users)


        val newIntent = intent
        val s1 = newIntent.extras.get("name")
        val s2 = newIntent.extras.get("phoneEmail")
        val s3 = newIntent.extras.get("photoUrl")
        tx1.text = s1.toString()
        tx2.text = s2.toString()
        Picasso.with(this)
                    .load(Uri.parse(s3.toString()))
                    .fit()
                    .into(imageView)


    }
}
