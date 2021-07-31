package com.example.finedustalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finedustalarm.databinding.ActivityMapBinding

class mapactivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}