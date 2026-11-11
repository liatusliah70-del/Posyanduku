package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SpmKesehatanEntity
import com.example.data.model.SpmPekerjaanUmumEntity
import com.example.data.model.SpmPendidikanEntity
import com.example.data.model.SpmPerumahanEntity
import com.example.data.model.SpmSosialEntity
import com.example.data.model.SpmTrantibumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpmDao {
    // 1. Pendidikan
    @Query("SELECT * FROM spm_pendidikan ORDER BY id DESC")
    fun getAllPendidikan(): Flow<List<SpmPendidikanEntity>>

    @Query("SELECT * FROM spm_pendidikan WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getPendidikanByPosyandu(posyanduId: Long): Flow<List<SpmPendidikanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendidikan(entity: SpmPendidikanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPendidikan(list: List<SpmPendidikanEntity>)

    @Update
    suspend fun updatePendidikan(entity: SpmPendidikanEntity)

    @Query("DELETE FROM spm_pendidikan WHERE id = :id")
    suspend fun deletePendidikan(id: Long)

    @Query("SELECT COUNT(*) FROM spm_pendidikan")
    fun getPendidikanCount(): Flow<Int>

    // 2. Kesehatan
    @Query("SELECT * FROM spm_kesehatan ORDER BY id DESC")
    fun getAllKesehatan(): Flow<List<SpmKesehatanEntity>>

    @Query("SELECT * FROM spm_kesehatan WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getKesehatanByPosyandu(posyanduId: Long): Flow<List<SpmKesehatanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKesehatan(entity: SpmKesehatanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKesehatan(list: List<SpmKesehatanEntity>)

    @Update
    suspend fun updateKesehatan(entity: SpmKesehatanEntity)

    @Query("DELETE FROM spm_kesehatan WHERE id = :id")
    suspend fun deleteKesehatan(id: Long)

    @Query("SELECT COUNT(*) FROM spm_kesehatan")
    fun getKesehatanCount(): Flow<Int>

    // 3. Pekerjaan Umum
    @Query("SELECT * FROM spm_pekerjaan_umum ORDER BY id DESC")
    fun getAllPekerjaanUmum(): Flow<List<SpmPekerjaanUmumEntity>>

    @Query("SELECT * FROM spm_pekerjaan_umum WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getPekerjaanUmumByPosyandu(posyanduId: Long): Flow<List<SpmPekerjaanUmumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPekerjaanUmum(entity: SpmPekerjaanUmumEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPekerjaanUmum(list: List<SpmPekerjaanUmumEntity>)

    @Update
    suspend fun updatePekerjaanUmum(entity: SpmPekerjaanUmumEntity)

    @Query("DELETE FROM spm_pekerjaan_umum WHERE id = :id")
    suspend fun deletePekerjaanUmum(id: Long)

    @Query("SELECT COUNT(*) FROM spm_pekerjaan_umum")
    fun getPekerjaanUmumCount(): Flow<Int>

    // 4. Perumahan Rakyat
    @Query("SELECT * FROM spm_perumahan ORDER BY id DESC")
    fun getAllPerumahan(): Flow<List<SpmPerumahanEntity>>

    @Query("SELECT * FROM spm_perumahan WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getPerumahanByPosyandu(posyanduId: Long): Flow<List<SpmPerumahanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerumahan(entity: SpmPerumahanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPerumahan(list: List<SpmPerumahanEntity>)

    @Update
    suspend fun updatePerumahan(entity: SpmPerumahanEntity)

    @Query("DELETE FROM spm_perumahan WHERE id = :id")
    suspend fun deletePerumahan(id: Long)

    @Query("SELECT COUNT(*) FROM spm_perumahan")
    fun getPerumahanCount(): Flow<Int>

    // 5. Trantibumlinmas
    @Query("SELECT * FROM spm_trantibum ORDER BY id DESC")
    fun getAllTrantibum(): Flow<List<SpmTrantibumEntity>>

    @Query("SELECT * FROM spm_trantibum WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getTrantibumByPosyandu(posyanduId: Long): Flow<List<SpmTrantibumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrantibum(entity: SpmTrantibumEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTrantibum(list: List<SpmTrantibumEntity>)

    @Update
    suspend fun updateTrantibum(entity: SpmTrantibumEntity)

    @Query("DELETE FROM spm_trantibum WHERE id = :id")
    suspend fun deleteTrantibum(id: Long)

    @Query("SELECT COUNT(*) FROM spm_trantibum")
    fun getTrantibumCount(): Flow<Int>

    // 6. Sosial
    @Query("SELECT * FROM spm_sosial ORDER BY id DESC")
    fun getAllSosial(): Flow<List<SpmSosialEntity>>

    @Query("SELECT * FROM spm_sosial WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getSosialByPosyandu(posyanduId: Long): Flow<List<SpmSosialEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSosial(entity: SpmSosialEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSosial(list: List<SpmSosialEntity>)

    @Update
    suspend fun updateSosial(entity: SpmSosialEntity)

    @Query("DELETE FROM spm_sosial WHERE id = :id")
    suspend fun deleteSosial(id: Long)

    @Query("SELECT COUNT(*) FROM spm_sosial")
    fun getSosialCount(): Flow<Int>
}
