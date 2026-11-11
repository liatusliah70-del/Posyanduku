package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StatusVerifikasi {
    MENUNGGU,
    DISETUJUI,
    PERLU_REVISI
}

enum class BidangSpm(val displayName: String) {
    PENDIDIKAN("Bidang Pendidikan"),
    KESEHATAN("Bidang Kesehatan"),
    PEKERJAAN_UMUM("Bidang Pekerjaan Umum"),
    PERUMAHAN_RAKYAT("Bidang Perumahan Rakyat"),
    TRANTIBUMLINMAS("Bidang Trantibumlinmas"),
    SOSIAL("Bidang Sosial")
}

@Entity(tableName = "laporan")
data class LaporanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nomorLaporan: String,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val bidangSpm: BidangSpm,
    val judulLaporan: String,
    val deskripsiCapaian: String,
    val jumlahSasaranTerlayani: Int,
    val targetSasaran: Int,
    val kendalaHambatan: String = "",
    val tindakLanjut: String = "",
    val periodeBulan: String, // e.g. "November"
    val periodeTahun: Int = 2026,
    val tanggalKirim: String,
    val statusVerifikasi: StatusVerifikasi = StatusVerifikasi.MENUNGGU,
    val catatanVerifikator: String = "",
    val namaVerifikator: String = "",
    val tanggalVerifikasi: String = "",
    val namaPetugasPengirim: String,
    val emailPetugasPengirim: String,
    val lampiranFoto: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
