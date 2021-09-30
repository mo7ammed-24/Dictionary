package com.example.dictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.R
import com.example.dictionary.data.Status
import com.example.dictionary.data.response.LanguageArray
import com.example.dictionary.data.repostry.Repostry
import com.example.dictionary.data.response.TranslatedWord
import com.example.dictionary.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var sourceLanguage:String
    lateinit var targetLanguage:String
    lateinit var reqWordTranslation:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSpinnerLanguages()
        getReqWordTranslation()
    }

    // this function to set the item of spinner using api request
    private fun setSpinnerLanguages() {
        lifecycleScope.launch {
            Repostry.returnLanguagesArray().collect(::bindLanguageArray)
        }
    }

    // this function to bind the incoming data to spinner
    private fun bindLanguageArray(langStatus: Status<Array<LanguageArray>>){
        when(langStatus){
            is Status.Error -> Log.i("Hamada","Error in bind language")
            is Status.Loading -> Log.i("Hamada","Loading")
            is Status.Success -> setup(langStatus.data)
        }
    }

    //this function is setup the spinner adapter and it's values and set clickListener
    fun setup(myList: Array<LanguageArray>) {
        setSpinnerAdapter(myList)
        setSpinnerValue(myList)
    }

    private fun setSpinnerAdapter(mList: Array<LanguageArray>) {
        val myLangList= mutableListOf<String>()
        mList.forEach { myLangList.add(it.name)}
        val mAdapter = ArrayAdapter(this,R.layout.spinner_item,myLangList)
        binding!!.spinnerTranslateTo.apply {
            adapter = mAdapter
        }
    }

    private fun setSpinnerValue(myList: Array<LanguageArray>) {
        binding?.spinnerTranslateTo?.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                targetLanguage = myList[p2].code
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                targetLanguage = myList[0].code
            }
        }
    }

    // this function to get required word to translate
    private fun getReqWordTranslation() {
        binding?.textInput?.addTextChangedListener(object : TextWatcher {
            var isTyping = false
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("Hamada", "Before typing")}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        detectLanguage(p0)
            }

            var timer = Timer()
            var delay = 2000L

            override fun afterTextChanged(p0: Editable?) {
                if (!isTyping) {
                    isTyping = true
                }
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        isTyping = false
                        reqWordTranslation = p0.toString()
                        getTranslatedWord(reqWordTranslation)
                    } }, delay)

            }

        })

    }

    private fun detectLanguage(p0: CharSequence?) {
        lifecycleScope.launch {
            Repostry.returnLanguageSource(p0.toString()).collect {
                when(it){
                    is Status.Error -> Log.i("Hamada","inside")
                    is Status.Loading -> Log.i("Hamada","inside")
                    is Status.Success -> sourceLanguage = it.data
                }
            }
        }
    }

    //this function to translate required word
    private fun getTranslatedWord(reqString: String) {
        lifecycleScope.launch{
            Repostry.returnTranslationResult(sourceLanguage.toString(), targetLanguage.toString(), reqString).catch { Log.i("Hamada",it.message.toString()) }.collect(::bindDataResult)
        }
    }

    //this function to bind translate result state in textView
    private suspend fun bindDataResult(it: Status<TranslatedWord>) {
        withContext(Dispatchers.Main){
            when(it){
                is Status.Error -> binding?.textView?.text=it.errorMessage
                is Status.Loading -> binding?.textView?.text=it.loadingMessage
                is Status.Success -> binding?.textView?.text=it.data.wordAfterTranslate
            }
        }
    }

}