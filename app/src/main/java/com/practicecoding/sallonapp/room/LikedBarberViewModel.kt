package com.practicecoding.sallonapp.room

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LikedBarberViewModel (
//    private val repository: Repo
    context: Context
) : ViewModel( ) {
    private  var repo: Repo
    init {
        val barberDao = Graph.provide(context).likedBarberDao()
        repo=Repo(barberDao)

    }
    fun likeBarber(barberUid: String) {
        viewModelScope.launch {
            repo.likeBarber(barberUid)
        }
    }

    fun unlikeBarber(barberUid: String) {
        viewModelScope.launch {
            repo.unlikeBarber(barberUid)
        }
    }

    fun isBarberLiked(barberUid: String): Boolean {
        return repo.isBarberLiked(barberUid)
    }

//    fun getLikedBarberByid(id: Int): Flow<LikedBarber> {
//        return lBRepository.getLikedBarber(id)
//    }
//
//    fun getLikedBarberByUid(barberUid: String): Flow<LikedBarber> {
//        return lBRepository.getLikedBarberByUid(barberUid)
//    }
//
//    fun deleteLikedBarber(likedBarber: LikedBarber) {
//        viewModelScope.launch {
//            lBRepository.deleteLikedBarber(likedBarber)
//        }
//    }

}