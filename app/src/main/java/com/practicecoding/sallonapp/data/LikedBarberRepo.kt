package com.practicecoding.sallonapp.data

import com.practicecoding.sallonapp.data.model.LikedBarber
import com.practicecoding.sallonapp.domain.LikedBarberDao
import kotlinx.coroutines.flow.Flow

class LikedBarberRepo(
    private val likedBarberDao: LikedBarberDao
) {
    fun getAllLikedBarbers(): Flow<List<LikedBarber>> = likedBarberDao.getAll()

    fun getLikedBarber(id: Int): Flow<LikedBarber> = likedBarberDao.getLikedBarber(id)

    fun getLikedBarberByUid(barberUid: String): Flow<LikedBarber> = likedBarberDao.getLikedBarberByUid(barberUid)

    suspend fun insertLikedBarber(likedBarber: LikedBarber) {
        likedBarberDao.insertAll(likedBarber)
    }

    suspend fun deleteLikedBarber(likedBarber: LikedBarber) {
        likedBarberDao.delete(likedBarber)
    }
}