package com.edgarjrm.traslator.Model

class Language(
    var codeLanguage: String,
    var titleIdiom: String
)

fun main() {
    val language = Language("en", "English")
    println(language.codeLanguage)  // Accede al código de idioma
    println(language.titleIdiom)    // Accede al título del idioma

    language.codeLanguage = "es"    // Cambia el código de idioma
    language.titleIdiom = "Spanish" // Cambia el título del idioma

    println(language.codeLanguage)  // "es"
    println(language.titleIdiom)    // "Spanish"
}