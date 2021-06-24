package com.citrus.ktordemo.ui.setup.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.citrus.ktordemo.R
import com.citrus.ktordemo.databinding.FragmentUsernameBinding
import com.citrus.ktordemo.ui.setup.SetupViewModel
import com.citrus.ktordemo.ui.setup.UsernameViewModel
import com.citrus.ktordemo.util.Constants.MAX_USERNAME_LENGTH
import com.citrus.ktordemo.util.Constants.MIN_USERNAME_LENGTH
import com.citrus.ktordemo.util.navigateSafely
import com.citrus.ktordemo.util.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UsernameFragment : Fragment(R.layout.fragment_username) {
    private var _binding: FragmentUsernameBinding? = null
    private val binding: FragmentUsernameBinding
        get() = _binding!!

    private val viewModel: UsernameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUsernameBinding.bind(view)
        listenToEvent()

        binding.btnNext.setOnClickListener{
            viewModel.validateUserNameAndNavigateToSelectRoom(
                binding.etUsername.text.toString()
            )
        }
    }

    private fun listenToEvent(){
        lifecycleScope.launchWhenStarted {
            viewModel.setupEvent.collect{ event ->
                when(event) {
                    is UsernameViewModel.SetupEvent.NavigateToSelectRoomEvent -> {
                        findNavController().navigateSafely(
                           R.id.action_usernameFragment_to_selectRoomFragment,
                            args = Bundle().apply { putString("username",event.userName) }
                            )
                    }
                    is UsernameViewModel.SetupEvent.InputEmptyError -> {
                        snackBar(R.string.error_field_empty)
                    }
                    is UsernameViewModel.SetupEvent.InputTooShortError -> {
                        snackBar(getString(R.string.error_username_too_short, MIN_USERNAME_LENGTH))
                    }
                    UsernameViewModel.SetupEvent.InputTooLongError -> {
                        snackBar(getString(R.string.error_username_too_long, MAX_USERNAME_LENGTH))
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}