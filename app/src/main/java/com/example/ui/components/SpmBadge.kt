package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BidangSpm
import com.example.data.model.StatusVerifikasi
import com.example.ui.theme.*

@Composable
fun SpmBadge(
    bidangSpm: BidangSpm,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (bidangSpm) {
        BidangSpm.PENDIDIKAN -> Triple(SpmPendidikanColor.copy(alpha = 0.12f), SpmPendidikanColor, Icons.Default.School)
        BidangSpm.KESEHATAN -> Triple(SpmKesehatanColor.copy(alpha = 0.12f), SpmKesehatanColor, Icons.Default.MedicalServices)
        BidangSpm.PEKERJAAN_UMUM -> Triple(SpmPekerjaanUmumColor.copy(alpha = 0.12f), SpmPekerjaanUmumColor, Icons.Default.WaterDrop)
        BidangSpm.PERUMAHAN_RAKYAT -> Triple(SpmPerumahanColor.copy(alpha = 0.12f), SpmPerumahanColor, Icons.Default.Home)
        BidangSpm.TRANTIBUMLINMAS -> Triple(SpmTrantibumColor.copy(alpha = 0.12f), SpmTrantibumColor, Icons.Default.Shield)
        BidangSpm.SOSIAL -> Triple(SpmSosialColor.copy(alpha = 0.12f), SpmSosialColor, Icons.Default.Diversity3)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = bidangSpm.displayName,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = bidangSpm.displayName,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StatusVerifikasiBadge(
    status: StatusVerifikasi,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, text, icon) = when (status) {
        StatusVerifikasi.DISETUJUI -> Quadruple(
            StatusSuccess.copy(alpha = 0.15f),
            StatusSuccess,
            "Disetujui",
            Icons.Default.CheckCircle
        )
        StatusVerifikasi.MENUNGGU -> Quadruple(
            StatusPending.copy(alpha = 0.18f),
            IndramayuGoldDark,
            "Menunggu Verifikasi",
            Icons.Default.HourglassTop
        )
        StatusVerifikasi.PERLU_REVISI -> Quadruple(
            StatusError.copy(alpha = 0.15f),
            StatusError,
            "Perlu Revisi",
            Icons.Default.ErrorOutline
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
