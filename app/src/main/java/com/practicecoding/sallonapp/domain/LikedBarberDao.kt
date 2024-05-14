package com.practicecoding.sallonapp.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.practicecoding.sallonapp.data.model.LikedBarber
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedBarberDao {
    @Query("SELECT * FROM LikedBarbersTable")
    fun getAll(): Flow<List<LikedBarber>>

    @Query("SELECT * FROM LikedBarbersTable WHERE id = :id")
    fun getLikedBarber(id: Int): Flow<LikedBarber>

    @Query("SELECT * FROM LikedBarbersTable WHERE barberUid = :barberUid")
    fun getLikedBarberByUid(barberUid: String): Flow<LikedBarber>

    @Upsert
    suspend fun insertAll(vararg likedBarber: LikedBarber)

    @Delete
    suspend fun delete(likedBarber: LikedBarber)
}