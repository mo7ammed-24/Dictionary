package com.example.dictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.dictionary.Constant
import com.example.dictionary.R
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.ui.fragment.TranslateFromFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var source:String
    lateinit var target:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        addCallbacks()
    }


    fun setup() {
        setSpinnerAdapter()
        setSpinnerValue()
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


    private fun setSpinnerAdapter() {
        val mAdapter = ArrayAdapter(this,R.layout.spinner_item,resources.getStringArray(R.array.language_option))
        binding!!.spinnerTranslateFrom.apply {
            adapter = mAdapter
        }
        val mAdapter2 = ArrayAdapter(this,R.layout.spinner_item,resources.getStringArray(R.array.language_option))
        binding!!.spinnerTranslateTo.apply {
            adapter = mAdapter2
        }
    }

    private fun setSpinnerValue() {
        binding?.spinnerTranslateFrom?.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                source = resources.getStringArray(R.array.language_option)[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                source = resources.getStringArray(R.array.language_option)[0]
            }
        }

        binding?.spinnerTranslateTo?.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                target = resources.getStringArray(R.array.language_option)[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                target = resources.getStringArray(R.array.language_option)[0]
            }
        }
    }
}