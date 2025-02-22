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
import java.util.Locale
import android.util.Log
import android.view.Menu
import android.widget.PopupMenu
import com.google.android.material.button.MaterialButton
import android.app.ProgressDialog

class MainActivity : AppCompatActivity() {

    lateinit var Et_Idiom_Origen:EditText
    lateinit var Tv_desty:TextView
    lateinit var Btn_Idiom_select:MaterialButton
    lateinit var Btn_Idiom_choise:MaterialButton
    lateinit var Btn_Traslator:MaterialButton
    private lateinit var progressDialog: ProgressDialog

    private lateinit var languageArrayList: ArrayList<Language>  // Cambié el nombre a 'Language'
    private val RECORDS = "My records"
    private val codeIdiomOrigen = "es"
    private val titleIdiomOrigen = "Español"
    private val targetLanguageCode = "en"
    private val titleTargetLanguage = "English"

    private lateinit var translateOptions: TranslatorOptions
    private lateinit var translator: Translator
    private var Origen_text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar lista de idiomas
        languagechoise()

        // Inicializar vistas
        Et_Idiom_Origen = findViewById(R.id.Et_Idiom_Origen)
        Tv_desty = findViewById(R.id.Tv_desty)
        Btn_Idiom_select = findViewById(R.id.Btb_Idiom_select)
        Btn_Idiom_choise = findViewById(R.id.Btb_Idiom_choise)
        Btn_Traslator = findViewById(R.id.Btn_Traslator)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCancelable(false)


        Btn_Idiom_choise.setOnClickListener {
            //Toast.makeText(this, "Idioma elegido", Toast.LENGTH_SHORT).show()
            ChoiseIdiomOrigen()
        }

        Btn_Idiom_select.setOnClickListener {
            //Toast.makeText(this, "Elegir idioma", Toast.LENGTH_SHORT).show()
            SelectTitleLanguage()
        }

        Btn_Traslator.setOnClickListener {
            //Toast.makeText(this, "Traducir", Toast.LENGTH_SHORT).show()
            ValidateData()
        }
    }


    private fun languagechoise() {
        languageArrayList = ArrayList()  // Inicialización correcta

        // Aquí debemos usar TranslateLanguage para obtener una lista de idiomas
        val listLanguages = TranslateLanguage.getAllLanguages()

        for (codeLanguage in listLanguages) {
            val titleLanguage = Locale(codeLanguage).displayLanguage
            //Log.d(RECORDS, "languagechoise: code_language $codeLanguage")
            //Log.d(RECORDS, "languagechoise: title_language $titleLanguage")

            // Asegúrate de tener la clase `Language` creada previamente
            val modelIdiom = Language(codeLanguage, titleLanguage)
            languageArrayList.add(modelIdiom)
        }
    }

    private fun ChoiseIdiomOrigen() {
        val popuMenu = PopupMenu(this, Btn_Idiom_choise)

        // Agregar los elementos al PopupMenu
        for (i in languageArrayList.indices) {  // Cambié la sintaxis del bucle
            popuMenu.menu.add(Menu.NONE, i, i, languageArrayList[i].titleLanguage)  // Acceso directo a la propiedad
        }

        // Mostrar el PopupMenu
        popuMenu.show()

        // Establecer el listener para los ítems del menú
        popuMenu.setOnMenuItemClickListener { item ->
            val position = item.itemId  // Usar item.itemId para obtener la posición

            val codeIdiomOrigen = languageArrayList[position].codeLanguage  // Acceso directo a la propiedad
            val titleIdiomOrigen = languageArrayList[position].titleLanguage  // Acceso directo a la propiedad

            // Actualizar los botones y el hint con el idioma seleccionado
            Btn_Idiom_choise.text = titleIdiomOrigen
            Et_Idiom_Origen.hint = "Enter text in: $titleIdiomOrigen"

            // Mostrar en el log el idioma elegido
            Log.d(RECORDS, "OnMenuItemClick: codeIdiomOrigen $codeIdiomOrigen")
            Log.d(RECORDS, "OnMenuItemClick: titleIdiomOrigen $titleIdiomOrigen")

            false
        }
    }

    private fun SelectTitleLanguage() {
        val popuMenu = PopupMenu(this, Btn_Idiom_select)

        // Agregar los elementos al PopupMenu
        for (i in languageArrayList.indices) {  // Cambié la sintaxis del bucle
            popuMenu.menu.add(Menu.NONE, i, i, languageArrayList[i].titleLanguage)  // Acceso directo a la propiedad
        }

        // Mostrar el PopupMenu
        popuMenu.show()

        // Establecer el listener para los ítems del menú
        popuMenu.setOnMenuItemClickListener { item ->
            val position = item.itemId  // Usar item.itemId para obtener la posición

            val targetLanguageCode = languageArrayList[position].codeLanguage  // Acceso directo a la propiedad
            val titleTargetLanguage = languageArrayList[position].titleLanguage  // Acceso directo a la propiedad

            // Actualizar los botones y el hint con el idioma seleccionado
            Btn_Idiom_select.text = titleTargetLanguage

            // Mostrar en el log el idioma elegido
            Log.d(RECORDS, "OnMenuItemClick: targetLanguageCode $targetLanguageCode")
            Log.d(RECORDS, "OnMenuItemClick: titleTargetLanguage $titleTargetLanguage")

            false
        }
    }

    private fun ValidateData() {
        Origen_text = Et_Idiom_Origen.text.toString().trim()
        Log.d(RECORDS, "ValidateData: Origen_text $Origen_text")
        if (Origen_text.isEmpty()){
            Toast.makeText(this, "Ingrese Texto", Toast.LENGTH_SHORT).show()
        } else{
            TranslateText()
        }

    }

    private fun TranslateText() {
        progressDialog.setMessage("Processing")
        progressDialog.show()

        translateOptions = TranslatorOptions.Builder()
            .setSourceLanguage(codeIdiomOrigen)
            .setTargetLanguage(targetLanguageCode)
            .build()

        translator = Translation.getClient(translateOptions)

        // Descargar el modelo de traducción
        val downloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener {
                Log.d(RECORDS, "onSuccess: The model has been downloaded successfully")
                progressDialog.setMessage("Translating text")
                translator.translate(Origen_text)
                    .addOnSuccessListener { translatedText ->
                        progressDialog.dismiss()
                        Tv_desty.text = translatedText
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Log.d(RECORDS, "onFailure: $e")
                        Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Log.d(RECORDS, "onFailure: $e")
                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
            }
    }
    data class Language(val codeLanguage: String, val titleLanguage: String)
}