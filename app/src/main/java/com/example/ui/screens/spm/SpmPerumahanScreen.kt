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
import com.example.data.model.SpmPerumahanEntity
import com.example.data.model.UserEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.SpmBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpmPerumahanScreen(
    dataList: List<SpmPerumahanEntity>,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onSave: (SpmPerumahanEntity, Boolean) -> Unit,
    onDelete: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<SpmPerumahanEntity?>(null) }
    var itemToDelete by remember { mutableStateOf<SpmPerumahanEntity?>(null) }

    val filteredList = remember(dataList, searchQuery) {
        dataList.filter {
            it.namaKepalaKeluarga.contains(searchQuery, ignoreCase = true) ||
                    it.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    it.statusKelayakan.contains(searchQuery, ignoreCase = true) ||
                    it.alamatRumah.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showDialog = true
                },
                containerColor = SpmPerumahanColor,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Data RTLH")
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
                    SpmBadge(bidangSpm = BidangSpm.PERUMAHAN_RAKYAT)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Modul Bidang Perumahan Rakyat",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Pendataan Rumah Tidak Layak Huni (RTLH), Kondisi Atap, Dinding, Lantai, & Rehabilitasi",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama KK, status RTLH, alamat...") },
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
                                    Icons.Default.Home,
                                    contentDescription = null,
                                    tint = SpmPerumahanColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Belum Ada Data SPM Perumahan", fontWeight = FontWeight.Bold)
                                Text(
                                    "Tekan tombol (+) untuk mendata kondisi rumah warga dan RTLH.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { item ->
                        val isHeavy = item.statusKelayakan.contains("Berat", ignoreCase = true)
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
                                            color = if (isHeavy) StatusError.copy(alpha = 0.15f) else SpmPerumahanColor.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = item.statusKelayakan,
                                                color = if (isHeavy) StatusError else SpmPerumahanColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = item.namaKepalaKeluarga,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "${item.alamatRumah} (Posyandu ${item.namaPosyandu})",
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
                                        Text("Kondisi Atap", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kondisiAtap, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Dinding", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kondisiDinding, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Lantai", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(item.kondisiLantai, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = IndramayuGold.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Prioritas: ${item.prioritasRehabilitasi}",
                                            color = IndramayuGoldDark,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Text(
                                        text = item.usulanBantuan,
                                        style = MaterialTheme.typography.labelSmall,
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
        PerumahanFormDialog(
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
            title = "Hapus Data SPM Perumahan",
            message = "Apakah Anda yakin ingin menghapus data RTLH KK ${itm.namaKepalaKeluarga}?",
            onConfirm = {
                onDelete(itm.id, itm.namaKepalaKeluarga)
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun PerumahanFormDialog(
    initial: SpmPerumahanEntity?,
    posyanduList: List<PosyanduEntity>,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (SpmPerumahanEntity, Boolean) -> Unit
) {
    val defaultPos = posyanduList.firstOrNull()
    var selectedPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: (defaultPos?.namaPosyandu ?: "Posyandu Melati I")) }
    var namaKK by remember { mutableStateOf(initial?.namaKepalaKeluarga ?: "") }
    var nikKK by remember { mutableStateOf(initial?.nikKk ?: "") }
    var alamat by remember { mutableStateOf(initial?.alamatRumah ?: "") }
    var kelayakan by remember { mutableStateOf(initial?.statusKelayakan ?: "RTLH Rusak Sedang") }
    var atap by remember { mutableStateOf(initial?.kondisiAtap ?: "Asbes/Seng Berkarat") }
    var dinding by remember { mutableStateOf(initial?.kondisiDinding ?: "Bambu/Anyaman/Kayu Lapuk") }
    var lantai by remember { mutableStateOf(initial?.kondisiLantai ?: "Plester Semen Retak") }
    var prioritas by remember { mutableStateOf(initial?.prioritasRehabilitasi ?: "Mendesak (Prioritas 2)") }
    var usulan by remember { mutableStateOf(initial?.usulanBantuan ?: "Program Rutilahu DPMD Indramayu") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Input Pendataan RTLH (Perumahan)" else "Ubah Data RTLH",
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
                        value = namaKK,
                        onValueChange = { namaKK = it },
                        label = { Text("Nama Kepala Keluarga *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = nikKK,
                        onValueChange = { nikKK = it },
                        label = { Text("NIK / Nomor KK") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text("Alamat Rumah Lengkap *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kelayakan,
                        onValueChange = { kelayakan = it },
                        label = { Text("Status Kelayakan Rumah") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = atap,
                            onValueChange = { atap = it },
                            label = { Text("Kondisi Atap") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = dinding,
                            onValueChange = { dinding = it },
                            label = { Text("Kondisi Dinding") },
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
                            value = lantai,
                            onValueChange = { lantai = it },
                            label = { Text("Kondisi Lantai") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = prioritas,
                            onValueChange = { prioritas = it },
                            label = { Text("Tingkat Prioritas") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = usulan,
                        onValueChange = { usulan = it },
                        label = { Text("Program Usulan Bantuan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaKK.isBlank() || alamat.isBlank()) {
                        formError = "Nama KK dan Alamat wajib diisi."
                        return@Button
                    }
                    val posObj = posyanduList.firstOrNull { it.namaPosyandu == selectedPosyandu } ?: defaultPos
                    val entity = SpmPerumahanEntity(
                        id = initial?.id ?: 0,
                        posyanduId = posObj?.id ?: 1L,
                        namaPosyandu = posObj?.namaPosyandu ?: selectedPosyandu,
                        kecamatan = posObj?.kecamatan ?: "Indramayu",
                        desa = posObj?.desa ?: "Margadadi",
                        tanggalPendataan = initial?.tanggalPendataan ?: "10/11/2026",
                        namaKepalaKeluarga = namaKK.trim(),
                        nikKk = nikKK.trim(),
                        alamatRumah = alamat.trim(),
                        statusKelayakan = kelayakan.trim(),
                        kondisiAtap = atap.trim(),
                        kondisiDinding = dinding.trim(),
                        kondisiLantai = lantai.trim(),
                        prioritasRehabilitasi = prioritas.trim(),
                        usulanBantuan = usulan.trim(),
                        petugasInput = currentUser?.fullName ?: "Petugas Posyandu"
                    )
                    onSave(entity, initial != null)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpmPerumahanColor)
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
