package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SpmCapaianChart(
    pendidikanCount: Int,
    kesehatanCount: Int,
    puCount: Int,
    perumahanCount: Int,
    trantibumCount: Int,
    sosialCount: Int,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Triple("Pendidikan", pendidikanCount, SpmPendidikanColor),
        Triple("Kesehatan", kesehatanCount, SpmKesehatanColor),
        Triple("Pekerjaan Umum", puCount, SpmPekerjaanUmumColor),
        Triple("Perumahan Rakyat", perumahanCount, SpmPerumahanColor),
        Triple("Trantibumlinmas", trantibumCount, SpmTrantibumColor),
        Triple("Sosial", sosialCount, SpmSosialColor)
    )

    val maxVal = maxOf(1, items.maxOf { it.second })

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Capaian Pelayanan 6 Bidang SPM",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Total data tercatat pada masing-masing indikator minimal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            items.forEach { (name, count, color) ->
                val fraction = (count.toFloat() / maxVal).coerceIn(0.05f, 1f)
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "$count data",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Progress bar
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    ) {
                        // Background track
                        drawRoundRect(
                            color = color.copy(alpha = 0.15f),
                            size = size,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                        )
                        // Fill bar
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(color.copy(alpha = 0.8f), color)
                            ),
                            size = Size(size.width * fraction, size.height),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PosyanduPerKecamatanChart(
    kecamatanData: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    val displayData = if (kecamatanData.isEmpty()) {
        listOf("Indramayu" to 2, "Jatibarang" to 1, "Sindang" to 1, "Balongan" to 1)
    } else {
        kecamatanData.take(6)
    }
    val maxCount = maxOf(1, displayData.maxOf { it.second })

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Distribusi Posyandu per Kecamatan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Jumlah unit Posyandu aktif yang terdata di sistem",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                val barCount = displayData.size
                val barWidth = (size.width / (barCount * 1.8f)).coerceIn(24f, 48f)
                val spacing = (size.width - (barCount * barWidth)) / (barCount + 1)

                displayData.forEachIndexed { index, (name, count) ->
                    val x = spacing + index * (barWidth + spacing)
                    val barHeight = (count.toFloat() / maxCount) * (size.height - 30f)
                    val y = size.height - barHeight - 20f

                    // Draw Bar
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(IndramayuNavyLight, IndramayuNavy)
                        ),
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                    )

                    // Baseline
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(0f, size.height - 18f),
                        end = Offset(size.width, size.height - 18f),
                        strokeWidth = 2f
                    )
                }
            }

            // Legend labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                displayData.forEach { (name, count) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = name.take(6),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "$count Unit",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = IndramayuNavy,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusVerifikasiChart(
    disetujui: Int,
    menunggu: Int,
    revisi: Int,
    modifier: Modifier = Modifier
) {
    val total = disetujui + menunggu + revisi
    val approvedRatio = if (total > 0) disetujui.toFloat() / total else 0.6f
    val pendingRatio = if (total > 0) menunggu.toFloat() / total else 0.3f
    val revisionRatio = if (total > 0) revisi.toFloat() / total else 0.1f

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Status Verifikasi Laporan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Monitoring pengesahan laporan oleh verifikator DPMD",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Donut Chart
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 24f
                        val radius = (size.minDimension - strokeWidth) / 2f
                        val center = Offset(size.width / 2f, size.height / 2f)

                        var startAngle = -90f
                        val sweep1 = approvedRatio * 360f
                        val sweep2 = pendingRatio * 360f
                        val sweep3 = revisionRatio * 360f

                        // Arc 1: Disetujui (Green)
                        drawArc(
                            color = StatusSuccess,
                            startAngle = startAngle,
                            sweepAngle = sweep1,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        startAngle += sweep1

                        // Arc 2: Menunggu (Amber)
                        drawArc(
                            color = StatusPending,
                            startAngle = startAngle,
                            sweepAngle = sweep2,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        startAngle += sweep2

                        // Arc 3: Revisi (Red)
                        drawArc(
                            color = StatusError,
                            startAngle = startAngle,
                            sweepAngle = sweep3,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$total",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = IndramayuNavy
                        )
                        Text(
                            text = "Laporan",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Legend
                Column(modifier = Modifier.weight(1f)) {
                    LegendItem("Disetujui", disetujui, StatusSuccess)
                    Spacer(modifier = Modifier.height(8.dp))
                    LegendItem("Menunggu", menunggu, StatusPending)
                    Spacer(modifier = Modifier.height(8.dp))
                    LegendItem("Perlu Revisi", revisi, StatusError)
                }
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, count: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = "$count",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun MonthlyTrendChart(
    modifier: Modifier = Modifier
) {
    val months = listOf("Jul", "Agu", "Sep", "Okt", "Nov", "Des")
    val points = listOf(14f, 22f, 35f, 48f, 66f, 75f)
    val maxP = 80f

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tren Pelayanan & Laporan Posyandu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Perkembangan rekapitulasi data SPM semester II 2026",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val stepX = size.width / (points.size - 1)
                val path = Path()
                val fillPath = Path()

                points.forEachIndexed { i, p ->
                    val x = i * stepX
                    val y = size.height - (p / maxP) * (size.height - 24f) - 12f
                    if (i == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, size.height)
                        fillPath.lineTo(x, y)
                    } else {
                        val prevX = (i - 1) * stepX
                        val prevY = size.height - (points[i - 1] / maxP) * (size.height - 24f) - 12f
                        val cx = (prevX + x) / 2f
                        path.cubicTo(cx, prevY, cx, y, x, y)
                        fillPath.cubicTo(cx, prevY, cx, y, x, y)
                    }
                }

                fillPath.lineTo(size.width, size.height)
                fillPath.close()

                // Area Gradient
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(PosyanduCyan.copy(alpha = 0.35f), Color.Transparent)
                    )
                )

                // Line Stroke
                drawPath(
                    path = path,
                    color = PosyanduCyan,
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )

                // Dots
                points.forEachIndexed { i, p ->
                    val x = i * stepX
                    val y = size.height - (p / maxP) * (size.height - 24f) - 12f
                    drawCircle(color = IndramayuNavy, radius = 5f, center = Offset(x, y))
                    drawCircle(color = Color.White, radius = 2.5f, center = Offset(x, y))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                months.forEach { m ->
                    Text(
                        text = m,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
