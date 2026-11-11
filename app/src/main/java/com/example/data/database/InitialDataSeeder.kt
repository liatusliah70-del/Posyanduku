package com.example.data.database

import com.example.data.model.*

object InitialDataSeeder {
    suspend fun seedDatabase(db: AppDatabase) {
        // 1. Seed Kecamatan di Kabupaten Indramayu
        val kecList = listOf(
            KecamatanEntity("KEC_01", "Indramayu", "32.12.01", 18, "Drs. H. Aep Surahman, M.Si"),
            KecamatanEntity("KEC_02", "Sindang", "32.12.02", 10, "Hj. Siti Nurjanah, S.Sos"),
            KecamatanEntity("KEC_03", "Jatibarang", "32.12.03", 15, "Dedi Supriyadi, S.STP"),
            KecamatanEntity("KEC_04", "Balongan", "32.12.04", 10, "Opik Hidayat, M.Si"),
            KecamatanEntity("KEC_05", "Karangampel", "32.12.05", 11, "Drs. Ade Sukma"),
            KecamatanEntity("KEC_06", "Haurgeulis", "32.12.06", 10, "Komarudin, S.IP"),
            KecamatanEntity("KEC_07", "Cantigi", "32.12.07", 7, "Wawan Gunawan, SE"),
            KecamatanEntity("KEC_08", "Losarang", "32.12.08", 12, "H. Boy Billy Prima, S.STP")
        )
        db.regionDao().insertAllKecamatan(kecList)

        // 2. Seed Desa/Kelurahan
        val desaList = listOf(
            DesaEntity("DESA_01", "KEC_01", "Indramayu", "Margadadi", "Kelurahan", "H. Sunaryo, S.Sos"),
            DesaEntity("DESA_02", "KEC_01", "Indramayu", "Lemahmekar", "Kelurahan", "Siti Aminah, SE"),
            DesaEntity("DESA_03", "KEC_01", "Indramayu", "Paoman", "Kelurahan", "Ahmad Fauzi, S.Pd"),
            DesaEntity("DESA_04", "KEC_01", "Indramayu", "Karanganyar", "Kelurahan", "Budi Santoso, ST"),
            DesaEntity("DESA_05", "KEC_02", "Sindang", "Dermayu", "Desa", "Wasmin, S.IP"),
            DesaEntity("DESA_06", "KEC_02", "Sindang", "Kenanga", "Desa", "H. Caswita"),
            DesaEntity("DESA_07", "KEC_03", "Jatibarang", "Jatibarang", "Desa", "Kurniawan, SE"),
            DesaEntity("DESA_08", "KEC_03", "Jatibarang", "Bulak", "Desa", "Suharto"),
            DesaEntity("DESA_09", "KEC_04", "Balongan", "Sukaurip", "Desa", "Taryono, S.Sos"),
            DesaEntity("DESA_10", "KEC_05", "Karangampel", "Karangampel Kidul", "Desa", "H. Mastur")
        )
        db.regionDao().insertAllDesa(desaList)

        // 3. Seed Users
        val users = listOf(
            UserEntity(
                id = "USR_ADMIN",
                email = "admin@indramayukab.go.id",
                username = "admin_dpmd",
                fullName = "Admin DPMD Indramayu",
                role = UserRole.ADMIN,
                phone = "081223344550",
                nip = "19850712 201001 1 008",
                jabatan = "Pranata Komputer & Administrator SPM DPMD",
                passwordHash = "indramayu2026"
            ),
            UserEntity(
                id = "USR_PETUGAS_1",
                email = "petugas@indramayukab.go.id",
                username = "kader_melati",
                fullName = "Siti Rohmah, A.Md.Keb",
                role = UserRole.PETUGAS,
                assignedKecamatan = "Indramayu",
                assignedDesa = "Margadadi",
                assignedPosyanduId = 1L,
                phone = "085295112233",
                nip = "-",
                jabatan = "Ketua Kader Posyandu Melati I",
                passwordHash = "posyandu2026"
            ),
            UserEntity(
                id = "USR_PETUGAS_2",
                email = "dewi.mawar@indramayukab.go.id",
                username = "dewi_kader",
                fullName = "Dewi Sartika, S.Kep",
                role = UserRole.PETUGAS,
                assignedKecamatan = "Indramayu",
                assignedDesa = "Karanganyar",
                assignedPosyanduId = 2L,
                phone = "087722119988",
                nip = "-",
                jabatan = "Kader Posyandu Mawar II",
                passwordHash = "posyandu2026"
            ),
            UserEntity(
                id = "USR_PETUGAS_3",
                email = "neneng.jatibarang@indramayukab.go.id",
                username = "neneng_jatibarang",
                fullName = "Neneng Hasanah",
                role = UserRole.PETUGAS,
                assignedKecamatan = "Jatibarang",
                assignedDesa = "Jatibarang",
                assignedPosyanduId = 3L,
                phone = "081394556677",
                nip = "-",
                jabatan = "Ketua Kader Posyandu Teratai",
                passwordHash = "posyandu2026"
            )
        )
        db.userDao().insertUsers(users)

        // 4. Seed Master Data Posyandu
        val posyanduList = listOf(
            PosyanduEntity(
                id = 1L,
                kodePosyandu = "POS-IMY-001",
                namaPosyandu = "Posyandu Melati I",
                kecamatan = "Indramayu",
                desa = "Margadadi",
                rw = "02",
                rt = "04",
                alamatLengkap = "Jl. Letnan Sutejo No. 14, RT 04 RW 02 Kel. Margadadi",
                namaKetua = "Siti Rohmah, A.Md.Keb",
                namaKaderUtama = "Ibu Carsiti & Ibu Casmi",
                kontakHp = "085295112233",
                jumlahKader = 6,
                sasaranBalita = 58,
                sasaranBumil = 14,
                sasaranLansia = 32,
                totalSasaran = 104,
                statusAktif = true,
                strataPosyandu = "Mandiri",
                hariBuka = "Selasa Minggu ke-2"
            ),
            PosyanduEntity(
                id = 2L,
                kodePosyandu = "POS-IMY-002",
                namaPosyandu = "Posyandu Mawar II",
                kecamatan = "Indramayu",
                desa = "Karanganyar",
                rw = "03",
                rt = "01",
                alamatLengkap = "Gang Melati RT 01 RW 03 Kel. Karanganyar",
                namaKetua = "Dewi Sartika, S.Kep",
                namaKaderUtama = "Ibu Tarinah",
                kontakHp = "087722119988",
                jumlahKader = 5,
                sasaranBalita = 45,
                sasaranBumil = 9,
                sasaranLansia = 24,
                totalSasaran = 78,
                statusAktif = true,
                strataPosyandu = "Purnama",
                hariBuka = "Rabu Minggu ke-1"
            ),
            PosyanduEntity(
                id = 3L,
                kodePosyandu = "POS-JTB-001",
                namaPosyandu = "Posyandu Teratai",
                kecamatan = "Jatibarang",
                desa = "Jatibarang",
                rw = "01",
                rt = "03",
                alamatLengkap = "Jl. Mayor Dasuki No. 88 Desa Jatibarang",
                namaKetua = "Neneng Hasanah",
                namaKaderUtama = "Ibu Rodiah & Ibu Euis",
                kontakHp = "081394556677",
                jumlahKader = 5,
                sasaranBalita = 62,
                sasaranBumil = 16,
                sasaranLansia = 35,
                totalSasaran = 113,
                statusAktif = true,
                strataPosyandu = "Mandiri",
                hariBuka = "Kamis Minggu ke-2"
            ),
            PosyanduEntity(
                id = 4L,
                kodePosyandu = "POS-SDG-001",
                namaPosyandu = "Posyandu Dahlia III",
                kecamatan = "Sindang",
                desa = "Kenanga",
                rw = "04",
                rt = "02",
                alamatLengkap = "Blok Kenanga RT 02 RW 04 Desa Kenanga",
                namaKetua = "Waryati",
                namaKaderUtama = "Ibu Dasinah",
                kontakHp = "082119988776",
                jumlahKader = 5,
                sasaranBalita = 50,
                sasaranBumil = 11,
                sasaranLansia = 30,
                totalSasaran = 91,
                statusAktif = true,
                strataPosyandu = "Purnama",
                hariBuka = "Senin Minggu ke-3"
            ),
            PosyanduEntity(
                id = 5L,
                kodePosyandu = "POS-BLG-001",
                namaPosyandu = "Posyandu Cempaka",
                kecamatan = "Balongan",
                desa = "Sukaurip",
                rw = "01",
                rt = "01",
                alamatLengkap = "Blok Kesepuhan RT 01 RW 01 Desa Sukaurip",
                namaKetua = "Rasminah",
                namaKaderUtama = "Ibu Carsini",
                kontakHp = "081987654321",
                jumlahKader = 5,
                sasaranBalita = 40,
                sasaranBumil = 8,
                sasaranLansia = 22,
                totalSasaran = 70,
                statusAktif = true,
                strataPosyandu = "Madya",
                hariBuka = "Sabtu Minggu ke-1"
            )
        )
        db.posyanduDao().insertAllPosyandu(posyanduList)

        // 5. Seed 6 SPM Module Records
        // SPM 1: Pendidikan
        db.spmDao().insertAllPendidikan(
            listOf(
                SpmPendidikanEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalInput = "10/11/2026",
                    namaAnak = "Muhammad Al-Ghifari",
                    nik = "3212010809210002",
                    usiaTahun = 4,
                    kategoriUsia = "Anak Usia Dini (0-6)",
                    statusAkses = "Bersekolah Aktif",
                    jenisLayanan = "PAUD / Kober",
                    namaSekolah = "PAUD Tunas Harapan Margadadi",
                    kebutuhanPendidikan = "Bantuan Alat Tulis & Buku Mewarnai",
                    catatanMonev = "Anak aktif mengikuti stimulasi tumbuh kembang PAUD mingguan.",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmPendidikanEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalInput = "08/11/2026",
                    namaAnak = "Rayhan Syaputra",
                    nik = "3212011504140003",
                    usiaTahun = 12,
                    kategoriUsia = "Usia Sekolah Dasar (7-12)",
                    statusAkses = "Bersekolah Aktif",
                    jenisLayanan = "SD / MI",
                    namaSekolah = "SDN Margadadi 1",
                    kebutuhanPendidikan = "Beasiswa Program Indonesia Pintar (PIP)",
                    catatanMonev = "Prestasi belajar baik, membutuhkan seragam dan sepatu sekolah.",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmPendidikanEntity(
                    posyanduId = 2L,
                    namaPosyandu = "Posyandu Mawar II",
                    kecamatan = "Indramayu",
                    desa = "Karanganyar",
                    tanggalInput = "05/11/2026",
                    namaAnak = "Bagus Permana",
                    nik = "3212012103110001",
                    usiaTahun = 15,
                    kategoriUsia = "Usia Menengah (13-18)",
                    statusAkses = "Putus Sekolah",
                    jenisLayanan = "Paket A/B/C",
                    namaSekolah = "PKBM Bina Mandiri (Rencana)",
                    kebutuhanPendidikan = "Fasilitasi Pendidikan Kesetaraan Paket B",
                    catatanMonev = "Perlu pendampingan agar kembali mengenyam pendidikan formal/nonformal.",
                    petugasInput = "Dewi Sartika, S.Kep"
                )
            )
        )

        // SPM 2: Kesehatan
        db.spmDao().insertAllKesehatan(
            listOf(
                SpmKesehatanEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalPelayanan = "10/11/2026",
                    namaSasaran = "Ananda Kenzo Aditya",
                    nik = "3212011210240001",
                    namaOrangTuaSuami = "Ibu Rina & Bpk Supardi",
                    jenisSasaran = "Bayi/Balita",
                    usiaBulanTahun = "14 Bulan",
                    beratBadanKg = 10.4,
                    tinggiBadanCm = 78.5,
                    statusGizi = "Gizi Baik (Normal)",
                    statusImunisasi = "Lengkap",
                    jenisImunisasiTerakhir = "DPT-HB-Hib Lanjutan",
                    pemberianPmtVitA = "Diberikan PMT Biskuit & Vit A Kapsul Merah",
                    lingkarKepalaLilaCm = 46.2,
                    catatanPelayanan = "Grafik KMS hijau naik secara konsisten, perkembangan motorik bagus.",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmKesehatanEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalPelayanan = "10/11/2026",
                    namaSasaran = "Ibu Nurlaela",
                    nik = "3212015502980004",
                    namaOrangTuaSuami = "Bpk Hendra Gunawan",
                    jenisSasaran = "Ibu Hamil",
                    usiaBulanTahun = "28 Tahun",
                    beratBadanKg = 62.0,
                    tinggiBadanCm = 155.0,
                    statusGizi = "Gizi Baik (Normal)",
                    statusImunisasi = "Lengkap",
                    jenisImunisasiTerakhir = "TT Ke-3",
                    pemberianPmtVitA = "Tablet Tambah Darah (TTD) 30 Tablet",
                    lingkarKepalaLilaCm = 24.5,
                    catatanPelayanan = "Usia kehamilan 28 minggu, TD 115/75 mmHg, detak jantung janin normal.",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmKesehatanEntity(
                    posyanduId = 2L,
                    namaPosyandu = "Posyandu Mawar II",
                    kecamatan = "Indramayu",
                    desa = "Karanganyar",
                    tanggalPelayanan = "07/11/2026",
                    namaSasaran = "Balita Fajar Pratama",
                    nik = "3212012005230002",
                    namaOrangTuaSuami = "Ibu Yayah",
                    jenisSasaran = "Bayi/Balita",
                    usiaBulanTahun = "26 Bulan",
                    beratBadanKg = 9.8,
                    tinggiBadanCm = 80.0,
                    statusGizi = "Risiko Stunting / Stunting",
                    statusImunisasi = "Sedang Berjalan",
                    jenisImunisasiTerakhir = "Campak Rubella",
                    pemberianPmtVitA = "Diberikan Paket PMT Telur & Susu Pertumbuhan",
                    lingkarKepalaLilaCm = 45.0,
                    catatanPelayanan = "TB/U di bawah -2 SD, dimasukkan ke intervensi program TOSS (Tanggap Obati Stunting) Indramayu.",
                    petugasInput = "Dewi Sartika, S.Kep"
                )
            )
        )

        // SPM 3: Pekerjaan Umum
        db.spmDao().insertAllPekerjaanUmum(
            listOf(
                SpmPekerjaanUmumEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalInput = "08/11/2026",
                    lokasiDusunRwRt = "Blok Karang Malang RT 04 RW 02",
                    aksesAirMinum = "Jaringan Perpipaan PDAM",
                    aksesSanitasiJamban = "Jamban Sehat Leher Angsa (Septic Tank)",
                    kondisiDrainase = "Berfungsi Baik & Bersih",
                    kondisiJalanLingkungan = "Rabat Beton / Aspal Baik",
                    kebutuhanSarprasPrioritas = "Normalisasi Saluran Sekunder Pemukiman",
                    deskripsiKondisi = "Kondisi sanitasi dan saluran air di wilayah RT 04 relatif baik, terhubung ke saluran pembuangan utama kota.",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmPekerjaanUmumEntity(
                    posyanduId = 3L,
                    namaPosyandu = "Posyandu Teratai",
                    kecamatan = "Jatibarang",
                    desa = "Jatibarang",
                    tanggalInput = "06/11/2026",
                    lokasiDusunRwRt = "Blok Gudang RT 03 RW 01",
                    aksesAirMinum = "Sumur Bor Terlindungi",
                    aksesSanitasiJamban = "Jamban Komunal Sehat",
                    kondisiDrainase = "Tersumbat Sedimen/Sampah",
                    kondisiJalanLingkungan = "Paving Rusak Ringan",
                    kebutuhanSarprasPrioritas = "Pembangunan Drainase U-Ditch 200m",
                    deskripsiKondisi = "Saat hujan deras terjadi genangan air selama 2 jam akibat pendangkalan parit lingkungan.",
                    petugasInput = "Neneng Hasanah"
                )
            )
        )

        // SPM 4: Perumahan Rakyat
        db.spmDao().insertAllPerumahan(
            listOf(
                SpmPerumahanEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalPendataan = "09/11/2026",
                    namaKepalaKeluarga = "Bpk Carsan (62 Th)",
                    nikKk = "3212011005640003",
                    alamatRumah = "RT 04 RW 02 Kelurahan Margadadi",
                    statusKelayakan = "RTLH Rusak Sedang",
                    kondisiAtap = "Asbes/Seng Berkarat",
                    kondisiDinding = "Bambu/Anyaman/Kayu Lapuk",
                    kondisiLantai = "Plester Semen Retak",
                    prioritasRehabilitasi = "Mendesak (Prioritas 2)",
                    usulanBantuan = "Program Rutilahu DPMD Indramayu",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmPerumahanEntity(
                    posyanduId = 2L,
                    namaPosyandu = "Posyandu Mawar II",
                    kecamatan = "Indramayu",
                    desa = "Karanganyar",
                    tanggalPendataan = "06/11/2026",
                    namaKepalaKeluarga = "Bpk Tarmo (58 Th)",
                    nikKk = "3212011408660001",
                    alamatRumah = "RT 01 RW 03 Kelurahan Karanganyar",
                    statusKelayakan = "RTLH Rusak Berat",
                    kondisiAtap = "Rumbia/Bocor Parah",
                    kondisiDinding = "Bambu/Anyaman/Kayu Lapuk",
                    kondisiLantai = "Tanah Liar",
                    prioritasRehabilitasi = "Sangat Mendesak (Prioritas 1)",
                    usulanBantuan = "BSPS / Bedah Rumah Baznas & Pemkab",
                    petugasInput = "Dewi Sartika, S.Kep"
                )
            )
        )

        // SPM 5: Trantibumlinmas
        db.spmDao().insertAllTrantibum(
            listOf(
                SpmTrantibumEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalInput = "09/11/2026",
                    wilayahRtRw = "RT 04 / RW 02 Margadadi",
                    statusPosRonda = "Pos Ronda Aktif 24 Jam",
                    jumlahAnggotaLinmas = 6,
                    kondisiKeamanan = "Lingkungan Aman Kondusif",
                    potensiKebencanaan = "Aman dari Bencana",
                    catatanKejadianBencana = "Tidak ada kejadian tindak pidana maupun bencana alam dalam 1 bulan terakhir.",
                    kebutuhanPerlindunganWarga = "Lampu Penerangan Jalan Lingkungan (PJU) 4 Titik",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmTrantibumEntity(
                    posyanduId = 5L,
                    namaPosyandu = "Posyandu Cempaka",
                    kecamatan = "Balongan",
                    desa = "Sukaurip",
                    tanggalInput = "07/11/2026",
                    wilayahRtRw = "RT 01 / RW 01 Sukaurip",
                    statusPosRonda = "Pos Ronda Aktif Sebagian",
                    jumlahAnggotaLinmas = 4,
                    kondisiKeamanan = "Lingkungan Aman Kondusif",
                    potensiKebencanaan = "Rawan Banjir / Rob Pasang Laut",
                    catatanKejadianBencana = "Pasang rob pesisir pantai mencapai 20cm di jalan pemukiman saat bulan purnama.",
                    kebutuhanPerlindunganWarga = "Tanggul Penahan Ombak Darurat & Pompa Air Portable",
                    petugasInput = "Rasminah"
                )
            )
        )

        // SPM 6: Sosial
        db.spmDao().insertAllSosial(
            listOf(
                SpmSosialEntity(
                    posyanduId = 1L,
                    namaPosyandu = "Posyandu Melati I",
                    kecamatan = "Indramayu",
                    desa = "Margadadi",
                    tanggalPendataan = "09/11/2026",
                    namaWarga = "Mbah Rasiti (76 Th)",
                    nik = "3212014503500001",
                    usiaTahun = 76,
                    kategoriSosial = "Lanjut Usia Terlantar / Rentan",
                    jenisDisabilitas = "Tidak Ada",
                    statusPenerimaanBansos = "Penerima BPNT/Sembako",
                    kebutuhanPelayananSosial = "Bantuan Pangan Sembako Tambahan & Home Care Lansia",
                    catatanKondisi = "Lansia tinggal seorang diri di rumah kontrakan sederhana, kondisi fisik menurun.",
                    petugasInput = "Siti Rohmah, A.Md.Keb"
                ),
                SpmSosialEntity(
                    posyanduId = 3L,
                    namaPosyandu = "Posyandu Teratai",
                    kecamatan = "Jatibarang",
                    desa = "Jatibarang",
                    tanggalPendataan = "08/11/2026",
                    namaWarga = "Dede Suhendar (19 Th)",
                    nik = "3212030206070002",
                    usiaTahun = 19,
                    kategoriSosial = "Penyandang Disabilitas Fisik/Sensorik",
                    jenisDisabilitas = "Fisik (Kelumpuhan Kaki Pasca Kecelakaan)",
                    statusPenerimaanBansos = "Belum Menerima Bansos",
                    kebutuhanPelayananSosial = "Alat Bantu (Kursi Roda) & Pelatihan Keterampilan Mandiri",
                    catatanKondisi = "Memerlukan kursi roda untuk mobilitas sehari-hari dan diusulkan program bansos disabilitas.",
                    petugasInput = "Neneng Hasanah"
                )
            )
        )

        // 6. Seed Laporan & Verifikasi
        val laporanList = listOf(
            LaporanEntity(
                nomorLaporan = "LAP-IMY-2026-001",
                posyanduId = 1L,
                namaPosyandu = "Posyandu Melati I",
                kecamatan = "Indramayu",
                desa = "Margadadi",
                bidangSpm = BidangSpm.KESEHATAN,
                judulLaporan = "Laporan Layanan Penimbangan Balita & Bumil Rutin Periode November 2026",
                deskripsiCapaian = "Telah dilaksanakan penimbangan balita sebanyak 54 anak dari 58 sasaran (93.1%), 12 bumil terlayani, imunisasi campak dan pemberian vit A kapsul merah.",
                jumlahSasaranTerlayani = 66,
                targetSasaran = 72,
                kendalaHambatan = "4 balita tidak hadir karena sedang berada di luar kota.",
                tindakLanjut = "Kunjungan sweeping kader ke rumah pada hari Kamis.",
                periodeBulan = "November",
                periodeTahun = 2026,
                tanggalKirim = "10/11/2026",
                statusVerifikasi = StatusVerifikasi.DISETUJUI,
                catatanVerifikator = "Data lengkap dan akurat sesuai buku register posyandu. Cakupan capaian sangat baik.",
                namaVerifikator = "Admin DPMD Indramayu",
                tanggalVerifikasi = "10/11/2026",
                namaPetugasPengirim = "Siti Rohmah, A.Md.Keb",
                emailPetugasPengirim = "petugas@indramayukab.go.id"
            ),
            LaporanEntity(
                nomorLaporan = "LAP-IMY-2026-002",
                posyanduId = 2L,
                namaPosyandu = "Posyandu Mawar II",
                kecamatan = "Indramayu",
                desa = "Karanganyar",
                bidangSpm = BidangSpm.PERUMAHAN_RAKYAT,
                judulLaporan = "Pendataan Validasi RTLH Prioritas 1 Karanganyar",
                deskripsiCapaian = "Pendataan dan verifikasi lapangan 3 unit rumah tidak layak huni warga miskin ekstrem di RT 01 RW 03.",
                jumlahSasaranTerlayani = 3,
                targetSasaran = 3,
                kendalaHambatan = "Status kepemilikan tanah keluarga masih berupa girik waris.",
                tindakLanjut = "Koordinasi dengan perangkat desa untuk surat keterangan kepemilikan.",
                periodeBulan = "November",
                periodeTahun = 2026,
                tanggalKirim = "09/11/2026",
                statusVerifikasi = StatusVerifikasi.MENUNGGU,
                catatanVerifikator = "",
                namaVerifikator = "",
                tanggalVerifikasi = "",
                namaPetugasPengirim = "Dewi Sartika, S.Kep",
                emailPetugasPengirim = "dewi.mawar@indramayukab.go.id"
            ),
            LaporanEntity(
                nomorLaporan = "LAP-JTB-2026-003",
                posyanduId = 3L,
                namaPosyandu = "Posyandu Teratai",
                kecamatan = "Jatibarang",
                desa = "Jatibarang",
                bidangSpm = BidangSpm.SOSIAL,
                judulLaporan = "Pemeriksaan dan Pendataan Disabilitas serta Lansia Terlantar",
                deskripsiCapaian = "Berhasil memverifikasi 8 warga lansia dan 2 penyandang disabilitas berat untuk pengusulan bantuan APBD DPMD.",
                jumlahSasaranTerlayani = 10,
                targetSasaran = 12,
                kendalaHambatan = "Belum memiliki foto KTP fisik yang terbaca jelas.",
                tindakLanjut = "Perlu revisi kelengkapan dokumen pendukung KTP/KK pemohon.",
                periodeBulan = "November",
                periodeTahun = 2026,
                tanggalKirim = "08/11/2026",
                statusVerifikasi = StatusVerifikasi.PERLU_REVISI,
                catatanVerifikator = "Mohon lampirkan scan NIK/KK yang valid agar dapat diusulkan ke sistem Dinsos/DPMD.",
                namaVerifikator = "Admin DPMD Indramayu",
                tanggalVerifikasi = "09/11/2026",
                namaPetugasPengirim = "Neneng Hasanah",
                emailPetugasPengirim = "neneng.jatibarang@indramayukab.go.id"
            )
        )
        db.laporanDao().insertAllLaporan(laporanList)

        // 7. Seed Audit Logs
        val logs = listOf(
            AuditLogEntity(
                waktuFormatted = "10/11/2026 09:30",
                userEmail = "admin@indramayukab.go.id",
                userName = "Admin DPMD Indramayu",
                userRole = "ADMIN",
                action = "VERIFIKASI_LAPORAN",
                entityType = "Laporan",
                details = "Menyetujui laporan kesehatan Posyandu Melati I Margadadi (LAP-IMY-2026-001)"
            ),
            AuditLogEntity(
                waktuFormatted = "10/11/2026 08:15",
                userEmail = "petugas@indramayukab.go.id",
                userName = "Siti Rohmah, A.Md.Keb",
                userRole = "PETUGAS",
                action = "INPUT_SPM_KESEHATAN",
                entityType = "SPM Kesehatan",
                details = "Mencatat pelayanan gizi dan imunisasi balita Ananda Kenzo Aditya"
            ),
            AuditLogEntity(
                waktuFormatted = "09/11/2026 14:20",
                userEmail = "dewi.mawar@indramayukab.go.id",
                userName = "Dewi Sartika, S.Kep",
                userRole = "PETUGAS",
                action = "KIRIM_LAPORAN",
                entityType = "Laporan",
                details = "Mengirimkan laporan RTLH Karanganyar (LAP-IMY-2026-002)"
            )
        )
        for (log in logs) {
            db.auditLogDao().insertLog(log)
        }
    }
}
