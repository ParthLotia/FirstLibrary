package com.example.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.DemoActivity

class MainActivity : AppCompatActivity() {
    lateinit var txtHello : TextView
    lateinit var edt_token : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtHello = findViewById(R.id.txtHello)
        edt_token = findViewById(R.id.edt_token)

        edt_token.setText(DemoActivity().initializeToken().toString())

    }
}