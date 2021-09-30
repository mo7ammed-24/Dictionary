package com.example.dictionary.data.network

import com.example.dictionary.data.Status
import com.example.dictionary.data.response.AutoDetectLanguage
import com.example.dictionary.data.response.LanguageArray
import com.example.dictionary.data.response.TranslatedWord
import com.google.gson.Gson
import okhttp3.*

object Client {
     //function to send request with required word to translate it
    fun sendTranslateRequest(langSource: String, langTarget: String, requiredWord: String): Status<TranslatedWord> {
        val clint= OkHttpClient()
        val url="https://translate.argosopentech.com/translate?q=$requiredWord&source=$langSource&target=$langTarget"
        val formBody=FormBody.Builder().build()
        val request=Request.Builder().url(url).post(formBody).build()
        val response=clint.newCall(request).execute()
        return if(response.isSuccessful){
            val translatedObject= Gson().fromJson(response.body?.string(), TranslatedWord::class.java)
            Status.Success(translatedObject)
        }
        else{
            Status.Error(response.message)
        }
    }
    // function that send request to generate the array of available language and it's code
    fun langArrayRequest(): Status<Array<LanguageArray>> {
        val client=OkHttpClient()
        val request=Request.Builder().url("https://translate.argosopentech.com/languages").build()
        val response=client.newCall(request).execute()
        return if (response.isSuccessful){
            val languageArrayObject=Gson().fromJson(response.body?.string(),Array<LanguageArray>::class.java)
            Status.Success(languageArrayObject)
        }
        else{
            Status.Error(response.message)
        }
    }

    // this is function to send request to detect language by send string of entered chars
    fun detectLanguageRequest(wordSegment:String): Status<String> {
        val url="https://translate.argosopentech.com/detect?q=$wordSegment"
        val client=OkHttpClient()
        val request=Request.Builder().url(url).post(FormBody.Builder().build()).build()
        val response=client.newCall(request).execute()
        return if(response.isSuccessful){
            val detectLanguageObject=Gson().fromJson(response.body?.string(),Array<AutoDetectLanguage>::class.java)
            Status.Success(detectLanguageObject[0].language)
        }
        else{
            Status.Error(response.message)
        }
    }


}