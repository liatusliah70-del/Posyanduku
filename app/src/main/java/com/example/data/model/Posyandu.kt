package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posyandu")
data class PosyanduEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kodePosyandu: String,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val rw: String = "01",
    val rt: String = "01",
    val alamatLengkap: String,
    val namaKetua: String,
    val namaKaderUtama: String,
    val kontakHp: String = "",
    val jumlahKader: Int = 5,
    val sasaranBalita: Int = 45,
    val sasaranBumil: Int = 12,
    val sasaranLansia: Int = 28,
    val totalSasaran: Int = 85,
    val statusAktif: Boolean = true,
    val strataPosyandu: String = "Mandiri", // Pratama, Madya, Purnama, Mandiri
    val hariBuka: String = "Senin Minggu ke-2",
    val createdAt: Long = System.currentTimeMillis()
)
