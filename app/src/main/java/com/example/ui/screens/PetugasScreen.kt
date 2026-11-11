package com.example.ui.screens

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
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetugasScreen(
    usersList: List<UserEntity>,
    onAddUser: (UserEntity) -> Unit,
    onUpdateUser: (UserEntity) -> Unit,
    onDeleteUser: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<UserEntity?>(null) }
    var userToDelete by remember { mutableStateOf<UserEntity?>(null) }

    val filteredList = remember(usersList, searchQuery) {
        usersList.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                    it.email.contains(searchQuery, ignoreCase = true) ||
                    (it.assignedKecamatan ?: "").contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingUser = null
                    showDialog = true
                },
                containerColor = IndramayuNavy,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Akun Petugas")
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
                    Text(
                        text = "Kelola Akun Petugas & Kader Posyandu",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Manajemen hak akses, wilayah penugasan, dan identitas petugas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama petugas, email, kecamatan...") },
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
                items(filteredList, key = { it.id }) { user ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (user.role == UserRole.ADMIN) IndramayuGold.copy(alpha = 0.2f) else PosyanduCyan.copy(alpha = 0.2f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (user.role == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (user.role == UserRole.ADMIN) IndramayuGoldDark else PosyanduCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.fullName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = if (user.role == UserRole.ADMIN) IndramayuGold.copy(alpha = 0.2f) else PosyanduCyan.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = user.role.name,
                                            color = if (user.role == UserRole.ADMIN) IndramayuGoldDark else PosyanduCyan,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (user.assignedKecamatan != null) {
                                    Text(
                                        text = "Wilayah: ${user.assignedKecamatan} • Desa ${user.assignedDesa ?: "-"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = IndramayuNavy,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                if (user.jabatan.isNotBlank()) {
                                    Text(
                                        text = "Jabatan: ${user.jabatan}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row {
                                IconButton(onClick = {
                                    editingUser = user
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = IndramayuNavyLight)
                                }
                                if (user.role != UserRole.ADMIN) {
                                    IconButton(onClick = { userToDelete = user }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = StatusError)
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
        UserFormDialog(
            initial = editingUser,
            onDismiss = {
                showDialog = false
                editingUser = null
            },
            onSave = { saved ->
                if (editingUser != null) {
                    onUpdateUser(saved)
                } else {
                    onAddUser(saved)
                }
                showDialog = false
                editingUser = null
            }
        )
    }

    userToDelete?.let { u ->
        ConfirmDeleteDialog(
            title = "Hapus Akun Pengguna",
            message = "Apakah Anda yakin ingin menghapus akun ${u.fullName} (${u.email})?",
            onConfirm = {
                onDeleteUser(u.id)
                userToDelete = null
            },
            onDismiss = { userToDelete = null }
        )
    }
}

@Composable
fun UserFormDialog(
    initial: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (UserEntity) -> Unit
) {
    var fullName by remember { mutableStateOf(initial?.fullName ?: "") }
    var email by remember { mutableStateOf(initial?.email ?: "") }
    var username by remember { mutableStateOf(initial?.username ?: "") }
    var role by remember { mutableStateOf(initial?.role ?: UserRole.PETUGAS) }
    var kecamatan by remember { mutableStateOf(initial?.assignedKecamatan ?: "Indramayu") }
    var desa by remember { mutableStateOf(initial?.assignedDesa ?: "Margadadi") }
    var jabatan by remember { mutableStateOf(initial?.jabatan ?: "Kader Posyandu") }
    var phone by remember { mutableStateOf(initial?.phone ?: "") }

    var formError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Tambah Akun Petugas" else "Ubah Data Petugas", fontWeight = FontWeight.Bold) },
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
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nama Lengkap Petugas *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Resmi *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username Login") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text("Hak Akses (Role):", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = role == UserRole.PETUGAS,
                            onClick = { role = UserRole.PETUGAS },
                            label = { Text("PETUGAS") }
                        )
                        FilterChip(
                            selected = role == UserRole.ADMIN,
                            onClick = { role = UserRole.ADMIN },
                            label = { Text("ADMIN") }
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = kecamatan,
                        onValueChange = { kecamatan = it },
                        label = { Text("Wilayah Tugas (Kecamatan)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = desa,
                        onValueChange = { desa = it },
                        label = { Text("Desa / Kelurahan Penugasan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = jabatan,
                        onValueChange = { jabatan = it },
                        label = { Text("Jabatan / Peran") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Nomor HP / WhatsApp") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fullName.isBlank() || email.isBlank()) {
                        formError = "Nama lengkap dan email wajib diisi."
                        return@Button
                    }
                    val user = UserEntity(
                        id = initial?.id ?: "USR_${System.currentTimeMillis() % 100000}",
                        email = email.trim(),
                        username = if (username.isNotBlank()) username.trim() else email.substringBefore("@"),
                        fullName = fullName.trim(),
                        role = role,
                        assignedKecamatan = kecamatan.trim(),
                        assignedDesa = desa.trim(),
                        jabatan = jabatan.trim(),
                        phone = phone.trim(),
                        passwordHash = initial?.passwordHash ?: "posyandu2026"
                    )
                    onSave(user)
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
