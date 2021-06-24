package com.citrus.ktordemo.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.repository.SetupRepository
import com.citrus.ktordemo.util.Constants.MAX_ROOM_NAME_LENGTH
import com.citrus.ktordemo.util.Constants.MAX_USERNAME_LENGTH
import com.citrus.ktordemo.util.Constants.MIN_ROOM_NAME_LENGTH
import com.citrus.ktordemo.util.Constants.MIN_USERNAME_LENGTH
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
class SetupViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class SetupEvent {
        object InputEmptyError : SetupEvent()
        object InputTooShortError : SetupEvent()
        object InputTooLongError : SetupEvent()

        data class CreateRoomEvent(val room: Room) : SetupEvent()
        data class CreateRoomErrorEvent(val error: String) : SetupEvent()

        data class NavigateToSelectRoomEvent(val userName: String) : SetupEvent()

        data class GetRoomEvent(val rooms: List<Room>) : SetupEvent()
        data class GetRoomErrorEvent(val error: String) : SetupEvent()
        object GetRoomLoadingEvent : SetupEvent()
        object GetRoomEmptyEvent : SetupEvent()

        data class JoinRoomEvent(val roomName: String) : SetupEvent()
        data class JoinRoomErrorEvent(val error: String) : SetupEvent()
    }

    /**one time event*/
    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent

    /**store state*/
    private val _rooms = MutableStateFlow<SetupEvent>(SetupEvent.GetRoomEmptyEvent)
    val rooms: StateFlow<SetupEvent> = _rooms

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

    fun createRoom(room:Room){
        viewModelScope.launch(dispatchers.main) {
            val trimmedRoomName = room.name.trim()
            when {
                trimmedRoomName.isEmpty() -> {
                    _setupEvent.emit(SetupEvent.InputEmptyError)
                }
                trimmedRoomName.length < MIN_ROOM_NAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooShortError)
                }
                trimmedRoomName.length > MAX_ROOM_NAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooLongError)
                }
                else -> {
                    val result = repository.createRoom(room)
                    if(result is Resource.Success){
                        _setupEvent.emit(SetupEvent.CreateRoomEvent(room))
                    } else {
                        _setupEvent.emit(SetupEvent.CreateRoomErrorEvent(
                            result.message ?: return@launch
                        ))
                    }
                }
            }
        }
    }

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