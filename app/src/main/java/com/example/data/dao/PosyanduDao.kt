package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PosyanduEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PosyanduDao {
    @Query("SELECT * FROM posyandu ORDER BY namaPosyandu ASC")
    fun getAllPosyandu(): Flow<List<PosyanduEntity>>

    @Query("SELECT * FROM posyandu WHERE kecamatan = :kecamatan ORDER BY namaPosyandu ASC")
    fun getPosyanduByKecamatan(kecamatan: String): Flow<List<PosyanduEntity>>

    @Query("SELECT * FROM posyandu WHERE desa = :desa ORDER BY namaPosyandu ASC")
    fun getPosyanduByDesa(desa: String): Flow<List<PosyanduEntity>>

    @Query("SELECT * FROM posyandu WHERE id = :id LIMIT 1")
    suspend fun getPosyanduById(id: Long): PosyanduEntity?

    @Query("SELECT * FROM posyandu WHERE namaPosyandu LIKE '%' || :query || '%' OR kecamatan LIKE '%' || :query || '%' OR desa LIKE '%' || :query || '%'")
    fun searchPosyandu(query: String): Flow<List<PosyanduEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosyandu(posyandu: PosyanduEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosyandu(list: List<PosyanduEntity>)

    @Update
    suspend fun updatePosyandu(posyandu: PosyanduEntity)

    @Query("DELETE FROM posyandu WHERE id = :id")
    suspend fun deletePosyanduById(id: Long)

    @Query("SELECT COUNT(*) FROM posyandu")
    fun getPosyanduCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(totalSasaran), 0) FROM posyandu")
    fun getTotalSasaranSum(): Flow<Int>
}
