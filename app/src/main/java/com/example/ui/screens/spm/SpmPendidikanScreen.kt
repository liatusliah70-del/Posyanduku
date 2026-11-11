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
import com.example.data.model.SpmPendidikanEntity
import com.example.data.model.UserEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpmPendidikanScreen(
    dataList: List<SpmPendidikanEntity>,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onSave: (SpmPendidikanEntity, Boolean) -> Unit,
    onDelete: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<SpmPendidikanEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<SpmPendidikanEntity?>(null) }

    val filteredList = remember(dataList, searchQuery) {
        dataList.filter {
            it.namaAnak.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.jenisLayanan.contains(searchQuery, ignoreCase = true) ||
                    it.statusAkses.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showDialog = true
                },
                containerColor = SpmPendidikanColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Data Pendidikan")
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
            // Header Card
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
                            SpmBadge(bidangSpm = BidangSpm.PENDIDIKAN)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Modul Bidang Pendidikan",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = IndramayuNavy
                            )
                            Text(
                                text = "Pendataan Anak Usia Dini & Sekolah, Layanan PAUD, dan Akses Pendidikan",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama anak, posyandu, status akses...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // List of items
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
                                    Icons.Default.School,
                                    contentDescription = null,
                                    tint = SpmPendidikanColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Belum Ada Data SPM Pendidikan", fontWeight = FontWeight.Bold)
                                Text(
                                    "Tekan tombol (+) di pojok kanan bawah untuk menambah data.",
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
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Surface(
                                            color = SpmPendidikanColor.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = item.statusAkses,
                                                color = SpmPendidikanColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = item.namaAnak,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Usia: ${item.usiaTahun} Tahun • ${item.kategoriUsia}",
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
                                        Text("Layanan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.jenisLayanan, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Posyandu", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.namaPosyandu, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Kebutuhan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kebutuhanPendidikan.take(15) + "...", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                    }
                                }

                                if (item.catatanMonev.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Catatan Monev: ${item.catatanMonev}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Form Dialog
    if (showDialog) {
        PendidikanFormDialog(
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

    // Delete confirmation
    itemToDelete?.let { itm ->
        ConfirmDeleteDialog(
            title = "Hapus Data SPM Pendidikan",
            message = "Apakah Anda yakin ingin menghapus data anak an. ${itm.namaAnak} di ${itm.namaPosyandu}?",
            onConfirm = {
                onDelete(itm.id, itm.namaAnak)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun PendidikanFormDialog(
    initial: SpmPendidikanEntity?,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (SpmPendidikanEntity, Boolean) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: (defaultPos?.namaPosyandu ?: "Posyandu Melati I")) }
    var namaAnak by remember { mutableStateOf(initial?.namaAnak ?: "") }
    var nik by remember { mutableStateOf(initial?.nik ?: "") }
    var usiaTahun by remember { mutableStateOf(initial?.usiaTahun?.toString() ?: "5") }
    var kategoriUsia by remember { mutableStateOf(initial?.kategoriUsia ?: "Anak Usia Dini (0-6)") }
    var statusAkses by remember { mutableStateOf(initial?.statusAkses ?: "Bersekolah Aktif") }
    var jenisLayanan by remember { mutableStateOf(initial?.jenisLayanan ?: "PAUD / Kober") }
    var namaSekolah by remember { mutableStateOf(initial?.namaSekolah ?: "") }
    var kebutuhan by remember { mutableStateOf(initial?.kebutuhanPendidikan ?: "Bantuan Seragam / Perlengkapan Sekolah") }
    var catatanMonev by remember { mutableStateOf(initial?.catatanMonev ?: "") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Input Data SPM Pendidikan" else "Ubah Data SPM Pendidikan",
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
                        value = namaAnak,
                        onValueChange = { namaAnak = it },
                        label = { Text("Nama Lengkap Anak *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = nik,
                        onValueChange = { nik = it },
                        label = { Text("NIK Anak (16 digit)") },
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
                            value = usiaTahun,
                            onValueChange = { usiaTahun = it },
                            label = { Text("Usia (Tahun) *") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = kategoriUsia,
                            onValueChange = { kategoriUsia = it },
                            label = { Text("Kategori Usia") },
                            modifier = Modifier.weight(1.5f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = statusAkses,
                        onValueChange = { statusAkses = it },
                        label = { Text("Status Akses (Bersekolah/Putus/Belum)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = jenisLayanan,
                        onValueChange = { jenisLayanan = it },
                        label = { Text("Jenis Layanan (PAUD/TK/SD/Paket)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = namaSekolah,
                        onValueChange = { namaSekolah = it },
                        label = { Text("Nama Lembaga / Sekolah") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kebutuhan,
                        onValueChange = { kebutuhan = it },
                        label = { Text("Kebutuhan Pelayanan / Beasiswa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = catatanMonev,
                        onValueChange = { catatanMonev = it },
                        label = { Text("Catatan Monitoring & Evaluasi") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaAnak.isBlank()) {
                        formError = "Nama anak harus diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val entity = SpmPendidikanEntity(
                        id = initial?.id ?: 0,
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        tanggalInput = initial?.tanggalInput ?: "10/11/2026",
                        namaAnak = namaAnak.trim(),
                        nik = nik.trim(),
                        usiaTahun = usiaTahun.toIntOrNull() ?: 5,
                        kategoriUsia = kategoriUsia.trim(),
                        statusAkses = statusAkses.trim(),
                        jenisLayanan = jenisLayanan.trim(),
                        namaSekolah = namaSekolah.trim(),
                        kebutuhanPendidikan = kebutuhan.trim(),
                        catatanMonev = catatanMonev.trim(),
                        petugasInput = currentUser?.fullName ?: "Petugas Posyandu"
                    )
                    onSave(entity, initial != null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpmPendidikanColor)
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
