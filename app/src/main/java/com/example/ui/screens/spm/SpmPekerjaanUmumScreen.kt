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
import com.example.data.model.SpmPekerjaanUmumEntity
import com.example.data.model.UserEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpmPekerjaanUmumScreen(
    dataList: List<SpmPekerjaanUmumEntity>,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onSave: (SpmPekerjaanUmumEntity, Boolean) -> Unit,
    onDelete: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<SpmPekerjaanUmumEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<SpmPekerjaanUmumEntity?>(null) }

    val filteredList = remember(dataList, searchQuery) {
        dataList.filter {
            it.lokasiDusunRwRt.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.aksesAirMinum.contains(searchQuery, ignoreCase = true) ||
                    it.aksesSanitasiJamban.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showDialog = true
                },
                containerColor = SpmPekerjaanUmumColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Data PU")
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
                    SpmBadge(bidangSpm = BidangSpm.PEKERJAAN_UMUM)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Modul Bidang Pekerjaan Umum",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Pendataan Akses Air Minum Layak, Sanitasi Sehat, Drainase, & Sarpras Lingkungan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari lokasi dusun, air minum, sanitasi...") },
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
                                    Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = SpmPekerjaanUmumColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Belum Ada Data SPM Pekerjaan Umum", fontWeight = FontWeight.Bold)
                                Text(
                                    "Tekan tombol (+) untuk mencatat kondisi sanitasi dan sarpras desa.",
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
                                            text = item.lokasiDusunRwRt,
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
                                        Text("Air Minum", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.aksesAirMinum, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = IndramayuNavy)
                                    }
                                    Column {
                                        Text("Sanitasi Jamban", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.aksesSanitasiJamban, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Drainase", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kondisiDrainase, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Column {
                                        Text("Jalan Lingkungan", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kondisiJalanLingkungan, style = MaterialTheme.typography.bodySmall)
                                    }
                                }

                                if (item.kebutuhanSarprasPrioritas.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        color = SpmPekerjaanUmumColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Kebutuhan Prioritas: ${item.kebutuhanSarprasPrioritas}",
                                            color = SpmPekerjaanUmumColor,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        PekerjaanUmumFormDialog(
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
            title = "Hapus Data SPM Pekerjaan Umum",
            message = "Apakah Anda yakin ingin menghapus data infrastruktur di ${itm.lokasiDusunRwRt}?",
            onConfirm = {
                onDelete(itm.id, itm.lokasiDusunRwRt)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun PekerjaanUmumFormDialog(
    initial: SpmPekerjaanUmumEntity?,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (SpmPekerjaanUmumEntity, Boolean) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: (defaultPos?.namaPosyandu ?: "Posyandu Melati I")) }
    var lokasiDusun by remember { mutableStateOf(initial?.lokasiDusunRwRt ?: "") }
    var airMinum by remember { mutableStateOf(initial?.aksesAirMinum ?: "Jaringan Perpipaan PDAM") }
    var sanitasi by remember { mutableStateOf(initial?.aksesSanitasiJamban ?: "Jamban Sehat Leher Angsa (Septic Tank)") }
    var drainase by remember { mutableStateOf(initial?.kondisiDrainase ?: "Berfungsi Baik & Bersih") }
    var jalan by remember { mutableStateOf(initial?.kondisiJalanLingkungan ?: "Rabat Beton / Aspal Baik") }
    var kebutuhan by remember { mutableStateOf(initial?.kebutuhanSarprasPrioritas ?: "Pembangunan SPAM Air Bersih Lingkungan") }
    var deskripsi by remember { mutableStateOf(initial?.deskripsiKondisi ?: "") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Input Sarpras & Air Bersih (PU)" else "Ubah Data PU",
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
                        value = lokasiDusun,
                        onValueChange = { lokasiDusun = it },
                        label = { Text("Lokasi Dusun / RT / RW *") },
                        placeholder = { Text("Contoh: Blok Karang Malang RT 04 RW 02") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = airMinum,
                        onValueChange = { airMinum = it },
                        label = { Text("Akses Air Minum Layak") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = sanitasi,
                        onValueChange = { sanitasi = it },
                        label = { Text("Akses Sanitasi / Jamban") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = drainase,
                        onValueChange = { drainase = it },
                        label = { Text("Kondisi Drainase Lingkungan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = jalan,
                        onValueChange = { jalan = it },
                        label = { Text("Kondisi Jalan Lingkungan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kebutuhan,
                        onValueChange = { kebutuhan = it },
                        label = { Text("Kebutuhan Sarpras Prioritas") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = deskripsi,
                        onValueChange = { deskripsi = it },
                        label = { Text("Catatan Lapangan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (lokasiDusun.isBlank()) {
                        formError = "Lokasi dusun/RT/RW wajib diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val entity = SpmPekerjaanUmumEntity(
                        id = initial?.id ?: 0,
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        tanggalInput = initial?.tanggalInput ?: "10/11/2026",
                        lokasiDusunRwRt = lokasiDusun.trim(),
                        aksesAirMinum = airMinum.trim(),
                        aksesSanitasiJamban = sanitasi.trim(),
                        kondisiDrainase = drainase.trim(),
                        kondisiJalanLingkungan = jalan.trim(),
                        kebutuhanSarprasPrioritas = kebutuhan.trim(),
                        deskripsiKondisi = deskripsi.trim(),
                        petugasInput = currentUser?.fullName ?: "Petugas Posyandu"
                    )
                    onSave(entity, initial != null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpmPekerjaanUmumColor)
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
