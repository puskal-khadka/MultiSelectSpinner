package com.puskal.multiselectspinnerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.puskal.multiselectspinnerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val testDataList = arrayListOf("Kotlin", "Java", "Python", "Php", "Swift")
        with(binding) {
            multiSelectSpinner.buildCheckedSpinner(testDataList){ selectedPositionList, displayString ->
                tvSelectedPosition.text = "Selected position:  $selectedPositionList"
                tvDispString.text = "Display String:  $displayString"
            }
        }

    }
}