package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosyandukuTopBar(
    currentUser: UserEntity?,
    onOpenDrawer: () -> Unit,
    onSwitchUserClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = IndramayuNavy,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onOpenDrawer) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Buka Menu",
                        tint = Color.White
                    )
                }

                // Official Emblem Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_posyanduku),
                    contentDescription = "Logo Posyandu Indramayu",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "POSYANDUKU INDRAMAYU",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "DINAS PEMBERDAYAAN MASYARAKAT DAN DESA",
                        style = MaterialTheme.typography.labelSmall,
                        color = IndramayuGold,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }

                // User Role Badge and quick actions
                if (currentUser != null) {
                    Surface(
                        color = if (currentUser.role == UserRole.ADMIN) IndramayuGold.copy(alpha = 0.25f) else PosyanduCyan.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clickable { onSwitchUserClick() }
                            .padding(end = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (currentUser.role == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                contentDescription = currentUser.role.name,
                                tint = if (currentUser.role == UserRole.ADMIN) IndramayuGold else Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentUser.role == UserRole.ADMIN) "ADMIN" else "PETUGAS",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Ganti Role",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Keluar",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
