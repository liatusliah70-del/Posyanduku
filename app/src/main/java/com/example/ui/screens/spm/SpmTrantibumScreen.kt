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
import com.example.data.model.SpmTrantibumEntity
import com.example.data.model.UserEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpmTrantibumScreen(
    dataList: List<SpmTrantibumEntity>,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onSave: (SpmTrantibumEntity, Boolean) -> Unit,
    onDelete: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<SpmTrantibumEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<SpmTrantibumEntity?>(null) }

    val filteredList = remember(dataList, searchQuery) {
        dataList.filter {
            it.wilayahRtRw.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.kondisiKeamanan.contains(searchQuery, ignoreCase = true) ||
                    it.potensiKebencanaan.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showDialog = true
                },
                containerColor = SpmTrantibumColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Data Trantibum")
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
                    SpmBadge(bidangSpm = BidangSpm.TRANTIBUMLINMAS)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Modul Bidang Trantibumlinmas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Ketenteraman, Ketertiban Umum, Pos Ronda, Satlinmas, & Mitigasi Kerawanan Bencana",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari wilayah RT/RW, keamanan, potensi bencana...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
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
                                    Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = SpmTrantibumColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Belum Ada Data SPM Trantibumlinmas", fontWeight = FontWeight.Bold)
                                Text(
                                    "Tekan tombol (+) untuk mencatat situasi ketertiban dan perlindungan warga.",
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
                                        Text(
                                            text = item.wilayahRtRw,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Posyandu: ${item.namaPosyandu} (${item.desa}, ${item.kecamatan})",
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
                                        Text("Pos Ronda", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.statusPosRonda, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Anggota Linmas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("${item.jumlahAnggotaLinmas} Orang", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = IndramayuNavy)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Situasi Keamanan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kondisiKeamanan, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Column {
                                        Text("Potensi Bencana", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.potensiKebencanaan, style = MaterialTheme.typography.bodySmall, color = if (item.potensiKebencanaan.contains("Aman")) StatusSuccess else StatusWarning, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                if (item.kebutuhanPerlindunganWarga.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Kebutuhan Linmas: ${item.kebutuhanPerlindunganWarga}",
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
        TrantibumFormDialog(
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
            title = "Hapus Data SPM Trantibumlinmas",
            message = "Apakah Anda yakin ingin menghapus data kamtibmas di ${itm.wilayahRtRw}?",
            onConfirm = {
                onDelete(itm.id, itm.wilayahRtRw)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun TrantibumFormDialog(
    initial: SpmTrantibumEntity?,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (SpmTrantibumEntity, Boolean) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: (defaultPos?.namaPosyandu ?: "Posyandu Melati I")) }
    var wilayah by remember { mutableStateOf(initial?.wilayahRtRw ?: "") }
    var posRonda by remember { mutableStateOf(initial?.statusPosRonda ?: "Pos Ronda Aktif 24 Jam") }
    var linmasCount by remember { mutableStateOf(initial?.jumlahAnggotaLinmas?.toString() ?: "4") }
    var keamanan by remember { mutableStateOf(initial?.kondisiKeamanan ?: "Lingkungan Aman Kondusif") }
    var bencana by remember { mutableStateOf(initial?.potensiKebencanaan ?: "Aman dari Bencana") }
    var kebutuhan by remember { mutableStateOf(initial?.kebutuhanPerlindunganWarga ?: "Penerangan Jalan Lingkungan (PJU) & Perlengkapan Satkamling") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Input Data Trantibumlinmas" else "Ubah Data Trantibum",
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
                        value = wilayah,
                        onValueChange = { wilayah = it },
                        label = { Text("Wilayah RT / RW / Dusun *") },
                        placeholder = { Text("Contoh: RT 04 / RW 02 Kelurahan Margadadi") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = posRonda,
                        onValueChange = { posRonda = it },
                        label = { Text("Status Pos Ronda / Kamling") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = linmasCount,
                        onValueChange = { linmasCount = it },
                        label = { Text("Jumlah Anggota Linmas Aktif") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = keamanan,
                        onValueChange = { keamanan = it },
                        label = { Text("Kondisi Keamanan Lingkungan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = bencana,
                        onValueChange = { bencana = it },
                        label = { Text("Potensi Kerawanan Bencana (Banjir Rob/Angin)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kebutuhan,
                        onValueChange = { kebutuhan = it },
                        label = { Text("Kebutuhan Perlindungan Warga") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (wilayah.isBlank()) {
                        formError = "Wilayah RT/RW wajib diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val entity = SpmTrantibumEntity(
                        id = initial?.id ?: 0,
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        tanggalInput = initial?.tanggalInput ?: "10/11/2026",
                        wilayahRtRw = wilayah.trim(),
                        statusPosRonda = posRonda.trim(),
                        jumlahAnggotaLinmas = linmasCount.toIntOrNull() ?: 4,
                        kondisiKeamanan = keamanan.trim(),
                        potensiKebencanaan = bencana.trim(),
                        kebutuhanPerlindunganWarga = kebutuhan.trim(),
                        petugasInput = currentUser?.fullName ?: "Petugas Posyandu"
                    )
                    onSave(entity, initial != null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpmTrantibumColor)
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
