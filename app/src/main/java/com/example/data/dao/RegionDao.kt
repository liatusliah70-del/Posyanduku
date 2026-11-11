package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DesaEntity
import com.example.data.model.KecamatanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {
    @Query("SELECT * FROM kecamatan ORDER BY namaKecamatan ASC")
    fun getAllKecamatan(): Flow<List<KecamatanEntity>>

    @Query("SELECT * FROM desa ORDER BY namaDesa ASC")
    fun getAllDesa(): Flow<List<DesaEntity>>

    @Query("SELECT * FROM desa WHERE kecamatanId = :kecamatanId ORDER BY namaDesa ASC")
    fun getDesaByKecamatan(kecamatanId: String): Flow<List<DesaEntity>>

    @Query("SELECT * FROM desa WHERE namaKecamatan = :namaKecamatan ORDER BY namaDesa ASC")
    fun getDesaByNamaKecamatan(namaKecamatan: String): Flow<List<DesaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKecamatan(kecamatan: KecamatanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKecamatan(list: List<KecamatanEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDesa(desa: DesaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDesa(list: List<DesaEntity>)

    @Update
    suspend fun updateKecamatan(kecamatan: KecamatanEntity)

    @Update
    suspend fun updateDesa(desa: DesaEntity)

    @Query("DELETE FROM kecamatan WHERE id = :id")
    suspend fun deleteKecamatanById(id: String)

    @Query("DELETE FROM desa WHERE id = :id")
    suspend fun deleteDesaById(id: String)

    @Query("SELECT COUNT(*) FROM kecamatan")
    fun getKecamatanCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM desa")
    fun getDesaCount(): Flow<Int>
}
