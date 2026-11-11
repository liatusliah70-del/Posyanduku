package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kecamatan")
data class KecamatanEntity(
    @PrimaryKey val id: String,
    val namaKecamatan: String,
    val kodeKecamatan: String,
    val jumlahDesa: Int = 0,
    val camatName: String = ""
)

@Entity(tableName = "desa")
data class DesaEntity(
    @PrimaryKey val id: String,
    val kecamatanId: String,
    val namaKecamatan: String,
    val namaDesa: String,
    val jenis: String = "Desa", // Desa or Kelurahan
    val kepalaDesa: String = "",
    val kodePos: String = "45211"
)
