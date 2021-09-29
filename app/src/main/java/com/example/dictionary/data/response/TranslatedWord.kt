package com.example.dictionary.data.response

import com.google.gson.annotations.SerializedName

data class TranslatedWord(@SerializedName("translatedText")val wordAfterTranslate:String)