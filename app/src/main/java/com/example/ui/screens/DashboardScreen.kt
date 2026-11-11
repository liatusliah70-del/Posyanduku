package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AppScreen
import com.example.R
import com.example.data.model.AuditLogEntity
import com.example.data.model.PosyanduEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    currentUser: UserEntity?,
    totalKecamatan: Int,
    totalDesa: Int,
    totalPosyandu: Int,
    totalPetugas: Int,
    totalSasaran: Int,
    totalLaporan: Int,
    verifiedLaporan: Int,
    pendingLaporan: Int,
    countPendidikan: Int,
    countKesehatan: Int,
    countPU: Int,
    countPerumahan: Int,
    countTrantibum: Int,
    countSosial: Int,
    posyanduList: List<PosyanduEntity>,
    recentLogs: List<AuditLogEntity>,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val kecamatanGrouped = posyanduList
        .groupBy { it.kecamatan }
        .map { (kec, list) -> kec to list.size }
        .sortedByDescending { it.second }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GovBackgroundLight),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Banner & Greeting featuring Petugas & Kader Posyandu
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = IndramayuNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(IndramayuNavyDark, IndramayuNavy, Color(0xFF1E40AF))
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 14.dp, bottom = 8.dp, end = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1.35f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "KABUPATEN INDRAMAYU",
                                style = MaterialTheme.typography.titleMedium,
                                color = IndramayuGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Selamat Datang,",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Text(
                                text = currentUser?.fullName ?: "Petugas Posyandu",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (currentUser?.role == UserRole.ADMIN) {
                                    "Administrator DPMD • Pemantauan 6 Bidang SPM Posyandu"
                                } else {
                                    "Petugas Wilayah ${currentUser?.assignedKecamatan ?: "Indramayu"} • Desa ${currentUser?.assignedDesa ?: "-"}"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(StatusSuccess, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Kader Aktif & Siap Melayani",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        // Friendly Posyandu Health Workers transparent illustration
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(190.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kader_posyandu),
                                contentDescription = "Petugas dan Kader Posyandu Indramayu",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }

        // 2. Ringkasan Statistik Utama (Grid of Cards)
        item {
            Text(
                text = "Statistik Pelayanan & Kelembagaan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Posyandu",
                        value = "$totalPosyandu",
                        subtitle = "Unit Terdaftar",
                        icon = Icons.Default.Storefront,
                        iconTint = IndramayuNavy,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(AppScreen.POSYANDU) }
                    )
                    StatCard(
                        title = "Sasaran Warga",
                        value = "$totalSasaran",
                        subtitle = "Balita, Bumil, Lansia",
                        icon = Icons.Default.Groups,
                        iconTint = PosyanduCyan,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Kecamatan",
                        value = "$totalKecamatan",
                        subtitle = "$totalDesa Desa/Kelurahan",
                        icon = Icons.Default.LocationCity,
                        iconTint = SpmPekerjaanUmumColor,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (currentUser?.role == UserRole.ADMIN) onNavigate(AppScreen.WILAYAH)
                            }
                    )
                    StatCard(
                        title = "Petugas & Kader",
                        value = "$totalPetugas",
                        subtitle = "Akun Aktif",
                        icon = Icons.Default.Badge,
                        iconTint = SpmKesehatanColor,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (currentUser?.role == UserRole.ADMIN) onNavigate(AppScreen.PETUGAS)
                            }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Total Laporan",
                        value = "$totalLaporan",
                        subtitle = "$verifiedLaporan Disetujui",
                        icon = Icons.Default.Assignment,
                        iconTint = StatusSuccess,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(AppScreen.LAPORAN) }
                    )
                    StatCard(
                        title = "Menunggu Verifikasi",
                        value = "$pendingLaporan",
                        subtitle = "Perlu Tinjauan",
                        icon = Icons.Default.PendingActions,
                        iconTint = StatusPending,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (currentUser?.role == UserRole.ADMIN) onNavigate(AppScreen.VERIFIKASI)
                            }
                    )
                }
            }
        }

        // 3. 6 Bidang SPM Modul Shortcuts
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "6 Bidang SPM Kabupaten Indramayu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Akses Modul",
                    style = MaterialTheme.typography.labelSmall,
                    color = IndramayuNavy,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpmShortcutItem(
                        title = "Pendidikan",
                        subtitle = "$countPendidikan Data",
                        icon = Icons.Default.School,
                        color = SpmPendidikanColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SPM_PENDIDIKAN) }
                    )
                    SpmShortcutItem(
                        title = "Kesehatan",
                        subtitle = "$countKesehatan Data",
                        icon = Icons.Default.MedicalServices,
                        color = SpmKesehatanColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SPM_KESEHATAN) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpmShortcutItem(
                        title = "Pekerjaan Umum",
                        subtitle = "$countPU Data",
                        icon = Icons.Default.WaterDrop,
                        color = SpmPekerjaanUmumColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SPM_PEKERJAAN_UMUM) }
                    )
                    SpmShortcutItem(
                        title = "Perumahan",
                        subtitle = "$countPerumahan Data",
                        icon = Icons.Default.Home,
                        color = SpmPerumahanColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SPM_PERUMAHAN) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpmShortcutItem(
                        title = "Trantibumlinmas",
                        subtitle = "$countTrantibum Data",
                        icon = Icons.Default.Shield,
                        color = SpmTrantibumColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SPM_TRANTIBUMLINMAS) }
                    )
                    SpmShortcutItem(
                        title = "Sosial",
                        subtitle = "$countSosial Data",
                        icon = Icons.Default.Diversity3,
                        color = SpmSosialColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SPM_SOSIAL) }
                    )
                }
            }
        }

        // 4. Grafik Capaian 6 Bidang SPM
        item {
            SpmCapaianChart(
                pendidikanCount = countPendidikan,
                kesehatanCount = countKesehatan,
                puCount = countPU,
                perumahanCount = countPerumahan,
                trantibumCount = countTrantibum,
                sosialCount = countSosial
            )
        }

        // 5. Grafik Posyandu per Kecamatan
        item {
            PosyanduPerKecamatanChart(kecamatanData = kecamatanGrouped)
        }

        // 6. Grafik Status Verifikasi
        item {
            StatusVerifikasiChart(
                disetujui = verifiedLaporan,
                menunggu = pendingLaporan,
                revisi = 1
            )
        }

        // 7. Grafik Tren Bulanan
        item {
            MonthlyTrendChart()
        }

        // 8. Riwayat Aktivitas Terakhir (Audit Log)
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
                        Text(
                            text = "Aktivitas Terakhir Sistem",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Log",
                            tint = IndramayuNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (recentLogs.isEmpty()) {
                        Text(
                            text = "Belum ada riwayat aktivitas tercatat.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        recentLogs.take(4).forEach { log ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(IndramayuNavy.copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = IndramayuNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = log.details,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 2
                                    )
                                    Text(
                                        text = "${log.userName} • ${log.waktuFormatted}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Divider(color = GovBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        // Footer spacing
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SpmShortcutItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
