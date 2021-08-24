package com.citrus.ktordemo.ui.setup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.ktordemo.data.remote.api.Admin
import com.citrus.ktordemo.data.remote.api.ApiService
import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.repository.ApiRepository
import com.citrus.ktordemo.repository.SetupRepository
import com.citrus.ktordemo.util.Constants.MAX_ROOM_NAME_LENGTH
import com.citrus.ktordemo.util.Constants.MAX_USERNAME_LENGTH
import com.citrus.ktordemo.util.Constants.MIN_ROOM_NAME_LENGTH
import com.citrus.ktordemo.util.Constants.MIN_USERNAME_LENGTH
import com.citrus.ktordemo.util.DispatcherProvider
import com.citrus.ktordemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider,
    private val model:ApiRepository
) : ViewModel() {

    sealed class SetupEvent {
        object InputEmptyError : SetupEvent()
        object InputTooShortError : SetupEvent()
        object InputTooLongError : SetupEvent()

        data class NavigateToSelectRoomEvent(val userName: String) : SetupEvent()
    }

    /**one time event*/
    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent

    init {
        var para = Admin(3,"Enzo","Citrus")
        viewModelScope.launch {
            model.searchById("3").collect {
                Log.e("result",it.toString())
            }
         }

//        var liveDataStore = model.getStore(viewModelScope,para)
//        Log.e("test", liveDataStore.value!!.message)

    }



    fun validateUserNameAndNavigateToSelectRoom(userName: String){
        viewModelScope.launch(dispatchers.main) {
            val trimmedUserName = userName.trim()
            when{
                trimmedUserName.isEmpty() -> {
                    _setupEvent.emit(SetupEvent.InputEmptyError)
                }
                trimmedUserName.length < MIN_USERNAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooShortError)
                }
                trimmedUserName.length > MAX_USERNAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooLongError)
                }
                else -> _setupEvent.emit(SetupEvent.NavigateToSelectRoomEvent(userName))
            }
        }
    }

}