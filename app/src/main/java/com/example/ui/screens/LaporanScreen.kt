package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.components.StatusVerifikasiBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanScreen(
    laporanList: List<LaporanEntity>,
    posyanduList: List<PosyanduEntity>,
    kecamatanList: List<KecamatanEntity>,
    currentUser: UserEntity?,
    onSubmitLaporan: (LaporanEntity) -> Unit,
    onDeleteLaporan: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedKecamatan by remember { mutableStateOf("Semua Kecamatan") }
    var selectedBulan by remember { mutableStateOf("Semua Bulan") }
    var selectedBidangSpm by remember { mutableStateOf("Semua SPM") }
    var showNewReportDialog by remember { mutableStateOf(false) }
    var laporanToDelete by remember { mutableStateOf<LaporanEntity?>(null) }
    var showPrintPreviewDialog by remember { mutableStateOf(false) }

    val filteredLaporan = remember(laporanList, selectedKecamatan, selectedBulan, selectedBidangSpm) {
        laporanList.filter {
            val matchesKec = selectedKecamatan == "Semua Kecamatan" || it.kecamatan == selectedKecamatan
            val matchesBulan = selectedBulan == "Semua Bulan" || it.periodeBulan == selectedBulan
            val matchesSpm = selectedBidangSpm == "Semua SPM" || it.bidangSpm.displayName == selectedBidangSpm
            matchesKec && matchesBulan && matchesSpm
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewReportDialog = true },
                containerColor = IndramayuNavy,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Buat Laporan Baru")
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(GovBackgroundLight)
        ) {
            // Header with Official Kop Summary
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sistem Laporan & Rekapitulasi SPM",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = IndramayuNavy
                            )
                            Text(
                                text = "Laporan Posyandu 6 Bidang SPM Kabupaten Indramayu",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Export / Share Actions
                        Row {
                            IconButton(onClick = { showPrintPreviewDialog = true }) {
                                Icon(Icons.Default.Print, contentDescription = "Cetak Rekapitulasi", tint = IndramayuNavy)
                            }
                            IconButton(onClick = {
                                shareReportText(context, filteredLaporan, selectedKecamatan, selectedBulan)
                            }) {
                                Icon(Icons.Default.Share, contentDescription = "Export / Bagikan", tint = PosyanduCyan)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Kecamatan Filter
                        var kecExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = kecExpanded,
                            onExpandedChange = { kecExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedKecamatan,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Kecamatan", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kecExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = kecExpanded,
                                onDismissRequest = { kecExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Semua Kecamatan") },
                                    onClick = {
                                        selectedKecamatan = "Semua Kecamatan"
                                        kecExpanded = false
                                    }
                                )
                                kecamatanList.forEach { kec ->
                                    DropdownMenuItem(
                                        text = { Text(kec.namaKecamatan) },
                                        onClick = {
                                            selectedKecamatan = kec.namaKecamatan
                                            kecExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Bulan Filter
                        var bulanExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = bulanExpanded,
                            onExpandedChange = { bulanExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedBulan,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Periode", fontSize = 11.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bulanExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = bulanExpanded,
                                onDismissRequest = { bulanExpanded = false }
                            ) {
                                val bulanList = listOf("Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
                                bulanList.forEach { bln ->
                                    DropdownMenuItem(
                                        text = { Text(bln) },
                                        onClick = {
                                            selectedBulan = bln
                                            bulanExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // SPM Filter Chips
                    ScrollableTabRow(
                        selectedTabIndex = 0,
                        edgePadding = 0.dp,
                        indicator = {},
                        divider = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val spmFilters = listOf("Semua SPM") + BidangSpm.values().map { it.displayName }
                        spmFilters.forEach { spmTitle ->
                            val isSel = selectedBidangSpm == spmTitle
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedBidangSpm = spmTitle },
                                label = { Text(spmTitle, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = IndramayuNavy,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        }
                    }
                }
            }

            // Reports List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Summary Statistics Bar
                item {
                    Surface(
                        color = IndramayuNavy.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total", style = MaterialTheme.typography.labelSmall)
                                Text("${filteredLaporan.size}", fontWeight = FontWeight.Bold, color = IndramayuNavy)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Disetujui", style = MaterialTheme.typography.labelSmall)
                                Text("${filteredLaporan.count { it.statusVerifikasi == StatusVerifikasi.DISETUJUI }}", fontWeight = FontWeight.Bold, color = StatusSuccess)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Menunggu", style = MaterialTheme.typography.labelSmall)
                                Text("${filteredLaporan.count { it.statusVerifikasi == StatusVerifikasi.MENUNGGU }}", fontWeight = FontWeight.Bold, color = StatusPending)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Revisi", style = MaterialTheme.typography.labelSmall)
                                Text("${filteredLaporan.count { it.statusVerifikasi == StatusVerifikasi.PERLU_REVISI }}", fontWeight = FontWeight.Bold, color = StatusError)
                            }
                        }
                    }
                }

                if (filteredLaporan.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.FolderOpen, contentDescription = null, tint = IndramayuNavy, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Tidak Ada Laporan yang Cocok", fontWeight = FontWeight.Bold)
                                Text("Silakan sesuaikan filter kecamatan atau periode bulan Anda.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    items(filteredLaporan, key = { it.id }) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.nomorLaporan,
                                            fontWeight = FontWeight.Bold,
                                            color = IndramayuNavy,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        SpmBadge(bidangSpm = item.bidangSpm)
                                    }
                                    StatusVerifikasiBadge(status = item.statusVerifikasi)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = item.judulLaporan,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${item.namaPosyandu} (${item.desa}, Kec. ${item.kecamatan})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = item.deskripsiCapaian,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = GovBorder.copy(alpha = 0.6f))
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sasaran: ${item.jumlahSasaranTerlayani}/${item.targetSasaran} Terlayani",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = IndramayuNavy
                                    )
                                    Text(
                                        text = "Periode: ${item.periodeBulan} ${item.periodeTahun}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    IconButton(
                                        onClick = { laporanToDelete = item },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = StatusError, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // New Report Dialog
    if (showNewReportDialog) {
        NewReportFormDialog(
            posyanduList = posyanduList,
            currentUser = currentUser,
            onDismiss = { showNewReportDialog = false },
            onSubmit = { newRpt ->
                onSubmitLaporan(newRpt)
                showNewReportDialog = false
            }
        )
    }

    // Print & PDF Official Preview Dialog
    if (showPrintPreviewDialog) {
        OfficialPrintKopDialog(
            laporanList = filteredLaporan,
            kecamatan = selectedKecamatan,
            bulan = selectedBulan,
            onDismiss = { showPrintPreviewDialog = false },
            onShare = {
                shareReportText(context, filteredLaporan, selectedKecamatan, selectedBulan)
            }
        )
    }

    // Delete confirmation
    laporanToDelete?.let { rpt ->
        ConfirmDeleteDialog(
            title = "Hapus Laporan",
            message = "Apakah Anda yakin ingin menghapus laporan '${rpt.nomorLaporan} - ${rpt.judulLaporan}'?",
            onConfirm = {
                onDeleteLaporan(rpt.id, rpt.nomorLaporan)
                laporanToDelete = null
            },
            onDismiss = { laporanToDelete = null }
        )
    }
}

private fun shareReportText(
    context: Context,
    list: List<LaporanEntity>,
    kecamatan: String,
    bulan: String
) {
    val dateStr = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
    val builder = StringBuilder()
    builder.appendLine("PEMERINTAH KABUPATEN INDRAMAYU")
    builder.appendLine("DINAS PEMBERDAYAAN MASYARAKAT DAN DESA")
    builder.appendLine("Jl. Gatot Subroto No. 6 Indramayu - Jawa Barat")
    builder.appendLine("==========================================")
    builder.appendLine("REKAPITULASI LAPORAN 6 BIDANG SPM POSYANDU")
    builder.appendLine("Wilayah: $kecamatan | Periode: $bulan")
    builder.appendLine("Tanggal Cetak: $dateStr")
    builder.appendLine("Total Laporan: ${list.size} Dokumen")
    builder.appendLine("------------------------------------------")

    list.forEachIndexed { i, rpt ->
        builder.appendLine("${i + 1}. [${rpt.nomorLaporan}] ${rpt.judulLaporan}")
        builder.appendLine("   Posyandu: ${rpt.namaPosyandu} (${rpt.desa}, Kec. ${rpt.kecamatan})")
        builder.appendLine("   Bidang: ${rpt.bidangSpm.displayName} | Status: ${rpt.statusVerifikasi.name}")
        builder.appendLine("   Capaian: ${rpt.jumlahSasaranTerlayani}/${rpt.targetSasaran} sasaran")
        builder.appendLine("   Ringkasan: ${rpt.deskripsiCapaian}")
        builder.appendLine()
    }
    builder.appendLine("==========================================")
    builder.appendLine("Aplikasi POSYANDUKU INDRAMAYU @beben_dpmd_imyu 2026")

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Rekapitulasi Laporan Posyanduku Indramayu")
        putExtra(Intent.EXTRA_TEXT, builder.toString())
    }
    context.startActivity(Intent.createChooser(intent, "Bagikan Rekapitulasi Laporan SPM"))
}

@Composable
fun OfficialPrintKopDialog(
    laporanList: List<LaporanEntity>,
    kecamatan: String,
    bulan: String,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Pratinjau Cetak Lembar Rekapitulasi Resmi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                // Kop Surat Resmi Pemkab Indramayu & DPMD
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_posyanduku),
                            contentDescription = "Logo Kop",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PEMERINTAH KABUPATEN INDRAMAYU",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = IndramayuNavyDark
                            )
                            Text(
                                text = "DINAS PEMBERDAYAAN MASYARAKAT DAN DESA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = IndramayuNavy
                            )
                            Text(
                                text = "Jl. Gatot Subroto No. 6 Indramayu, Jawa Barat 45211",
                                fontSize = 9.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                    Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    Text(
                        text = "LEMBAR REKAPITULASI PELAYANAN 6 BIDANG SPM POSYANDU",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Wilayah: $kecamatan • Periode: $bulan 2026",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(laporanList) { rpt ->
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "${rpt.nomorLaporan} - ${rpt.judulLaporan}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${rpt.namaPosyandu} (${rpt.bidangSpm.displayName}) • Status: ${rpt.statusVerifikasi.name}",
                                fontSize = 10.sp,
                                color = IndramayuNavy
                            )
                            Text(
                                text = "Capaian: ${rpt.jumlahSasaranTerlayani}/${rpt.targetSasaran} sasaran terlayani",
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Mengetahui,\nKepala Dinas Pemberdayaan Masyarakat dan Desa\nKabupaten Indramayu",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "( .................................................... )",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onShare()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export / Bagikan")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Composable
fun NewReportFormDialog(
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSubmit: (LaporanEntity) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(defaultPos?.namaPosyandu ?: "Posyandu Melati I") }
    var selectedSpm by remember { mutableStateOf(BidangSpm.KESEHATAN) }
    var judul by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var sasaranTerlayani by remember { mutableStateOf("50") }
    var targetSasaran by remember { mutableStateOf("60") }
    var kendala by remember { mutableStateOf("") }
    var tindakLanjut by remember { mutableStateOf("") }
    var periodeBulan by remember { mutableStateOf("November") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kirim Laporan Posyandu Baru", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (formError != null) {
                    item {
                        Text(text = formError ?: "", color = StatusError, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                item {
                    OutlinedTextField(
                        value = judul,
                        onValueChange = { judul = it },
                        label = { Text("Judul Laporan Kegiatan *") },
                        placeholder = { Text("Contoh: Laporan Penimbangan Balita & Gizi Rutin") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text("Pilih Bidang SPM:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        FilterChip(
                            selected = selectedSpm == BidangSpm.KESEHATAN,
                            onClick = { selectedSpm = BidangSpm.KESEHATAN },
                            label = { Text("Kesehatan", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = selectedSpm == BidangSpm.PENDIDIKAN,
                            onClick = { selectedSpm = BidangSpm.PENDIDIKAN },
                            label = { Text("Pendidikan", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = selectedSpm == BidangSpm.SOSIAL,
                            onClick = { selectedSpm = BidangSpm.SOSIAL },
                            label = { Text("Sosial", fontSize = 11.sp) }
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = deskripsi,
                        onValueChange = { deskripsi = it },
                        label = { Text("Deskripsi Capaian Pelayanan *") },
                        placeholder = { Text("Jelaskan hasil capaian pelayanan dan kegiatan...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = sasaranTerlayani,
                            onValueChange = { sasaranTerlayani = it },
                            label = { Text("Terlayani") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = targetSasaran,
                            onValueChange = { targetSasaran = it },
                            label = { Text("Target") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = kendala,
                        onValueChange = { kendala = it },
                        label = { Text("Kendala / Hambatan Lapangan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = tindakLanjut,
                        onValueChange = { tindakLanjut = it },
                        label = { Text("Rencana Tindak Lanjut") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (judul.isBlank() || deskripsi.isBlank()) {
                        formError = "Judul dan deskripsi capaian wajib diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    val newRpt = LaporanEntity(
                        nomorLaporan = "LAP-IMY-2026-${System.currentTimeMillis() % 1000}",
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        bidangSpm = selectedSpm,
                        judulLaporan = judul.trim(),
                        deskripsiCapaian = deskripsi.trim(),
                        jumlahSasaranTerlayani = sasaranTerlayani.toIntOrNull() ?: 0,
                        targetSasaran = targetSasaran.toIntOrNull() ?: 0,
                        kendalaHambatan = kendala.trim(),
                        tindakLanjut = tindakLanjut.trim(),
                        periodeBulan = periodeBulan,
                        periodeTahun = 2026,
                        tanggalKirim = dateStr,
                        statusVerifikasi = StatusVerifikasi.MENUNGGU,
                        namaPetugasPengirim = currentUser?.fullName ?: "Petugas Posyandu",
                        emailPetugasPengirim = currentUser?.email ?: "petugas@indramayukab.go.id"
                    )
                    onSubmit(newRpt)
                },
                colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy)
            ) {
                Text("Kirim Laporan")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
