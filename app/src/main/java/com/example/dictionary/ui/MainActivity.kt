package com.example.dictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.Constant
import com.example.dictionary.R
import com.example.dictionary.Status
import com.example.dictionary.data.LanguageCode
import com.example.dictionary.data.Languages
import com.example.dictionary.data.Repostry
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.ui.fragment.TranslateFromFragment
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var source:String
    lateinit var target:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLanguages()
        addCallbacks()
    }

    private fun setLanguages() {
        lifecycleScope.launch {
            Repostry.returnLanguagesFlow().catch { Log.i("Hamada",it.message.toString()) }.collect(::bindLanguage)
        }
    }

    private fun bindLanguage(langStatus: Status<Array<LanguageCode>>){
        when(langStatus){
            is Status.Error -> Log.i("Hamada","Error in bind language")
            is Status.Loading -> Log.i("Hamada","Loading")
            is Status.Success -> setup(langStatus.data)
        }
    }
    fun setup(myList: Array<LanguageCode>) {
        setSpinnerAdapter(myList)
        setSpinnerValue(myList)
    }

    fun addCallbacks() {
        binding?.startButton?.setOnClickListener {
            val fragmentTranslater= TranslateFromFragment()
            val bundle=Bundle()
            bundle.putString(Constant.SOURCE,source)
            bundle.putString(Constant.TARGET,target)
            fragmentTranslater.arguments=bundle
            hideViews()
            supportFragmentManager.beginTransaction().add(R.id.translate_fragment,fragmentTranslater).commit()
        }
    }

    private fun hideViews() {
        binding.spinnerTranslateFrom.visibility=View.GONE
        binding.spinnerTranslateTo.visibility=View.GONE
        binding.textSelectLanguageFrom.visibility=View.GONE
        binding.textSelectLanguageTo.visibility=View.GONE
        binding.textTranslateFrom.visibility=View.GONE
        binding.textTranslateTo.visibility=View.GONE
        binding.startButton.visibility=View.GONE
    }


    private fun setSpinnerAdapter(mList: Array<LanguageCode>) {
        val myLangList= mutableListOf<String>()
        mList.forEach { myLangList.add(it.name)}
        val mAdapter = ArrayAdapter(this,R.layout.spinner_item,myLangList)
        binding!!.spinnerTranslateFrom.apply {
            adapter = mAdapter
        }
        val mAdapter2 = ArrayAdapter(this,R.layout.spinner_item,myLangList)
        binding!!.spinnerTranslateTo.apply {
            adapter = mAdapter2
        }
    }

    private fun setSpinnerValue(myList: Array<LanguageCode>) {
        binding?.spinnerTranslateFrom?.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                source = myList[p2].code
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                source = myList[0].code
            }
        }

        binding?.spinnerTranslateTo?.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                target = myList[p2].code
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                target = myList[0].code
            }
        }
    }
}