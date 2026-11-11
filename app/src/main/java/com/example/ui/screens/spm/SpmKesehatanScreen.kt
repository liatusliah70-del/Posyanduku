package com.example.ui.screens.spm

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BidangSpm
import com.example.data.model.PosyanduEntity
import com.example.data.model.SpmKesehatanEntity
import com.example.data.model.UserEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpmKesehatanScreen(
    dataList: List<SpmKesehatanEntity>,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onSave: (SpmKesehatanEntity, Boolean) -> Unit,
    onDelete: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSasaranFilter by remember { mutableStateOf("Semua Sasaran") }
    var showDialog by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<SpmKesehatanEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<SpmKesehatanEntity?>(null) }

    val filteredList = remember(dataList, searchQuery, selectedSasaranFilter) {
        dataList.filter {
            val matchesQuery = it.namaSasaran.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.statusGizi.contains(searchQuery, ignoreCase = true)
            val matchesFilter = selectedSasaranFilter == "Semua Sasaran" || it.jenisSasaran == selectedSasaranFilter
            matchesQuery && matchesFilter
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showDialog = true
                },
                containerColor = SpmKesehatanColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Data Kesehatan")
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
            // Header
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SpmBadge(bidangSpm = BidangSpm.KESEHATAN)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Modul Bidang Kesehatan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Pelayanan Balita, Bumil, Busui, Penimbangan, Imunisasi, & Pemantauan Stunting",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama sasaran, status gizi, posyandu...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ScrollableTabRow(
                        selectedTabIndex = 0,
                        edgePadding = 0.dp,
                        divider = {},
                        indicator = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val filters = listOf("Semua Sasaran", "Bayi/Balita", "Ibu Hamil", "Ibu Menyusui", "Lansia")
                        filters.forEach { f ->
                            val isSel = selectedSasaranFilter == f
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedSasaranFilter = f },
                                label = { Text(f, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SpmKesehatanColor,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        }
                    }
                }
            }

            // Cards List
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
                                    Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = SpmKesehatanColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Belum Ada Data SPM Kesehatan", fontWeight = FontWeight.Bold)
                                Text(
                                    "Tekan tombol (+) untuk mencatat pelayanan penimbangan dan gizi.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { item ->
                        val isStunting = item.statusGizi.contains("Stunting", ignoreCase = true)
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
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                color = SpmKesehatanColor.copy(alpha = 0.12f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = item.jenisSasaran,
                                                    color = SpmKesehatanColor,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = if (isStunting) StatusError.copy(alpha = 0.15f) else StatusSuccess.copy(alpha = 0.12f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = item.statusGizi,
                                                    color = if (isStunting) StatusError else StatusSuccess,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = item.namaSasaran,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Usia: ${item.usiaBulanTahun} • Ortu/Keluarga: ${item.namaOrangTuaSuami.ifBlank { "-" }}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row {
                                        IconButton(onClick = {
                                            editingEntity = item
                                            showDialog = true
                                        }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = IndramayuNavyLight)
                                        }
                                        IconButton(onClick = { itemToDelete = item }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = StatusError)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = GovBorder.copy(alpha = 0.6f))
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Berat Badan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("${item.beratBadanKg} kg", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = IndramayuNavy)
                                    }
                                    Column {
                                        Text("Tinggi Badan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("${item.tinggiBadanCm} cm", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = IndramayuNavy)
                                    }
                                    Column {
                                        Text("Imunisasi", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.jenisImunisasiTerakhir, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                if (item.pemberianPmtVitA.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "PMT / Suplemen: ${item.pemberianPmtVitA}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (item.catatanPelayanan.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Catatan: ${item.catatanPelayanan}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isStunting) StatusError else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isStunting) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        KesehatanFormDialog(
            initial = editingEntity,
            posyanduList = posyanduList,
            currentUser = currentUser,
            onDismiss = {
                showDialog = false
                editingEntity = null
            },
            onSave = { saved, isEdit ->
                onSave(saved, isEdit)
                showDialog = false
                editingEntity = null
            }
        )
    }

    itemToDelete?.let { itm ->
        ConfirmDeleteDialog(
            title = "Hapus Data SPM Kesehatan",
            message = "Apakah Anda yakin ingin menghapus data pemeriksaan an. ${itm.namaSasaran}?",
            onConfirm = {
                onDelete(itm.id, itm.namaSasaran)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun KesehatanFormDialog(
    initial: SpmKesehatanEntity?,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (SpmKesehatanEntity, Boolean) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: (defaultPos?.namaPosyandu ?: "Posyandu Melati I")) }
    var namaSasaran by remember { mutableStateOf(initial?.namaSasaran ?: "") }
    var nik by remember { mutableStateOf(initial?.nik ?: "") }
    var namaOrtu by remember { mutableStateOf(initial?.namaOrangTuaSuami ?: "") }
    var jenisSasaran by remember { mutableStateOf(initial?.jenisSasaran ?: "Bayi/Balita") }
    var usia by remember { mutableStateOf(initial?.usiaBulanTahun ?: "18 Bulan") }
    var bb by remember { mutableStateOf(initial?.beratBadanKg?.toString() ?: "10.0") }
    var tb by remember { mutableStateOf(initial?.tinggiBadanCm?.toString() ?: "80.0") }
    var statusGizi by remember { mutableStateOf(initial?.statusGizi ?: "Gizi Baik (Normal)") }
    var imunisasi by remember { mutableStateOf(initial?.jenisImunisasiTerakhir ?: "Campak Rubella") }
    var pmt by remember { mutableStateOf(initial?.pemberianPmtVitA ?: "PMT Biskuit & Vitamin A") }
    var catatan by remember { mutableStateOf(initial?.catatanPelayanan ?: "") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Input Layanan Posyandu (Kesehatan)" else "Ubah Layanan Kesehatan",
                fontWeight = FontWeight.Bold
            )
        },
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
                        value = namaSasaran,
                        onValueChange = { namaSasaran = it },
                        label = { Text("Nama Sasaran (Balita/Bumil/Lansia) *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = nik,
                        onValueChange = { nik = it },
                        label = { Text("NIK Sasaran (16 digit)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = namaOrtu,
                        onValueChange = { namaOrtu = it },
                        label = { Text("Nama Orang Tua / Suami") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = jenisSasaran,
                            onValueChange = { jenisSasaran = it },
                            label = { Text("Kategori Sasaran") },
                            modifier = Modifier.weight(1.2f)
                        )
                        OutlinedTextField(
                            value = usia,
                            onValueChange = { usia = it },
                            label = { Text("Usia (Bulan/Th)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = bb,
                            onValueChange = { bb = it },
                            label = { Text("Berat Badan (kg) *") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = tb,
                            onValueChange = { tb = it },
                            label = { Text("Tinggi Badan (cm) *") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = statusGizi,
                        onValueChange = { statusGizi = it },
                        label = { Text("Status Gizi (Normal/Stunting/Kurang)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = imunisasi,
                        onValueChange = { imunisasi = it },
                        label = { Text("Imunisasi Diberikan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = pmt,
                        onValueChange = { pmt = it },
                        label = { Text("Pemberian PMT / Vit A / Suplemen") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = catatan,
                        onValueChange = { catatan = it },
                        label = { Text("Catatan Bidan / Kader") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaSasaran.isBlank()) {
                        formError = "Nama sasaran harus diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val entity = SpmKesehatanEntity(
                        id = initial?.id ?: 0,
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        tanggalPelayanan = initial?.tanggalPelayanan ?: "10/11/2026",
                        namaSasaran = namaSasaran.trim(),
                        nik = nik.trim(),
                        namaOrangTuaSuami = namaOrtu.trim(),
                        jenisSasaran = jenisSasaran.trim(),
                        usiaBulanTahun = usia.trim(),
                        beratBadanKg = bb.toDoubleOrNull() ?: 10.0,
                        tinggiBadanCm = tb.toDoubleOrNull() ?: 80.0,
                        statusGizi = statusGizi.trim(),
                        statusImunisasi = "Lengkap",
                        jenisImunisasiTerakhir = imunisasi.trim(),
                        pemberianPmtVitA = pmt.trim(),
                        catatanPelayanan = catatan.trim(),
                        petugasInput = currentUser?.fullName ?: "Petugas Posyandu"
                    )
                    onSave(entity, initial != null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpmKesehatanColor)
            ) {
                Text("Simpan Data")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
