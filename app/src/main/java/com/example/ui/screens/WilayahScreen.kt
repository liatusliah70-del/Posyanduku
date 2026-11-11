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
import com.example.data.model.DesaEntity
import com.example.data.model.KecamatanEntity
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WilayahScreen(
    kecamatanList: List<KecamatanEntity>,
    desaList: List<DesaEntity>,
    onAddKecamatan: (KecamatanEntity) -> Unit,
    onAddDesa: (DesaEntity) -> Unit,
    onDeleteKecamatan: (String) -> Unit,
    onDeleteDesa: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Kecamatan, 1: Desa/Kelurahan
    var showAddKecDialog by remember { mutableStateOf(false) }
    var showAddDesaDialog by remember { mutableStateOf(false) }
    var kecToDelete by remember { mutableStateOf<KecamatanEntity?>(null) }
    var desaToDelete by remember { mutableStateOf<DesaEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GovBackgroundLight)
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Master Data Wilayah",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = IndramayuNavy
                )
                Text(
                    text = "Hierarki Wilayah: Kabupaten Indramayu > Kecamatan > Desa/Kelurahan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IndramayuNavy
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Kecamatan (${kecamatanList.size})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Desa / Kelurahan (${desaList.size})", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari wilayah...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (selectedTab == 0) showAddKecDialog = true else showAddDesaDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (selectedTab == 0) "Kecamatan" else "Desa")
                    }
                }
            }
        }

        // List Content
        if (selectedTab == 0) {
            val filteredKec = kecamatanList.filter {
                it.namaKecamatan.contains(searchQuery, ignoreCase = true) ||
                        it.kodeKecamatan.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredKec, key = { it.id }) { kec ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = IndramayuNavy.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = kec.kodeKecamatan,
                                            color = IndramayuNavy,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Kecamatan ${kec.namaKecamatan}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Camat: ${kec.camatName} • ${kec.jumlahDesa} Desa/Kelurahan",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { kecToDelete = kec }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = StatusError)
                            }
                        }
                    }
                }
            }
        } else {
            val filteredDesa = desaList.filter {
                it.namaDesa.contains(searchQuery, ignoreCase = true) ||
                        it.namaKecamatan.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredDesa, key = { it.id }) { desa ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = PosyanduTeal.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = desa.jenis,
                                            color = PosyanduTeal,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = desa.namaDesa,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Kecamatan ${desa.namaKecamatan} • Kuwu/Lurah: ${desa.kepalaDesa}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { desaToDelete = desa }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = StatusError)
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Kecamatan Dialog
    if (showAddKecDialog) {
        var namaKec by remember { mutableStateOf("") }
        var kodeKec by remember { mutableStateOf("32.12.") }
        var camat by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddKecDialog = false },
            title = { Text("Tambah Kecamatan Baru", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = namaKec,
                        onValueChange = { namaKec = it },
                        label = { Text("Nama Kecamatan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = kodeKec,
                        onValueChange = { kodeKec = it },
                        label = { Text("Kode Kecamatan (Kemendagri)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = camat,
                        onValueChange = { camat = it },
                        label = { Text("Nama Camat") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (namaKec.isNotBlank()) {
                        val newKec = KecamatanEntity(
                            id = "KEC_${System.currentTimeMillis() % 10000}",
                            namaKecamatan = namaKec.trim(),
                            kodeKecamatan = kodeKec.trim(),
                            jumlahDesa = 10,
                            camatName = camat.trim()
                        )
                        onAddKecamatan(newKec)
                        showAddKecDialog = false
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddKecDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Add Desa Dialog
    if (showAddDesaDialog) {
        var namaDesa by remember { mutableStateOf("") }
        var kecName by remember { mutableStateOf(kecamatanList.firstOrNull()?.namaKecamatan ?: "Indramayu") }
        var jenis by remember { mutableStateOf("Desa") }
        var kepalaDesa by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDesaDialog = false },
            title = { Text("Tambah Desa / Kelurahan", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = namaDesa,
                        onValueChange = { namaDesa = it },
                        label = { Text("Nama Desa / Kelurahan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = kecName,
                        onValueChange = { kecName = it },
                        label = { Text("Nama Kecamatan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = jenis,
                        onValueChange = { jenis = it },
                        label = { Text("Jenis (Desa / Kelurahan)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = kepalaDesa,
                        onValueChange = { kepalaDesa = it },
                        label = { Text("Kuwu / Kepala Desa / Lurah") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (namaDesa.isNotBlank()) {
                        val newDesa = DesaEntity(
                            id = "DESA_${System.currentTimeMillis() % 10000}",
                            kecamatanId = "KEC_REF",
                            namaKecamatan = kecName.trim(),
                            namaDesa = namaDesa.trim(),
                            jenis = jenis.trim(),
                            kepalaDesa = kepalaDesa.trim()
                        )
                        onAddDesa(newDesa)
                        showAddDesaDialog = false
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDesaDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Delete dialogs
    kecToDelete?.let { k ->
        ConfirmDeleteDialog(
            title = "Hapus Kecamatan",
            message = "Apakah Anda yakin ingin menghapus data Kecamatan ${k.namaKecamatan}?",
            onConfirm = {
                onDeleteKecamatan(k.id)
                kecToDelete = null
            },
            onDismiss = { kecToDelete = null }
        )
    }

    desaToDelete?.let { d ->
        ConfirmDeleteDialog(
            title = "Hapus Desa",
            message = "Apakah Anda yakin ingin menghapus data ${d.jenis} ${d.namaDesa} di Kecamatan ${d.namaKecamatan}?",
            onConfirm = {
                onDeleteDesa(d.id)
                desaToDelete = null
            },
            onDismiss = { desaToDelete = null }
        )
    }
}
