package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Bidang Pendidikan
@Entity(tableName = "spm_pendidikan")
data class SpmPendidikanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val tanggalInput: String,
    val namaAnak: String,
    val nik: String = "",
    val usiaTahun: Int,
    val kategoriUsia: String, // Anak Usia Dini (0-6), Usia Sekolah Dasar (7-12), Usia Menengah (13-18)
    val statusAkses: String,  // Bersekolah Aktif, Putus Sekolah, Belum Pernah Sekolah
    val jenisLayanan: String, // PAUD / Kober, TK / RA, SD / MI, SMP / MTs, SMA / SMK, Paket A/B/C
    val namaSekolah: String = "",
    val kebutuhanPendidikan: String, // Beasiswa, Bantuan Seragam/Buku, Fasilitas Transportasi, Pendampingan Khusus
    val catatanMonev: String = "",
    val dokumentasiFoto: String = "",
    val petugasInput: String = "Kader Posyandu",
    val createdAt: Long = System.currentTimeMillis()
)

// 2. Bidang Kesehatan
@Entity(tableName = "spm_kesehatan")
data class SpmKesehatanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val tanggalPelayanan: String,
    val namaSasaran: String,
    val nik: String = "",
    val namaOrangTuaSuami: String = "",
    val jenisSasaran: String, // Bayi/Balita, Ibu Hamil, Ibu Menyusui, Remaja Putri, WUS
    val usiaBulanTahun: String, // misal: "18 Bulan" atau "28 Tahun"
    val beratBadanKg: Double,
    val tinggiBadanCm: Double,
    val statusGizi: String, // Gizi Baik (Normal), Gizi Kurang, Risiko Stunting / Stunting, Gizi Lebih
    val statusImunisasi: String, // Lengkap, Sedang Berjalan, Belum Imunisasi
    val jenisImunisasiTerakhir: String = "Campak/Rubella",
    val pemberianPmtVitA: String = "PMT Pemulihan & Kapsul Vitamin A",
    val lingkarKepalaLilaCm: Double = 0.0,
    val catatanPelayanan: String = "",
    val dokumentasiFoto: String = "",
    val petugasInput: String = "Bidan Desa / Kader Posyandu",
    val createdAt: Long = System.currentTimeMillis()
)

// 3. Bidang Pekerjaan Umum
@Entity(tableName = "spm_pekerjaan_umum")
data class SpmPekerjaanUmumEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val tanggalInput: String,
    val lokasiDusunRwRt: String,
    val aksesAirMinum: String, // Jaringan Perpipaan PDAM, Sumur Bor Terlindungi, Sumur Gali Terlindung, Air Hujan, Tidak Layak
    val aksesSanitasiJamban: String, // Jamban Sehat Leher Angsa (Septic Tank), Jamban Komunal Sehat, Jamban Cemplung, BABS
    val kondisiDrainase: String, // Berfungsi Baik & Bersih, Tersumbat Sedimen/Sampah, Rusak Parah/Genangan
    val kondisiJalanLingkungan: String, // Rabat Beton / Aspal Baik, Paving Rusak Ringan, Jalan Tanah / Rusak Berat
    val kebutuhanSarprasPrioritas: String, // Pembangunan SPAM Air Bersih, Pembangunan Drainase Utama, Jamban Sehat Mandiri
    val deskripsiKondisi: String = "",
    val dokumentasiFoto: String = "",
    val petugasInput: String = "Kader Posyandu",
    val createdAt: Long = System.currentTimeMillis()
)

// 4. Bidang Perumahan Rakyat
@Entity(tableName = "spm_perumahan")
data class SpmPerumahanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val tanggalPendataan: String,
    val namaKepalaKeluarga: String,
    val nikKk: String = "",
    val alamatRumah: String,
    val statusKelayakan: String, // Rumah Layak Huni (RLH), RTLH Rusak Ringan, RTLH Rusak Sedang, RTLH Rusak Berat
    val kondisiAtap: String,     // Genteng Baik, Asbes/Seng Berkarat, Rumbia/Bocor Parah
    val kondisiDinding: String,  // Tembok Plester, Tembok Retak/Bata Telanjang, Bambu/Anyaman/Kayu Lapuk
    val kondisiLantai: String,   // Keramik/Ubin, Plester Semen Retak, Tanah Liar
    val ventilasiPencahayaan: String = "Cukup", // Cukup, Kurang, Tidak Ada
    val sanitasiDalamRumah: String = "Ada Jamban Sehat", // Ada Jamban Sehat, Jamban Tidak Standar, Menumpang
    val prioritasRehabilitasi: String, // Sangat Mendesak (Prioritas 1), Mendesak (Prioritas 2), Biasa
    val usulanBantuan: String = "BSPS / Rutilahu APBD",
    val dokumentasiFoto: String = "",
    val petugasInput: String = "Kader Posyandu",
    val createdAt: Long = System.currentTimeMillis()
)

// 5. Bidang Ketenteraman, Ketertiban Umum dan Perlindungan Masyarakat (Trantibumlinmas)
@Entity(tableName = "spm_trantibum")
data class SpmTrantibumEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val tanggalInput: String,
    val wilayahRtRw: String,
    val statusPosRonda: String, // Pos Ronda Aktif 24 Jam, Pos Ronda Aktif Sebagian, Tidak Ada Pos Ronda
    val jumlahAnggotaLinmas: Int = 4,
    val kondisiKeamanan: String, // Lingkungan Aman Kondusif, Rawan Pencurian/Tindak Kejahatan, Rawan Konflik Sosial
    val potensiKebencanaan: String, // Aman dari Bencana, Rawan Banjir / Rob Pasang Laut, Rawan Angin Kencang/Puting Beliung, Rawan Kekeringan
    val catatanKejadianBencana: String = "",
    val kebutuhanPerlindunganWarga: String, // Penerangan Jalan Lingkungan, Perlengkapan Satkamling, Sosialisasi Tanggap Bencana
    val dokumentasiFoto: String = "",
    val petugasInput: String = "Kader Posyandu & Satlinmas",
    val createdAt: Long = System.currentTimeMillis()
)

// 6. Bidang Sosial
@Entity(tableName = "spm_sosial")
data class SpmSosialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val posyanduId: Long,
    val namaPosyandu: String,
    val kecamatan: String,
    val desa: String,
    val tanggalPendataan: String,
    val namaWarga: String,
    val nik: String = "",
    val usiaTahun: Int,
    val kategoriSosial: String, // Lanjut Usia Terlantar / Rentan, Penyandang Disabilitas Fisik/Sensorik, Anak Yatim/Piatu Terlantar, Keluarga Miskin Ekstrem
    val jenisDisabilitas: String = "-", // Fisik, Netra, Rungu Wicara, Mental/Intelektual, Ganda, Tidak Ada
    val statusPenerimaanBansos: String, // Penerima PKH, Penerima BPNT/Sembako, BLT Dana Desa, Belum Menerima Bansos
    val kebutuhanPelayananSosial: String, // Alat Bantu (Kursi Roda/Kruk), Bantuan Pangan Sembako, Pendampingan Home Care, KIS/PBI JKN
    val catatanKondisi: String = "",
    val dokumentasiFoto: String = "",
    val petugasInput: String = "Kader Posyandu",
    val createdAt: Long = System.currentTimeMillis()
)
