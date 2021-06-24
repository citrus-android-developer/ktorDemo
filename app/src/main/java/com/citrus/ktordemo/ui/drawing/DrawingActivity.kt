package com.citrus.ktordemo.ui.drawing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.citrus.ktordemo.databinding.ActivityDrawingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawingBinding

    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeToUiStateUpdate()

        binding.colorGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.checkRadioButton(checkedId)
        }
    }

    private fun subscribeToUiStateUpdate() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedColorButtonId.collect { id ->
                binding.colorGroup.check(id)
            }
        }
    }
}