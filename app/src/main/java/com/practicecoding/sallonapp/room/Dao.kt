package com.practicecoding.sallonapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM likedbarberstable WHERE uid = :uid")
    fun getLikedBarber(uid: String): LikedBarber?

    @Query("SELECT * FROM likedbarberstable")
    fun getLikedBarbers(): Flow<List<LikedBarber>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun likeBarber(likedBarber: LikedBarber)

    @Delete
    suspend fun unlikeBarber(likedBarber: LikedBarber)
}