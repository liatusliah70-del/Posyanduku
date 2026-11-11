package com.example.data.repository

import com.example.data.database.AppDatabase
import com.example.data.database.InitialDataSeeder
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PosyanduRepository(private val db: AppDatabase) {

    // Auth & Users
    val allUsers: Flow<List<UserEntity>> = db.userDao().getAllUsers()
    val totalUsersCount: Flow<Int> = db.userDao().getUserCount()
    val totalPetugasCount: Flow<Int> = db.userDao().getPetugasCount()

    suspend fun login(identifier: String, passwordAttempt: String): UserEntity? {
        val user = db.userDao().getUserByEmailOrUsername(identifier.trim())
        if (user != null && user.passwordHash == passwordAttempt.trim()) {
            logActivity(user, "LOGIN", "Pengguna berhasil masuk ke sistem Posyanduku")
            return user
        }
        return null
    }

    suspend fun insertUser(user: UserEntity, actor: UserEntity?) {
        db.userDao().insertUser(user)
        actor?.let { logActivity(it, "TAMBAH_PENGGUNA", "Menambahkan akun petugas ${user.fullName} (${user.email})") }
    }

    suspend fun updateUser(user: UserEntity, actor: UserEntity?) {
        db.userDao().updateUser(user)
        actor?.let { logActivity(it, "UBAH_PENGGUNA", "Memperbarui data akun ${user.fullName}") }
    }

    suspend fun deleteUser(id: String, actor: UserEntity?) {
        db.userDao().deleteUserById(id)
        actor?.let { logActivity(it, "HAPUS_PENGGUNA", "Menghapus akun petugas ID $id") }
    }

    suspend fun changePassword(userId: String, newPass: String, actor: UserEntity?) {
        val user = db.userDao().getUserById(userId)
        if (user != null) {
            val updated = user.copy(passwordHash = newPass)
            db.userDao().updateUser(updated)
            actor?.let { logActivity(it, "UBAH_PASSWORD", "Berhasil memperbarui kata sandi akun ${user.email}") }
        }
    }

    // Regions
    val allKecamatan: Flow<List<KecamatanEntity>> = db.regionDao().getAllKecamatan()
    val allDesa: Flow<List<DesaEntity>> = db.regionDao().getAllDesa()
    val totalKecamatanCount: Flow<Int> = db.regionDao().getKecamatanCount()
    val totalDesaCount: Flow<Int> = db.regionDao().getDesaCount()

    fun getDesaByKecamatan(kecamatanId: String): Flow<List<DesaEntity>> =
        db.regionDao().getDesaByKecamatan(kecamatanId)

    suspend fun insertKecamatan(kec: KecamatanEntity, actor: UserEntity?) {
        db.regionDao().insertKecamatan(kec)
        actor?.let { logActivity(it, "TAMBAH_KECAMATAN", "Menambahkan master kecamatan ${kec.namaKecamatan}") }
    }

    suspend fun insertDesa(desa: DesaEntity, actor: UserEntity?) {
        db.regionDao().insertDesa(desa)
        actor?.let { logActivity(it, "TAMBAH_DESA", "Menambahkan master desa ${desa.namaDesa} di Kec. ${desa.namaKecamatan}") }
    }

    suspend fun deleteKecamatan(id: String, actor: UserEntity?) {
        db.regionDao().deleteKecamatanById(id)
        actor?.let { logActivity(it, "HAPUS_KECAMATAN", "Menghapus kecamatan ID $id") }
    }

    suspend fun deleteDesa(id: String, actor: UserEntity?) {
        db.regionDao().deleteDesaById(id)
        actor?.let { logActivity(it, "HAPUS_DESA", "Menghapus desa ID $id") }
    }

    // Posyandu
    val allPosyandu: Flow<List<PosyanduEntity>> = db.posyanduDao().getAllPosyandu()
    val totalPosyanduCount: Flow<Int> = db.posyanduDao().getPosyanduCount()
    val totalSasaranSum: Flow<Int> = db.posyanduDao().getTotalSasaranSum()

    fun getPosyanduByKecamatan(kec: String): Flow<List<PosyanduEntity>> =
        db.posyanduDao().getPosyanduByKecamatan(kec)

    fun searchPosyandu(query: String): Flow<List<PosyanduEntity>> =
        db.posyanduDao().searchPosyandu(query)

    suspend fun insertPosyandu(posyandu: PosyanduEntity, actor: UserEntity?): Long {
        val id = db.posyanduDao().insertPosyandu(posyandu)
        actor?.let { logActivity(it, "TAMBAH_POSYANDU", "Menambahkan Posyandu baru: ${posyandu.namaPosyandu} di ${posyandu.desa}, ${posyandu.kecamatan}") }
        return id
    }

    suspend fun updatePosyandu(posyandu: PosyanduEntity, actor: UserEntity?) {
        db.posyanduDao().updatePosyandu(posyandu)
        actor?.let { logActivity(it, "UBAH_POSYANDU", "Memperbarui profil Posyandu: ${posyandu.namaPosyandu}") }
    }

    suspend fun deletePosyandu(id: Long, nama: String, actor: UserEntity?) {
        db.posyanduDao().deletePosyanduById(id)
        actor?.let { logActivity(it, "HAPUS_POSYANDU", "Menghapus data Posyandu $nama (ID: $id)") }
    }

    // SPM 1: Pendidikan
    val allPendidikan: Flow<List<SpmPendidikanEntity>> = db.spmDao().getAllPendidikan()
    val totalPendidikanCount: Flow<Int> = db.spmDao().getPendidikanCount()
    suspend fun insertPendidikan(entity: SpmPendidikanEntity, actor: UserEntity?): Long {
        val id = db.spmDao().insertPendidikan(entity)
        actor?.let { logActivity(it, "INPUT_SPM_PENDIDIKAN", "Pendataan anak ${entity.namaAnak} (${entity.jenisLayanan}) di ${entity.namaPosyandu}") }
        return id
    }
    suspend fun updatePendidikan(entity: SpmPendidikanEntity, actor: UserEntity?) {
        db.spmDao().updatePendidikan(entity)
        actor?.let { logActivity(it, "UBAH_SPM_PENDIDIKAN", "Memperbarui data pendidikan anak ${entity.namaAnak}") }
    }
    suspend fun deletePendidikan(id: Long, nama: String, actor: UserEntity?) {
        db.spmDao().deletePendidikan(id)
        actor?.let { logActivity(it, "HAPUS_SPM_PENDIDIKAN", "Menghapus data SPM Pendidikan: $nama") }
    }

    // SPM 2: Kesehatan
    val allKesehatan: Flow<List<SpmKesehatanEntity>> = db.spmDao().getAllKesehatan()
    val totalKesehatanCount: Flow<Int> = db.spmDao().getKesehatanCount()
    suspend fun insertKesehatan(entity: SpmKesehatanEntity, actor: UserEntity?): Long {
        val id = db.spmDao().insertKesehatan(entity)
        actor?.let { logActivity(it, "INPUT_SPM_KESEHATAN", "Pelayanan ${entity.jenisSasaran} an. ${entity.namaSasaran} di ${entity.namaPosyandu}") }
        return id
    }
    suspend fun updateKesehatan(entity: SpmKesehatanEntity, actor: UserEntity?) {
        db.spmDao().updateKesehatan(entity)
        actor?.let { logActivity(it, "UBAH_SPM_KESEHATAN", "Memperbarui data kesehatan an. ${entity.namaSasaran}") }
    }
    suspend fun deleteKesehatan(id: Long, nama: String, actor: UserEntity?) {
        db.spmDao().deleteKesehatan(id)
        actor?.let { logActivity(it, "HAPUS_SPM_KESEHATAN", "Menghapus data SPM Kesehatan: $nama") }
    }

    // SPM 3: Pekerjaan Umum
    val allPekerjaanUmum: Flow<List<SpmPekerjaanUmumEntity>> = db.spmDao().getAllPekerjaanUmum()
    val totalPekerjaanUmumCount: Flow<Int> = db.spmDao().getPekerjaanUmumCount()
    suspend fun insertPekerjaanUmum(entity: SpmPekerjaanUmumEntity, actor: UserEntity?): Long {
        val id = db.spmDao().insertPekerjaanUmum(entity)
        actor?.let { logActivity(it, "INPUT_SPM_PU", "Pendataan infrastruktur ${entity.lokasiDusunRwRt} (${entity.aksesAirMinum})") }
        return id
    }
    suspend fun updatePekerjaanUmum(entity: SpmPekerjaanUmumEntity, actor: UserEntity?) {
        db.spmDao().updatePekerjaanUmum(entity)
        actor?.let { logActivity(it, "UBAH_SPM_PU", "Memperbarui data infrastruktur ${entity.lokasiDusunRwRt}") }
    }
    suspend fun deletePekerjaanUmum(id: Long, nama: String, actor: UserEntity?) {
        db.spmDao().deletePekerjaanUmum(id)
        actor?.let { logActivity(it, "HAPUS_SPM_PU", "Menghapus data SPM PU: $nama") }
    }

    // SPM 4: Perumahan Rakyat
    val allPerumahan: Flow<List<SpmPerumahanEntity>> = db.spmDao().getAllPerumahan()
    val totalPerumahanCount: Flow<Int> = db.spmDao().getPerumahanCount()
    suspend fun insertPerumahan(entity: SpmPerumahanEntity, actor: UserEntity?): Long {
        val id = db.spmDao().insertPerumahan(entity)
        actor?.let { logActivity(it, "INPUT_SPM_PERUMAHAN", "Pendataan RTLH KK ${entity.namaKepalaKeluarga} (${entity.statusKelayakan})") }
        return id
    }
    suspend fun updatePerumahan(entity: SpmPerumahanEntity, actor: UserEntity?) {
        db.spmDao().updatePerumahan(entity)
        actor?.let { logActivity(it, "UBAH_SPM_PERUMAHAN", "Memperbarui data RTLH KK ${entity.namaKepalaKeluarga}") }
    }
    suspend fun deletePerumahan(id: Long, nama: String, actor: UserEntity?) {
        db.spmDao().deletePerumahan(id)
        actor?.let { logActivity(it, "HAPUS_SPM_PERUMAHAN", "Menghapus data SPM Perumahan KK: $nama") }
    }

    // SPM 5: Trantibumlinmas
    val allTrantibum: Flow<List<SpmTrantibumEntity>> = db.spmDao().getAllTrantibum()
    val totalTrantibumCount: Flow<Int> = db.spmDao().getTrantibumCount()
    suspend fun insertTrantibum(entity: SpmTrantibumEntity, actor: UserEntity?): Long {
        val id = db.spmDao().insertTrantibum(entity)
        actor?.let { logActivity(it, "INPUT_SPM_TRANTIBUM", "Pendataan Kamtibmas di ${entity.wilayahRtRw} (${entity.kondisiKeamanan})") }
        return id
    }
    suspend fun updateTrantibum(entity: SpmTrantibumEntity, actor: UserEntity?) {
        db.spmDao().updateTrantibum(entity)
        actor?.let { logActivity(it, "UBAH_SPM_TRANTIBUM", "Memperbarui data Kamtibmas di ${entity.wilayahRtRw}") }
    }
    suspend fun deleteTrantibum(id: Long, nama: String, actor: UserEntity?) {
        db.spmDao().deleteTrantibum(id)
        actor?.let { logActivity(it, "HAPUS_SPM_TRANTIBUM", "Menghapus data SPM Trantibum: $nama") }
    }

    // SPM 6: Sosial
    val allSosial: Flow<List<SpmSosialEntity>> = db.spmDao().getAllSosial()
    val totalSosialCount: Flow<Int> = db.spmDao().getSosialCount()
    suspend fun insertSosial(entity: SpmSosialEntity, actor: UserEntity?): Long {
        val id = db.spmDao().insertSosial(entity)
        actor?.let { logActivity(it, "INPUT_SPM_SOSIAL", "Pendataan sosial warga ${entity.namaWarga} (${entity.kategoriSosial})") }
        return id
    }
    suspend fun updateSosial(entity: SpmSosialEntity, actor: UserEntity?) {
        db.spmDao().updateSosial(entity)
        actor?.let { logActivity(it, "UBAH_SPM_SOSIAL", "Memperbarui data sosial warga ${entity.namaWarga}") }
    }
    suspend fun deleteSosial(id: Long, nama: String, actor: UserEntity?) {
        db.spmDao().deleteSosial(id)
        actor?.let { logActivity(it, "HAPUS_SPM_SOSIAL", "Menghapus data SPM Sosial warga: $nama") }
    }

    // Laporan & Verifikasi
    val allLaporan: Flow<List<LaporanEntity>> = db.laporanDao().getAllLaporan()
    val totalLaporanCount: Flow<Int> = db.laporanDao().getTotalLaporanCount()
    val verifiedLaporanCount: Flow<Int> = db.laporanDao().getVerifiedLaporanCount()
    val pendingLaporanCount: Flow<Int> = db.laporanDao().getPendingLaporanCount()
    val revisionLaporanCount: Flow<Int> = db.laporanDao().getRevisionLaporanCount()

    suspend fun insertLaporan(laporan: LaporanEntity, actor: UserEntity?): Long {
        val id = db.laporanDao().insertLaporan(laporan)
        actor?.let { logActivity(it, "KIRIM_LAPORAN", "Mengirimkan laporan ${laporan.nomorLaporan} - ${laporan.judulLaporan}") }
        return id
    }

    suspend fun updateLaporan(laporan: LaporanEntity, actor: UserEntity?) {
        db.laporanDao().updateLaporan(laporan)
        actor?.let { logActivity(it, "UPDATE_LAPORAN", "Memperbarui laporan ${laporan.nomorLaporan}") }
    }

    suspend fun verifikasiLaporan(
        laporanId: Long,
        status: StatusVerifikasi,
        catatan: String,
        verifikator: UserEntity
    ) {
        val existing = db.laporanDao().getLaporanById(laporanId)
        if (existing != null) {
            val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val updated = existing.copy(
                statusVerifikasi = status,
                catatanVerifikator = catatan,
                namaVerifikator = verifikator.fullName,
                tanggalVerifikasi = dateStr
            )
            db.laporanDao().updateLaporan(updated)
            logActivity(
                verifikator,
                "VERIFIKASI_LAPORAN",
                "Status laporan ${existing.nomorLaporan} diubah menjadi $status (${catatan.take(40)})"
            )
        }
    }

    suspend fun deleteLaporan(id: Long, nomor: String, actor: UserEntity?) {
        db.laporanDao().deleteLaporanById(id)
        actor?.let { logActivity(it, "HAPUS_LAPORAN", "Menghapus laporan $nomor") }
    }

    // Audit Log
    val recentAuditLogs: Flow<List<AuditLogEntity>> = db.auditLogDao().getRecentLogs()

    suspend fun logActivity(user: UserEntity, action: String, details: String) {
        val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val log = AuditLogEntity(
            waktuFormatted = dateStr,
            userEmail = user.email,
            userName = user.fullName,
            userRole = user.role.name,
            action = action,
            entityType = "Posyanduku",
            details = details
        )
        db.auditLogDao().insertLog(log)
    }

    suspend fun resetDatabaseToInitial(actor: UserEntity?) {
        InitialDataSeeder.seedDatabase(db)
        actor?.let { logActivity(it, "RESET_DATA", "Menginisialisasi ulang master data aplikasi Posyanduku") }
    }
}
