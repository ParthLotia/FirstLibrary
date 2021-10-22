package com.example.testing

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SplashScreen


class MainActivity : AppCompatActivity() {
    lateinit var txtHello: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtHello = findViewById(R.id.txtHello)

        SplashScreen().initializeToken().toString()

    }

    override fun onStart() {
        super.onStart()
        offerReplacingDefaultDialer()

    }

    private fun offerReplacingDefaultDialer() {
        if (Build.VERSION.SDK_INT >= 29) {
            Log.e("here29","here29")
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(
                intent,
                2
            ) // you need to define CHANGE_DEFAULT_DIALER as a static final int

        } else {
            Log.e("here28","here28")
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            if (!packageName.equals(telecomManager.defaultDialerPackage)) {
                val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                startActivity(intent)
            }
        }

    }
}