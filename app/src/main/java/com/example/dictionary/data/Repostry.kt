package com.example.dictionary.data

import com.example.dictionary.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Repostry {

    fun returnFlow(source: String, target: String, reqString: String) = flow<Status<TranslatedWord>>(){
        emit(Status.Loading("loading.."))
        emit(Client.getRequest(source,target,reqString))
    }.flowOn(Dispatchers.IO)
}