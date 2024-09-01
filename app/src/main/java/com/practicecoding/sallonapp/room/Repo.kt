package com.practicecoding.sallonapp.room

class Repo(
    private val likedBarberDao: Dao
) {
    //    private val likedBarberDao:Dao = TODO()
    suspend fun likeBarber(barberUid: String) {
        likedBarberDao.likeBarber(LikedBarber(barberUid))
    }

    suspend fun unlikeBarber(barberUid: String) {
        likedBarberDao.unlikeBarber(LikedBarber(barberUid))
    }

    fun isBarberLiked(barberUid: String): Boolean {
        val likedBarber = likedBarberDao.getLikedBarber(barberUid)
        return likedBarber != null
    }
    fun getLikedBarbers() = likedBarberDao.getLikedBarbers()
}