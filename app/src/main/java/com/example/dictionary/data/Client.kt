package com.example.dictionary.data

import android.util.Log
import com.example.dictionary.Status
import com.google.gson.Gson
import okhttp3.*

object Client {

    fun getRequest(source: String, target: String, reqString: String): Status<TranslatedWord> {
        val clint= OkHttpClient()
        Log.i("Hamada","$reqString - $source - $target")
        val url="https://translate.argosopentech.com/translate?q=$reqString&source=$source&target=$target"
        val formBody=FormBody.Builder().build()
        Log.i("Hamada","before execute")
        val request=Request.Builder().url(url).post(formBody).build()
        val response=clint.newCall(request).execute()
        return if(response.isSuccessful){
            val translatedObject= Gson().fromJson(response.body?.string(),TranslatedWord::class.java)
            Status.Success(translatedObject)
        }
        else{
            Status.Error(response.message)
        }
    }

    fun getLaguage(): Status<Array<LanguageCode>> {
        val client=OkHttpClient()
        val request=Request.Builder().url("https://translate.argosopentech.com/languages").build()
        val response=client.newCall(request).execute()
        return if (response.isSuccessful){
            Log.i("Hamada","inside success")
            val languageObject=Gson().fromJson(response.body?.string(),Array<LanguageCode>::class.java)
            Status.Success(languageObject)
        }
        else{
            Status.Error(response.message)
        }
    }


}