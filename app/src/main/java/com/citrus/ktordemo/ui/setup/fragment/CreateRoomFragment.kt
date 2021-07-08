package com.citrus.ktordemo.ui.setup.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.citrus.ktordemo.R
import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.databinding.FragmentCreateRoomBinding
import com.citrus.ktordemo.ui.setup.CreateRoomViewModel
import com.citrus.ktordemo.ui.setup.SetupViewModel
import com.citrus.ktordemo.util.Constants
import com.citrus.ktordemo.util.hideKeyBoard
import com.citrus.ktordemo.util.navigateSafely
import com.citrus.ktordemo.util.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CreateRoomFragment: Fragment(R.layout.fragment_create_room) {
    private var _binding: FragmentCreateRoomBinding? = null
    private val binding: FragmentCreateRoomBinding
        get() = _binding!!

    private val viewModel: CreateRoomViewModel by viewModels()
    private val args: CreateRoomFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateRoomBinding.bind(view)
        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setupRoomSizeSpinner()
        listenToEvent()

        binding.btnCreateRoom.setOnClickListener {

            binding.createRoomProgressBar.isVisible = true
            viewModel.createRoom(
                Room(
                    binding.etRoomName.text.toString(),
                    binding.tvMaxPersons.text.toString().toInt()
                )
            )
            requireActivity().hideKeyBoard(binding.root)
        }
    }

    private fun listenToEvent(){
        lifecycleScope.launchWhenStarted {
            viewModel.setupEvent.collect { event ->
                when(event) {
                    is CreateRoomViewModel.SetupEvent.CreateRoomEvent -> {
                        viewModel.joinRooms(args.username, event.room.name)
                    }
                    is CreateRoomViewModel.SetupEvent.InputEmptyError ->{
                        binding.createRoomProgressBar.isVisible = false
                        snackBar(R.string.error_field_empty)
                    }
                    is CreateRoomViewModel.SetupEvent.InputTooShortError -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackBar(getString(R.string.error_room_name_too_short,Constants.MIN_ROOM_NAME_LENGTH))
                    }
                    is CreateRoomViewModel.SetupEvent.InputTooLongError -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackBar(getString(R.string.error_room_name_too_long,Constants.MAX_ROOM_NAME_LENGTH))
                    }
                    is CreateRoomViewModel.SetupEvent.CreateRoomErrorEvent -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackBar(event.error)
                    }
                    is CreateRoomViewModel.SetupEvent.JoinRoomEvent -> {
                        binding.createRoomProgressBar.isVisible = false
                        findNavController().navigateSafely(
                            R.id.action_createRoomFragment_to_drawingActivity,
                            args = Bundle().apply {
                                putString("username",args.username)
                                putString("roomName",event.roomName)
                            }
                        )
                    }
                    is CreateRoomViewModel.SetupEvent.JoinRoomErrorEvent -> {
                        binding.createRoomProgressBar.isVisible = false
                        snackBar(event.error)
                    }
                }
            }
        }
    }

    private fun setupRoomSizeSpinner(){
        val roomSize = resources.getStringArray(R.array.room_size_array)
        val adapter = ArrayAdapter(requireContext(),R.layout.textview_room_size,roomSize)
        binding.tvMaxPersons.setAdapter(adapter)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}