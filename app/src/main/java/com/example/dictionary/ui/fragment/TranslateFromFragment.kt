package com.example.dictionary.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.Constant
import com.example.dictionary.Status
import com.example.dictionary.data.Repostry
import com.example.dictionary.data.TranslatedWord
import com.example.dictionary.databinding.FragmentFromTranslateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TranslateFromFragment:BaseFragment<FragmentFromTranslateBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFromTranslateBinding=FragmentFromTranslateBinding::inflate
     lateinit var source:String
     lateinit var target:String
     lateinit var reqString:String
    override fun onStart() {
        super.onStart()
        arguments?.let {
            source=it.getString(Constant.SOURCE).toString()
            target=it.getString(Constant.TARGET).toString()
        }
    }
    override fun setup() {
    }

    override fun addCallbacks() {
        binding?.textInput?.addTextChangedListener(object : TextWatcher {
            var isTyping = false

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("Hamada", "Before typing")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("Hamada", "On typing")
            }

            var timer = Timer()
            var delay = 1500L

            override fun afterTextChanged(p0: Editable?) {
                if (!isTyping) {
                    isTyping = true
                }
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        isTyping = false
                        reqString = p0.toString()
                        getTranslatedWord(reqString)
                    }

                }, delay)
            }

        })


    }

    private fun getTranslatedWord(reqString: String) {
        lifecycleScope.launch{
            Repostry.returnFlow(source.toString(), target.toString(), reqString).catch { Log.i("Hamada",it.message.toString()) }.collect(::bindData)
        }
    }

    suspend fun bindData(it: Status<TranslatedWord>) {
        withContext(Dispatchers.Main){
            when(it){
                is Status.Error -> binding?.textView?.text=it.errorMessage
                is Status.Loading -> binding?.textView?.text=it.loadingMessage
                is Status.Success -> {Log.i("Hamada",it.data.wordAfterTranslate)
                    binding?.textView?.text=it.data.wordAfterTranslate}
            }
        }
    }



    }