package com.citrus.ktordemo.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.repository.SetupRepository
import com.citrus.ktordemo.util.DispatcherProvider
import com.citrus.ktordemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectRoomViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class SetupEvent {
        data class GetRoomEvent(val rooms: List<Room>) : SetupEvent()
        data class GetRoomErrorEvent(val error: String) : SetupEvent()
        object GetRoomLoadingEvent : SetupEvent()
        object GetRoomEmptyEvent : SetupEvent()

        data class JoinRoomEvent(val roomName: String) : SetupEvent()
        data class JoinRoomErrorEvent(val error: String) : SetupEvent()
    }


    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent

    /**store state*/
    private val _rooms = MutableStateFlow<SetupEvent>(SetupEvent.GetRoomEmptyEvent)
    val rooms: StateFlow<SetupEvent> = _rooms



    fun getRooms(searchQuery: String) {
        _rooms.value = SetupEvent.GetRoomLoadingEvent
        viewModelScope.launch(dispatchers.main) {
            val result = repository.getRoom(searchQuery)
            if(result is Resource.Success){
                _rooms.value = SetupEvent.GetRoomEvent(result.data ?: return@launch)
            }else{
                _setupEvent.emit(SetupEvent.GetRoomErrorEvent(
                    result.message ?: return@launch
                ))
            }
        }
    }

    fun joinRooms(userName: String, roomName: String) {
        viewModelScope.launch(dispatchers.main) {
            val result = repository.joinRoom(userName, roomName)
            if(result is Resource.Success){
                _setupEvent.emit(SetupEvent.JoinRoomEvent(roomName))
            }else{
                _setupEvent.emit(SetupEvent.JoinRoomErrorEvent(
                    result.message ?: return@launch
                ))
            }
        }
    }
}