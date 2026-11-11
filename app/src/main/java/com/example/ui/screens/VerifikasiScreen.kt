package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LaporanEntity
import com.example.data.model.StatusVerifikasi
import com.example.data.model.UserEntity
import com.example.ui.components.SpmBadge
import com.example.ui.components.StatusVerifikasiBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifikasiScreen(
    laporanList: List<LaporanEntity>,
    currentUser: UserEntity?,
    onVerifikasi: (id: Long, status: StatusVerifikasi, catatan: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Semua, 1: Menunggu, 2: Disetujui, 3: Perlu Revisi
    var activeReportForReview by remember { mutableStateOf<LaporanEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(laporanList, selectedTab, searchQuery) {
        laporanList.filter {
            val matchesTab = when (selectedTab) {
                1 -> it.statusVerifikasi == StatusVerifikasi.MENUNGGU
                2 -> it.statusVerifikasi == StatusVerifikasi.DISETUJUI
                3 -> it.statusVerifikasi == StatusVerifikasi.PERLU_REVISI
                else -> true
            }
            val matchesSearch = it.judulLaporan.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.nomorLaporan.contains(searchQuery, ignoreCase = true)
            matchesTab && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GovBackgroundLight)
    ) {
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
                    Column {
                        Text(
                            text = "Verifikasi Laporan Posyandu",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = IndramayuNavy
                        )
                        Text(
                            text = "Pemeriksaan dan validasi laporan 6 Bidang SPM oleh DPMD",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = StatusPending.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${laporanList.count { it.statusVerifikasi == StatusVerifikasi.MENUNGGU }} Antrean",
                            color = IndramayuGoldDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari nomor laporan, posyandu, judul...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 0.dp,
                    indicator = {},
                    divider = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val tabs = listOf(
                        "Semua (${laporanList.size})",
                        "Menunggu (${laporanList.count { it.statusVerifikasi == StatusVerifikasi.MENUNGGU }})",
                        "Disetujui (${laporanList.count { it.statusVerifikasi == StatusVerifikasi.DISETUJUI }})",
                        "Revisi (${laporanList.count { it.statusVerifikasi == StatusVerifikasi.PERLU_REVISI }})"
                    )
                    tabs.forEachIndexed { index, tabTitle ->
                        FilterChip(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            label = { Text(tabTitle, fontSize = 12.sp) },
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

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredList.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Tidak Ada Antrean Verifikasi", fontWeight = FontWeight.Bold)
                            Text(
                                "Semua laporan pada filter ini telah ditinjau.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredList, key = { it.id }) { item ->
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
                                    Surface(
                                        color = IndramayuNavy.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = item.nomorLaporan,
                                            color = IndramayuNavy,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    SpmBadge(bidangSpm = item.bidangSpm)
                                }
                                StatusVerifikasiBadge(status = item.statusVerifikasi)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = item.judulLaporan,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${item.namaPosyandu} • ${item.desa}, Kec. ${item.kecamatan}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.deskripsiCapaian,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 3
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = GovBorder.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Pengirim: ${item.namaPetugasPengirim}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Tgl Kirim: ${item.tanggalKirim} (${item.periodeBulan} ${item.periodeTahun})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = { activeReportForReview = item },
                                    colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Tinjau & Validasi", fontSize = 12.sp)
                                }
                            }

                            if (item.catatanVerifikator.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Catatan Verifikator (${item.namaVerifikator}): ${item.catatanVerifikator}",
                                        fontSize = 11.sp,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Review & Verification Dialog
    activeReportForReview?.let { rpt ->
        var selectedStatus by remember { mutableStateOf(rpt.statusVerifikasi) }
        var reviewNote by remember { mutableStateOf(rpt.catatanVerifikator) }

        AlertDialog(
            onDismissRequest = { activeReportForReview = null },
            title = {
                Text(text = "Tinjauan Verifikasi Laporan", fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = rpt.judulLaporan,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${rpt.nomorLaporan} • Posyandu ${rpt.namaPosyandu}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Deskripsi Capaian Posyandu:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(rpt.deskripsiCapaian, fontSize = 12.sp)
                                if (rpt.kendalaHambatan.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Kendala: ${rpt.kendalaHambatan}", fontSize = 11.sp, color = StatusWarning)
                                }
                            }
                        }
                    }

                    item {
                        Text("Keputusan Verifikasi:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedStatus == StatusVerifikasi.DISETUJUI,
                                onClick = { selectedStatus = StatusVerifikasi.DISETUJUI },
                                label = { Text("Disetujui") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StatusSuccess,
                                    selectedLabelColor = Color.White
                                )
                            )
                            FilterChip(
                                selected = selectedStatus == StatusVerifikasi.PERLU_REVISI,
                                onClick = { selectedStatus = StatusVerifikasi.PERLU_REVISI },
                                label = { Text("Perlu Revisi") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StatusError,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = reviewNote,
                            onValueChange = { reviewNote = it },
                            label = { Text("Catatan Verifikator DPMD") },
                            placeholder = { Text("Contoh: Data telah lengkap dan sesuai buku register.") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onVerifikasi(rpt.id, selectedStatus, reviewNote.trim())
                        activeReportForReview = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy)
                ) {
                    Text("Simpan Status")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { activeReportForReview = null }) {
                    Text("Batal")
                }
            }
        )
    }
}
