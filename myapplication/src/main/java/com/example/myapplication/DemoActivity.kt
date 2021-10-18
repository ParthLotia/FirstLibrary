package com.example.myapplication

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class DemoActivity : AppCompatActivity() {
    var token : String? = ""
    var temp_token : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)


        initializeToken()

    }

    fun initializeToken() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast

            Log.e("adsads", token!!)
        })
    }

}