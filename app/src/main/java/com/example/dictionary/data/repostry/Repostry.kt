package com.example.dictionary.data.repostry

import com.example.dictionary.data.Status
import com.example.dictionary.data.response.LanguageArray
import com.example.dictionary.data.response.TranslatedWord
import com.example.dictionary.data.network.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Repostry {
    //this function work to emmit the state of TranslatedWord using Flow
    fun returnTranslationResult(source: String, target: String, reqString: String) = flow<Status<TranslatedWord>>(){
        emit(Status.Loading("loading.."))
        emit(Client.sendTranslateRequest(source,target,reqString))
    }.flowOn(Dispatchers.IO)

    //this function emmit the language array state using flow for each State
    fun returnLanguagesArray()= flow<Status<Array<LanguageArray>>> {
        emit(Status.Loading("Loading.."))
        emit(Client.langArrayRequest())
    }.flowOn(Dispatchers.IO)

    //this function to emit the language source state  using auto detect request
    fun returnLanguageSource(wordSegment: String) = flow<Status<String>>{
        emit(Status.Loading("Loading"))
        emit(Client.detectLanguageRequest(wordSegment))}.flowOn(Dispatchers.IO)
}