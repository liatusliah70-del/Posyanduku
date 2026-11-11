package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.SpmCapaianChart
import com.example.ui.theme.*

@Composable
fun MonitoringScreen(
    totalPosyandu: Int,
    countPendidikan: Int,
    countKesehatan: Int,
    countPU: Int,
    countPerumahan: Int,
    countTrantibum: Int,
    countSosial: Int,
    kesehatanList: List<SpmKesehatanEntity>,
    perumahanList: List<SpmPerumahanEntity>,
    modifier: Modifier = Modifier
) {
    val stuntingCount = kesehatanList.count { it.statusGizi.contains("Stunting", ignoreCase = true) }
    val normalCount = kesehatanList.count { it.statusGizi.contains("Normal", ignoreCase = true) || it.statusGizi.contains("Baik", ignoreCase = true) }
    val rtLhBeratCount = perumahanList.count { it.statusKelayakan.contains("Berat", ignoreCase = true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GovBackgroundLight),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Monitoring & Evaluasi (Monev) SPM",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Analisis Ketercapaian Standar Pelayanan Minimal di Seluruh Posyandu",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Summary Chart
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

        // Key Health & Stunting Monitoring Card
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
                            text = "Indikator Kunci Kesehatan & Stunting",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = SpmKesehatanColor)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = StatusSuccess.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Gizi Baik / Normal", style = MaterialTheme.typography.labelSmall, color = StatusSuccess)
                                Text("$normalCount Sasaran", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            color = if (stuntingCount > 0) StatusError.copy(alpha = 0.12f) else StatusSuccess.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Risiko Stunting", style = MaterialTheme.typography.labelSmall, color = if (stuntingCount > 0) StatusError else StatusSuccess)
                                Text("$stuntingCount Balita", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Rekomendasi DPMD: Balita dengan indikasi risiko stunting wajib mendapatkan intervensi Program TOSS (Tanggap Obati Stunting) dan PMT pemulihan selama 90 hari.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Housing & Infrastructure Monitoring Card
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
                            text = "Indikator Perumahan & Infrastruktur (PU)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.HomeWork, contentDescription = null, tint = SpmPerumahanColor)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = StatusError.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("RTLH Rusak Berat", style = MaterialTheme.typography.labelSmall, color = StatusError)
                                Text("$rtLhBeratCount Rumah", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            color = SpmPekerjaanUmumColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Titik Sarpras Terdata", style = MaterialTheme.typography.labelSmall, color = SpmPekerjaanUmumColor)
                                Text("$countPU Dusun", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Prioritas usulan bedah rumah BSPS/Rutilahu dan perbaikan sanitasi SPAM diajukan ke dinas teknis dan musrenbangdes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Education & Social Aid Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sosial, Trantibum, dan Pendidikan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• SPM Pendidikan: $countPendidikan anak usia dini dan sekolah terdata.\n• SPM Trantibumlinmas: $countTrantibum wilayah memiliki pos ronda aktif.\n• SPM Sosial: $countSosial warga lansia dan disabilitas terdata dan terhubung dengan usulan bansos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
