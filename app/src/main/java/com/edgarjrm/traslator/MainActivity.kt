package com.edgarjrm.traslator

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    lateinit var Et_Idiom_Origen:EditText
    lateinit var Tv_desty:TextView
    lateinit var Btb_Idiom_select:MaterialButton
    lateinit var Btb_Idiom_choise:MaterialButton
    lateinit var Btn_Traslator:MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Et_Idiom_Origen = findViewById(R.id.Et_Idiom_Origen)
        Tv_desty = findViewById(R.id.Tv_desty)
        Btb_Idiom_select = findViewById(R.id.Btb_Idiom_select)
        Btb_Idiom_choise = findViewById(R.id.Btb_Idiom_choise)
        Btn_Traslator = findViewById(R.id.Btn_Traslator)

    }

}