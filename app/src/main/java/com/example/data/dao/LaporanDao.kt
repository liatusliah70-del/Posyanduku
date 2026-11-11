package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BidangSpm
import com.example.data.model.LaporanEntity
import com.example.data.model.StatusVerifikasi
import kotlinx.coroutines.flow.Flow

@Dao
interface LaporanDao {
    @Query("SELECT * FROM laporan ORDER BY id DESC")
    fun getAllLaporan(): Flow<List<LaporanEntity>>

    @Query("SELECT * FROM laporan WHERE statusVerifikasi = :status ORDER BY id DESC")
    fun getLaporanByStatus(status: StatusVerifikasi): Flow<List<LaporanEntity>>

    @Query("SELECT * FROM laporan WHERE posyanduId = :posyanduId ORDER BY id DESC")
    fun getLaporanByPosyandu(posyanduId: Long): Flow<List<LaporanEntity>>

    @Query("SELECT * FROM laporan WHERE kecamatan = :kecamatan ORDER BY id DESC")
    fun getLaporanByKecamatan(kecamatan: String): Flow<List<LaporanEntity>>

    @Query("SELECT * FROM laporan WHERE bidangSpm = :bidangSpm ORDER BY id DESC")
    fun getLaporanByBidangSpm(bidangSpm: BidangSpm): Flow<List<LaporanEntity>>

    @Query("SELECT * FROM laporan WHERE id = :id LIMIT 1")
    suspend fun getLaporanById(id: Long): LaporanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaporan(laporan: LaporanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLaporan(list: List<LaporanEntity>)

    @Update
    suspend fun updateLaporan(laporan: LaporanEntity)

    @Query("DELETE FROM laporan WHERE id = :id")
    suspend fun deleteLaporanById(id: Long)

    @Query("SELECT COUNT(*) FROM laporan")
    fun getTotalLaporanCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM laporan WHERE statusVerifikasi = 'DISETUJUI'")
    fun getVerifiedLaporanCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM laporan WHERE statusVerifikasi = 'MENUNGGU'")
    fun getPendingLaporanCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM laporan WHERE statusVerifikasi = 'PERLU_REVISI'")
    fun getRevisionLaporanCount(): Flow<Int>
}
