package com.edgarjrm.traslator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import android.widget.EditText
import android.widget.TextView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var etIdiomOrigen: EditText
    private lateinit var tvDesty: TextView
    private lateinit var btnTranslator: Button
    private lateinit var spanishEnglishTranslator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        etIdiomOrigen = findViewById(R.id.Et_Idiom_Origen)
        tvDesty = findViewById(R.id.Tv_desty)
        btnTranslator = findViewById(R.id.Btn_Traslator)

        // Configurar el traductor español-inglés
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        spanishEnglishTranslator = Translation.getClient(options)

        // Descargar el modelo de traducción
        downloadTranslationModel()

        // Configurar el botón de traducción
        btnTranslator.setOnClickListener {
            val textToTranslate = etIdiomOrigen.text.toString()
            if (textToTranslate.isNotEmpty()) {
                translateText(textToTranslate)
            } else {
                Toast.makeText(this, "Por favor ingrese texto para traducir", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun downloadTranslationModel() {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        spanishEnglishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Toast.makeText(this, "Modelo descargado exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al descargar el modelo: ${exception.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun translateText(text: String) {
        spanishEnglishTranslator.translate(text)
            .addOnSuccessListener { translatedText ->
                tvDesty.text = translatedText
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error en la traducción: ${exception.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        spanishEnglishTranslator.close()
    }
}