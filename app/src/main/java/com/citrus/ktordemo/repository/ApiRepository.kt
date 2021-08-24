package com.citrus.ktordemo.repository


import androidx.lifecycle.liveData
import com.citrus.ktordemo.data.remote.api.Admin
import com.citrus.ktordemo.data.remote.api.ApiService
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.sandwich.toLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val apiService: ApiService,
) {
    fun getStoreId(admin: Admin) = flow {
        apiService.getStoreId(admin).suspendOnSuccess {
            data?.let {
                if(data!!.successful == "true"){
                    emit(it.message)
                }
            }
        }
    }

    fun getStore(viewModelScope : CoroutineScope,admin: Admin) = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        emitSource(
            apiService.getStoreId(admin)
                .onError {
                    // handles error cases when the API request gets an error response.
                }.onException {
                    // handles exceptional cases when the API request gets an exception response.
                }.toLiveData()) // returns an observable LiveData
    }


    fun searchById(pid:String) = flow {
        apiService.searchById(pid).suspendOnSuccess {
            data?.let {
                emit(it)
            }
        }
    }
}