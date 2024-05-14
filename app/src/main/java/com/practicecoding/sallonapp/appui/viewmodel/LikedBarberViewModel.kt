package com.practicecoding.sallonapp.appui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicecoding.sallonapp.Graph
import com.practicecoding.sallonapp.data.LikedBarberRepo
import com.practicecoding.sallonapp.data.model.LikedBarber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
@HiltViewModel
class LikedBarberViewModel(
    private val lBRepository: LikedBarberRepo = Graph.likedBarberRepo
) : ViewModel() {

    lateinit var getAllLikedBarbers: Flow<List<LikedBarber>>

    init {
        viewModelScope.launch {
            getAllLikedBarbers = lBRepository.getAllLikedBarbers()
        }
    }

    fun addOrUpdateLikedBarber(likedBarber: LikedBarber) {
        viewModelScope.launch {
            lBRepository.insertLikedBarber(likedBarber)
        }
    }

    fun getLikedBarberByid(id: Int): Flow<LikedBarber> {
        return lBRepository.getLikedBarber(id)
    }

    fun getLikedBarberByUid(barberUid: String): Flow<LikedBarber> {
        return lBRepository.getLikedBarberByUid(barberUid)
    }

    fun deleteLikedBarber(likedBarber: LikedBarber) {
        viewModelScope.launch {
            lBRepository.deleteLikedBarber(likedBarber)
        }
    }

}