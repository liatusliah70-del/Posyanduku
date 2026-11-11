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
import com.example.data.model.SpmSosialEntity
import com.example.data.model.UserEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpmSosialScreen(
    dataList: List<SpmSosialEntity>,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onSave: (SpmSosialEntity, Boolean) -> Unit,
    onDelete: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedKategoriFilter by remember { mutableStateOf("Semua Kategori") }
    var showDialog by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<SpmSosialEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<SpmSosialEntity?>(null) }

    val filteredList = remember(dataList, searchQuery, selectedKategoriFilter) {
        dataList.filter {
            val matchesQuery = it.namaWarga.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.kategoriSosial.contains(searchQuery, ignoreCase = true) ||
                    it.statusPenerimaanBansos.contains(searchQuery, ignoreCase = true)
            val matchesCat = selectedKategoriFilter == "Semua Kategori" || it.kategoriSosial.contains(selectedKategoriFilter, ignoreCase = true)
            matchesQuery && matchesCat
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showDialog = true
                },
                containerColor = SpmSosialColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Data Sosial")
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
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SpmBadge(bidangSpm = BidangSpm.SOSIAL)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Modul Bidang Sosial",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Pendataan Lansia Rentan, Penyandang Disabilitas, Anak Terlantar, & Kepesertaan Bansos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama warga, bansos, kategori...") },
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
                        val filters = listOf("Semua Kategori", "Lanjut Usia", "Disabilitas", "Keluarga Miskin", "Anak")
                        filters.forEach { f ->
                            val isSel = selectedKategoriFilter == f
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedKategoriFilter = f },
                                label = { Text(f, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SpmSosialColor,
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
                                    Icons.Default.Diversity3,
                                    contentDescription = null,
                                    tint = SpmSosialColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Belum Ada Data SPM Sosial", fontWeight = FontWeight.Bold)
                                Text(
                                    "Tekan tombol (+) untuk mencatat data warga rentan dan bansos.",
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
                                            color = SpmSosialColor.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = item.kategoriSosial,
                                                color = SpmSosialColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = item.namaWarga,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Usia: ${item.usiaTahun} Th • Posyandu: ${item.namaPosyandu} (${item.desa})",
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
                                        Text("Status Bansos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.statusPenerimaanBansos, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = IndramayuNavy)
                                    }
                                    Column {
                                        Text("Kebutuhan Pelayanan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kebutuhanPelayananSosial.take(24) + "...", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                if (item.jenisDisabilitas.isNotBlank() && item.jenisDisabilitas != "-") {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Kondisi Disabilitas: ${item.jenisDisabilitas}",
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

    if (showDialog) {
        SosialFormDialog(
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
            title = "Hapus Data SPM Sosial",
            message = "Apakah Anda yakin ingin menghapus data sosial warga an. ${itm.namaWarga}?",
            onConfirm = {
                onDelete(itm.id, itm.namaWarga)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun SosialFormDialog(
    initial: SpmSosialEntity?,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (SpmSosialEntity, Boolean) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: (defaultPos?.namaPosyandu ?: "Posyandu Melati I")) }
    var namaWarga by remember { mutableStateOf(initial?.namaWarga ?: "") }
    var nik by remember { mutableStateOf(initial?.nik ?: "") }
    var usia by remember { mutableStateOf(initial?.usiaTahun?.toString() ?: "65") }
    var kategori by remember { mutableStateOf(initial?.kategoriSosial ?: "Lanjut Usia Terlantar / Rentan") }
    var disabilitas by remember { mutableStateOf(initial?.jenisDisabilitas ?: "-") }
    var bansos by remember { mutableStateOf(initial?.statusPenerimaanBansos ?: "Penerima BPNT/Sembako") }
    var kebutuhan by remember { mutableStateOf(initial?.kebutuhanPelayananSosial ?: "Bantuan Pangan Sembako & Home Care") }
    var catatan by remember { mutableStateOf(initial?.catatanKondisi ?: "") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Input Pendataan Sosial Warga" else "Ubah Data Sosial",
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
                        value = namaWarga,
                        onValueChange = { namaWarga = it },
                        label = { Text("Nama Lengkap Warga *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = nik,
                        onValueChange = { nik = it },
                        label = { Text("NIK (16 digit)") },
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
                            value = usia,
                            onValueChange = { usia = it },
                            label = { Text("Usia (Tahun)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = kategori,
                            onValueChange = { kategori = it },
                            label = { Text("Kategori Sosial") },
                            modifier = Modifier.weight(1.5f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = disabilitas,
                        onValueChange = { disabilitas = it },
                        label = { Text("Jenis Disabilitas (jika ada)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = bansos,
                        onValueChange = { bansos = it },
                        label = { Text("Status Penerimaan Bansos (PKH/BPNT/BLT)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kebutuhan,
                        onValueChange = { kebutuhan = it },
                        label = { Text("Kebutuhan Pelayanan (Alat Bantu/Sembako)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = catatan,
                        onValueChange = { catatan = it },
                        label = { Text("Catatan Lapangan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaWarga.isBlank()) {
                        formError = "Nama warga wajib diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val entity = SpmSosialEntity(
                        id = initial?.id ?: 0,
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        tanggalPendataan = initial?.tanggalPendataan ?: "10/11/2026",
                        namaWarga = namaWarga.trim(),
                        nik = nik.trim(),
                        usiaTahun = usia.toIntOrNull() ?: 65,
                        kategoriSosial = kategori.trim(),
                        jenisDisabilitas = disabilitas.trim(),
                        statusPenerimaanBansos = bansos.trim(),
                        kebutuhanPelayananSosial = kebutuhan.trim(),
                        catatanKondisi = catatan.trim(),
                        petugasInput = currentUser?.fullName ?: "Petugas Posyandu"
                    )
                    onSave(entity, initial != null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpmSosialColor)
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
