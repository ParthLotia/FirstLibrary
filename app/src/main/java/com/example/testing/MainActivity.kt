package com.example.testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.APICall

class MainActivity : AppCompatActivity() {
    lateinit var txtHello : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtHello = findViewById(R.id.txtHello)
        txtHello.text = APICall().setData(this,"Parth")

    }
}