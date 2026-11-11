package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KecamatanEntity
import com.example.data.model.PosyanduEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosyanduScreen(
    posyanduList: List<PosyanduEntity>,
    kecamatanList: List<KecamatanEntity>,
    currentUser: UserEntity?,
    onAddPosyandu: (PosyanduEntity) -> Unit,
    onUpdatePosyandu: (PosyanduEntity) -> Unit,
    onDeletePosyandu: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedKecamatanFilter by remember { mutableStateOf("Semua Kecamatan") }
    var showFormDialog by remember { mutableStateOf(false) }
    var editingPosyandu by remember { mutableStateOf<PosyanduEntity?>(null) }
    var posyanduToDelete by remember { mutableStateOf<PosyanduEntity?>(null) }

    val filteredList = remember(posyanduList, searchQuery, selectedKecamatanFilter) {
        posyanduList.filter { p ->
            val matchesQuery = p.namaPosyandu.contains(searchQuery, ignoreCase = true) ||
                    p.desa.contains(searchQuery, ignoreCase = true) ||
                    p.kecamatan.contains(searchQuery, ignoreCase = true) ||
                    p.namaKetua.contains(searchQuery, ignoreCase = true)
            val matchesKec = selectedKecamatanFilter == "Semua Kecamatan" || p.kecamatan == selectedKecamatanFilter
            matchesQuery && matchesKec
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingPosyandu = null
                    showFormDialog = true
                },
                containerColor = IndramayuNavy,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Posyandu")
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
            // Header Info & Search
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
                                text = "Master Data Posyandu",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = IndramayuNavy
                            )
                            Text(
                                text = "Total ${filteredList.size} unit Posyandu di Kabupaten Indramayu",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama Posyandu, desa, kecamatan, atau ketua...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Hapus")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter Chip per Kecamatan
                    ScrollableTabRow(
                        selectedTabIndex = 0,
                        edgePadding = 0.dp,
                        divider = {},
                        indicator = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val filterOptions = listOf("Semua Kecamatan") + kecamatanList.map { it.namaKecamatan }
                        filterOptions.forEach { kec ->
                            val isSelected = selectedKecamatanFilter == kec
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedKecamatanFilter = kec },
                                label = { Text(kec, fontSize = 12.sp) },
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

            // List of Posyandu Cards
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
                                    Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Tidak Ada Data Posyandu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Coba ubah kata kunci pencarian atau filter kecamatan Anda.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { posyandu ->
                        PosyanduCardItem(
                            posyandu = posyandu,
                            canManage = currentUser?.role == UserRole.ADMIN || currentUser?.assignedPosyanduId == posyandu.id,
                            onEdit = {
                                editingPosyandu = posyandu
                                showFormDialog = true
                            },
                            onDelete = {
                                posyanduToDelete = posyandu
                            }
                        )
                    }
                }
            }
        }
    }

    // Add / Edit Dialog
    if (showFormDialog) {
        PosyanduFormDialog(
            initial = editingPosyandu,
            kecamatanList = kecamatanList,
            onDismiss = {
                showFormDialog = false
                editingPosyandu = null
            },
            onSave = { saved ->
                if (editingPosyandu != null) {
                    onUpdatePosyandu(saved)
                } else {
                    onAddPosyandu(saved)
                }
                showFormDialog = false
                editingPosyandu = null
            }
        )
    }

    // Delete Confirmation
    posyanduToDelete?.let { p ->
        ConfirmDeleteDialog(
            title = "Hapus Posyandu",
            message = "Apakah Anda yakin ingin menghapus '${p.namaPosyandu}' di Desa ${p.desa}, Kecamatan ${p.kecamatan}? Seluruh data terkait tidak dapat dipulihkan.",
            onConfirm = {
                onDeletePosyandu(p.id, p.namaPosyandu)
                posyanduToDelete = null
            },
            onDismiss = {
                posyanduToDelete = null
            }
        )
    }
}

@Composable
fun PosyanduCardItem(
    posyandu: PosyanduEntity,
    canManage: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
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
                            color = IndramayuNavy.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = posyandu.kodePosyandu,
                                color = IndramayuNavy,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (posyandu.statusAktif) StatusSuccess.copy(alpha = 0.12f) else StatusError.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (posyandu.statusAktif) "Aktif" else "Nonaktif",
                                color = if (posyandu.statusAktif) StatusSuccess else StatusError,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = IndramayuGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = posyandu.strataPosyandu,
                                color = IndramayuGoldDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = posyandu.namaPosyandu,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${posyandu.desa}, Kec. ${posyandu.kecamatan} (RT ${posyandu.rt} / RW ${posyandu.rw})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (canManage) {
                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = IndramayuNavyLight, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = StatusError, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = GovBorder.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

            // Details info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Ketua Posyandu",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = posyandu.namaKetua,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column {
                    Text(
                        text = "Total Sasaran",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${posyandu.totalSasaran} Jiwa",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                }

                Column {
                    Text(
                        text = "Jadwal Pelayanan",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = posyandu.hariBuka,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PosyanduFormDialog(
    initial: PosyanduEntity?,
    kecamatanList: List<KecamatanEntity>,
    onDismiss: () -> Unit,
    onSave: (PosyanduEntity) -> Unit
) {
    var namaPosyandu by remember { mutableStateOf(initial?.namaPosyandu ?: "") }
    var kodePosyandu by remember { mutableStateOf(initial?.kodePosyandu ?: "POS-IMY-${System.currentTimeMillis() % 1000}") }
    var kecamatan by remember { mutableStateOf(initial?.kecamatan ?: (kecamatanList.firstOrNull()?.namaKecamatan ?: "Indramayu")) }
    var desa by remember { mutableStateOf(initial?.desa ?: "Margadadi") }
    var rw by remember { mutableStateOf(initial?.rw ?: "01") }
    var rt by remember { mutableStateOf(initial?.rt ?: "01") }
    var alamat by remember { mutableStateOf(initial?.alamatLengkap ?: "") }
    var ketua by remember { mutableStateOf(initial?.namaKetua ?: "") }
    var kaderUtama by remember { mutableStateOf(initial?.namaKaderUtama ?: "") }
    var kontakHp by remember { mutableStateOf(initial?.kontakHp ?: "") }
    var sasaranBalita by remember { mutableStateOf(initial?.sasaranBalita?.toString() ?: "40") }
    var sasaranBumil by remember { mutableStateOf(initial?.sasaranBumil?.toString() ?: "10") }
    var sasaranLansia by remember { mutableStateOf(initial?.sasaranLansia?.toString() ?: "25") }
    var strata by remember { mutableStateOf(initial?.strataPosyandu ?: "Mandiri") }
    var hariBuka by remember { mutableStateOf(initial?.hariBuka ?: "Senin Minggu ke-2") }
    var statusAktif by remember { mutableStateOf(initial?.statusAktif ?: true) }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initial == null) "Tambah Posyandu Baru" else "Ubah Data Posyandu",
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
                        Text(
                            text = formError ?: "",
                            color = StatusError,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = namaPosyandu,
                        onValueChange = { namaPosyandu = it },
                        label = { Text("Nama Posyandu *") },
                        placeholder = { Text("Contoh: Posyandu Melati I") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kodePosyandu,
                        onValueChange = { kodePosyandu = it },
                        label = { Text("Kode Posyandu") },
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
                            value = kecamatan,
                            onValueChange = { kecamatan = it },
                            label = { Text("Kecamatan *") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = desa,
                            onValueChange = { desa = it },
                            label = { Text("Desa/Kel *") },
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
                            value = rt,
                            onValueChange = { rt = it },
                            label = { Text("RT") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = rw,
                            onValueChange = { rw = it },
                            label = { Text("RW") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text("Alamat Lengkap") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = ketua,
                        onValueChange = { ketua = it },
                        label = { Text("Nama Ketua Posyandu *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kaderUtama,
                        onValueChange = { kaderUtama = it },
                        label = { Text("Nama Kader Utama") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = kontakHp,
                        onValueChange = { kontakHp = it },
                        label = { Text("Kontak / HP") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = sasaranBalita,
                            onValueChange = { sasaranBalita = it },
                            label = { Text("Balita") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = sasaranBumil,
                            onValueChange = { sasaranBumil = it },
                            label = { Text("Bumil") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = sasaranLansia,
                            onValueChange = { sasaranLansia = it },
                            label = { Text("Lansia") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = strata,
                        onValueChange = { strata = it },
                        label = { Text("Strata (Pratama/Madya/Purnama/Mandiri)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = hariBuka,
                        onValueChange = { hariBuka = it },
                        label = { Text("Jadwal Buka Pelayanan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = statusAktif,
                            onCheckedChange = { statusAktif = it }
                        )
                        Text("Status Posyandu Aktif", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaPosyandu.isBlank() || kecamatan.isBlank() || desa.isBlank() || ketua.isBlank()) {
                        formError = "Mohon lengkapi kolom bertanda bintang (*)"
                        return@Button
                    }
                    val balita = sasaranBalita.toIntOrNull() ?: 0
                    val bumil = sasaranBumil.toIntOrNull() ?: 0
                    val lansia = sasaranLansia.toIntOrNull() ?: 0
                    val total = balita + bumil + lansia

                    val saved = PosyanduEntity(
                        id = initial?.id ?: 0,
                        kodePosyandu = kodePosyandu,
                        namaPosyandu = namaPosyandu.trim(),
                        kecamatan = kecamatan.trim(),
                        desa = desa.trim(),
                        rw = rw.trim(),
                        rt = rt.trim(),
                        alamatLengkap = alamat.trim(),
                        namaKetua = ketua.trim(),
                        namaKaderUtama = kaderUtama.trim(),
                        kontakHp = kontakHp.trim(),
                        sasaranBalita = balita,
                        sasaranBumil = bumil,
                        sasaranLansia = lansia,
                        totalSasaran = total,
                        statusAktif = statusAktif,
                        strataPosyandu = strata.trim(),
                        hariBuka = hariBuka.trim()
                    )
                    onSave(saved)
                },
                colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
