package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    users: List<UserEntity>,
    onLoginSubmit: (identifier: String, pass: String, onError: (String) -> Unit) -> Unit,
    onQuickUserSelect: (UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showQuickUserDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(IndramayuNavyDark, IndramayuNavy, GovBackgroundLight)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Official Logo
            Image(
                painter = painterResource(id = R.drawable.logo_posyanduku),
                contentDescription = "Logo Posyandu Indramayu",
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "POSYANDUKU",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Text(
                text = "6 BIDANG STANDAR PELAYANAN MINIMAL (SPM)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = IndramayuGold,
                textAlign = TextAlign.Center,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "PEMERINTAH KABUPATEN INDRAMAYU\nDINAS PEMBERDAYAAN MASYARAKAT DAN DESA",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Login Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Masuk ke Sistem",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndramayuNavy
                    )
                    Text(
                        text = "Gunakan akun resmi Admin atau Petugas Posyandu",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (errorMessage != null) {
                        Surface(
                            color = StatusError.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = StatusError,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    color = StatusError,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Email / Username Input
                    OutlinedTextField(
                        value = identifier,
                        onValueChange = {
                            identifier = it
                            errorMessage = null
                        },
                        label = { Text("Email atau Nama Pengguna") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "Email/Username", tint = IndramayuNavy)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("Kata Sandi") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Kata Sandi", tint = IndramayuNavy)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Sembunyikan Kata Sandi" else "Tampilkan Kata Sandi",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            if (identifier.isNotBlank() && password.isNotBlank()) {
                                onLoginSubmit(identifier, password) { err ->
                                    errorMessage = err
                                }
                            }
                        }),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Lupa Password Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showForgotPasswordDialog = true }) {
                            Text(
                                text = "Lupa Kata Sandi?",
                                style = MaterialTheme.typography.bodySmall,
                                color = IndramayuNavyLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            if (identifier.isBlank() || password.isBlank()) {
                                errorMessage = "Mohon isi email/username dan kata sandi Anda."
                            } else {
                                onLoginSubmit(identifier, password) { err ->
                                    errorMessage = err
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndramayuNavy),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = "Masuk",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Masuk Aplikasi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Select User dialog trigger
                    OutlinedButton(
                        onClick = { showQuickUserDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IndramayuNavy),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Pilih Akun", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pilih Akun Terdaftar", fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Footer as requested in brief
            Text(
                text = "@beben_dpmd_imyu 2026",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Sistem Informasi Posyandu 6 Bidang SPM Kabupaten Indramayu",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Lupa Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            icon = { Icon(Icons.Default.HelpOutline, contentDescription = null, tint = IndramayuNavy) },
            title = { Text("Bantuan Lupa Kata Sandi", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Untuk pemulihan kata sandi akun Petugas Posyandu atau Admin, silakan hubungi Administrator DPMD Kabupaten Indramayu melalui Bidang Kelembagaan dan Partisipasi Masyarakat atau melalui WhatsApp resmi DPMD: 0812-2334-4550."
                )
            },
            confirmButton = {
                Button(onClick = { showForgotPasswordDialog = false }) {
                    Text("Mengerti")
                }
            }
        )
    }

    // Quick User Switch / Selector Dialog
    if (showQuickUserDialog) {
        AlertDialog(
            onDismissRequest = { showQuickUserDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = IndramayuNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pilih Akun Pengguna", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "Pilih salah satu akun terdaftar untuk langsung masuk ke sesi:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    users.forEach { user ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    showQuickUserDialog = false
                                    onQuickUserSelect(user)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
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
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = user.fullName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${user.role.name} • ${user.email}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showQuickUserDialog = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}
