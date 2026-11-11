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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AuditLogEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun PengaturanScreen(
    currentUser: UserEntity?,
    auditLogs: List<AuditLogEntity>,
    onChangePassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showFirebaseGuideDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GovBackgroundLight),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Profil Pengguna Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(
                                    if (currentUser?.role == UserRole.ADMIN) IndramayuGold.copy(alpha = 0.2f) else PosyanduCyan.copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (currentUser?.role == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                contentDescription = null,
                                tint = if (currentUser?.role == UserRole.ADMIN) IndramayuGoldDark else PosyanduCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentUser?.fullName ?: "Pengguna",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${currentUser?.role?.name} • ${currentUser?.email}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (currentUser?.assignedKecamatan != null) {
                                Text(
                                    text = "Wilayah: Kec. ${currentUser.assignedKecamatan} (Desa ${currentUser.assignedDesa ?: "-"})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IndramayuNavy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = GovBorder.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Keamanan Akun", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Kata Sandi Terproteksi", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(
                            onClick = { showChangePasswordDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ubah Password", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 2. Status Integrasi Database & Firebase Firestore
        item {
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
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = IndramayuNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Integrasi Database & Cloud Backend",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = StatusSuccess.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Storage, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Database Lokal: Aktif & Berjalan", fontWeight = FontWeight.Bold, color = StatusSuccess, fontSize = 13.sp)
                                Text("Room Database (SQLite) menyimpan seluruh data 6 bidang SPM secara persisten di perangkat.", fontSize = 11.sp, color = Color.DarkGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = IndramayuGold.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = IndramayuGoldDark, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Sinkronisasi Cloud Firestore", fontWeight = FontWeight.Bold, color = IndramayuGoldDark, fontSize = 13.sp)
                                Text("Siap dihubungkan ke Firebase Cloud Firestore untuk sinkronisasi multi-perangkat real-time.", fontSize = 11.sp, color = Color.DarkGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showFirebaseGuideDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.SettingsSuggest, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Petunjuk Konfigurasi Cloud Backend", fontSize = 12.sp)
                    }
                }
            }
        }

        // 3. Audit Log Riwayat Aktivitas
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Audit Log & Keamanan Sistem",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Riwayat aktivitas verifikasi, input, dan perubahan data pengguna",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (auditLogs.isEmpty()) {
                        Text("Belum ada log tercatat.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        auditLogs.take(10).forEach { log ->
                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = log.action,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = IndramayuNavy
                                    )
                                    Text(
                                        text = log.waktuFormatted,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                }
                                Text(
                                    text = log.details,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Oleh: ${log.userName} (${log.userRole})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp
                                )
                            }
                            Divider(color = GovBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        // 4. Informasi Sistem & Pengembang
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tentang Aplikasi POSYANDUKU", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Pemerintah Kabupaten Indramayu\nDinas Pemberdayaan Masyarakat dan Desa (DPMD)\nBidang Standar Pelayanan Minimal (SPM)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Versi: 1.0.0 Stable (2026)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Text("Pengembang: @beben_dpmd_imyu 2026", style = MaterialTheme.typography.labelSmall, color = IndramayuNavy, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Ubah Kata Sandi Akun", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (passError != null) {
                        Text(text = passError ?: "", color = StatusError, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Kata Sandi Baru") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Ulangi Kata Sandi Baru") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPassword.length < 6) {
                            passError = "Kata sandi minimal 6 karakter."
                            return@Button
                        }
                        if (newPassword != confirmPassword) {
                            passError = "Konfirmasi kata sandi tidak cocok."
                            return@Button
                        }
                        onChangePassword(newPassword)
                        showChangePasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Firebase Architecture & Config Guide Dialog
    if (showFirebaseGuideDialog) {
        AlertDialog(
            onDismissRequest = { showFirebaseGuideDialog = false },
            icon = { Icon(Icons.Default.Cloud, contentDescription = null, tint = IndramayuNavy) },
            title = { Text("Petunjuk Integrasi Cloud Firestore", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Sesuai standar teknis Master Prompt:\n" +
                                "1. Aplikasi saat ini menggunakan penyimpanan data lokal Room Database yang sudah berfungsi penuh (CRUD, pencarian, filter, dan verifikasi).\n" +
                                "2. Skema entitas (User, Posyandu, 6 SPM, Laporan) telah dirancang identik dengan koleksi Cloud Firestore.\n" +
                                "3. Untuk mengaktifkan sinkronisasi cloud online, file google-services.json resmi dari konsol Firebase Pemkab Indramayu dapat ditempatkan di folder app/ dan Firestore dihubungkan langsung tanpa mengubah struktur data.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showFirebaseGuideDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}
