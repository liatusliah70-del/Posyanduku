package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.components.PosyandukuTopBar
import com.example.ui.screens.*
import com.example.ui.screens.spm.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PosyandukuTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val allKecamatan by viewModel.allKecamatan.collectAsStateWithLifecycle()
    val allDesa by viewModel.allDesa.collectAsStateWithLifecycle()
    val allPosyandu by viewModel.allPosyandu.collectAsStateWithLifecycle()

    val spmPendidikanList by viewModel.spmPendidikanList.collectAsStateWithLifecycle()
    val spmKesehatanList by viewModel.spmKesehatanList.collectAsStateWithLifecycle()
    val spmPekerjaanUmumList by viewModel.spmPekerjaanUmumList.collectAsStateWithLifecycle()
    val spmPerumahanList by viewModel.spmPerumahanList.collectAsStateWithLifecycle()
    val spmTrantibumList by viewModel.spmTrantibumList.collectAsStateWithLifecycle()
    val spmSosialList by viewModel.spmSosialList.collectAsStateWithLifecycle()

    val allLaporan by viewModel.allLaporan.collectAsStateWithLifecycle()
    val recentLogs by viewModel.recentAuditLogs.collectAsStateWithLifecycle()

    val totalPosyandu by viewModel.totalPosyanduCount.collectAsStateWithLifecycle()
    val totalKecamatan by viewModel.totalKecamatanCount.collectAsStateWithLifecycle()
    val totalDesa by viewModel.totalDesaCount.collectAsStateWithLifecycle()
    val totalPetugas by viewModel.totalPetugasCount.collectAsStateWithLifecycle()
    val totalSasaran by viewModel.totalSasaranSum.collectAsStateWithLifecycle()
    val totalLaporan by viewModel.totalLaporanCount.collectAsStateWithLifecycle()
    val verifiedLaporan by viewModel.verifiedLaporanCount.collectAsStateWithLifecycle()
    val pendingLaporan by viewModel.pendingLaporanCount.collectAsStateWithLifecycle()

    val countPendidikan by viewModel.countPendidikan.collectAsStateWithLifecycle()
    val countKesehatan by viewModel.countKesehatan.collectAsStateWithLifecycle()
    val countPU by viewModel.countPU.collectAsStateWithLifecycle()
    val countPerumahan by viewModel.countPerumahan.collectAsStateWithLifecycle()
    val countTrantibum by viewModel.countTrantibum.collectAsStateWithLifecycle()
    val countSosial by viewModel.countSosial.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSwitchUserModal by remember { mutableStateOf(false) }

    // User message snackbar trigger
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    // Android Hardware / Gesture Back Button handling
    BackHandler(enabled = currentScreen != AppScreen.LOGIN && (currentScreen != AppScreen.DASHBOARD || drawerState.isOpen)) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            viewModel.handleBack()
        }
    }

    // If on LOGIN screen, show authentication screen directly
    if (currentUser == null || currentScreen == AppScreen.LOGIN) {
        AuthScreen(
            users = allUsers,
            onLoginSubmit = { identifier, pass, onError ->
                viewModel.login(identifier, pass, onSuccess = {}, onError = onError)
            },
            onQuickUserSelect = { user ->
                viewModel.switchUser(user)
                viewModel.navigateTo(AppScreen.DASHBOARD)
            }
        )
        return
    }

    // Navigation Drawer with Official Indramayu & DPMD Sidebar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(310.dp)
            ) {
                SidebarContent(
                    currentUser = currentUser,
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        viewModel.navigateTo(screen)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onLogout = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.logout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                PosyandukuTopBar(
                    currentUser = currentUser,
                    onOpenDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    onSwitchUserClick = { showSwitchUserModal = true },
                    onLogoutClick = { viewModel.logout() }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.DASHBOARD,
                        onClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Dashboard", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndramayuNavy,
                            indicatorColor = IndramayuNavy.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.POSYANDU,
                        onClick = { viewModel.navigateTo(AppScreen.POSYANDU) },
                        icon = { Icon(Icons.Default.Storefront, contentDescription = "Posyandu") },
                        label = { Text("Posyandu", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndramayuNavy,
                            indicatorColor = IndramayuNavy.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SPM_KESEHATAN,
                        onClick = { viewModel.navigateTo(AppScreen.SPM_KESEHATAN) },
                        icon = { Icon(Icons.Default.MedicalServices, contentDescription = "SPM") },
                        label = { Text("Kesehatan", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SpmKesehatanColor,
                            indicatorColor = SpmKesehatanColor.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.LAPORAN,
                        onClick = { viewModel.navigateTo(AppScreen.LAPORAN) },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = "Laporan") },
                        label = { Text("Laporan", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndramayuNavy,
                            indicatorColor = IndramayuNavy.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PENGATURAN,
                        onClick = { viewModel.navigateTo(AppScreen.PENGATURAN) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
                        label = { Text("Pengaturan", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndramayuNavy,
                            indicatorColor = IndramayuNavy.copy(alpha = 0.15f)
                        )
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    AppScreen.DASHBOARD -> {
                        DashboardScreen(
                            currentUser = currentUser,
                            totalKecamatan = totalKecamatan,
                            totalDesa = totalDesa,
                            totalPosyandu = totalPosyandu,
                            totalPetugas = totalPetugas,
                            totalSasaran = totalSasaran,
                            totalLaporan = totalLaporan,
                            verifiedLaporan = verifiedLaporan,
                            pendingLaporan = pendingLaporan,
                            countPendidikan = countPendidikan,
                            countKesehatan = countKesehatan,
                            countPU = countPU,
                            countPerumahan = countPerumahan,
                            countTrantibum = countTrantibum,
                            countSosial = countSosial,
                            posyanduList = allPosyandu,
                            recentLogs = recentLogs,
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }

                    AppScreen.WILAYAH -> {
                        WilayahScreen(
                            kecamatanList = allKecamatan,
                            desaList = allDesa,
                            onAddKecamatan = { viewModel.addKecamatan(it) },
                            onAddDesa = { viewModel.addDesa(it) },
                            onDeleteKecamatan = { viewModel.deleteKecamatan(it) },
                            onDeleteDesa = { viewModel.deleteDesa(it) }
                        )
                    }

                    AppScreen.POSYANDU -> {
                        PosyanduScreen(
                            posyanduList = allPosyandu,
                            kecamatanList = allKecamatan,
                            currentUser = currentUser,
                            onAddPosyandu = { viewModel.addPosyandu(it) },
                            onUpdatePosyandu = { viewModel.updatePosyandu(it) },
                            onDeletePosyandu = { id, nama -> viewModel.deletePosyandu(id, nama) }
                        )
                    }

                    AppScreen.SPM_PENDIDIKAN -> {
                        SpmPendidikanScreen(
                            dataList = spmPendidikanList,
                            posyanduList = allPosyandu,
                            currentUser = currentUser,
                            onSave = { entity, isEdit -> viewModel.saveSpmPendidikan(entity, isEdit) },
                            onDelete = { id, nama -> viewModel.deleteSpmPendidikan(id, nama) }
                        )
                    }

                    AppScreen.SPM_KESEHATAN -> {
                        SpmKesehatanScreen(
                            dataList = spmKesehatanList,
                            posyanduList = allPosyandu,
                            currentUser = currentUser,
                            onSave = { entity, isEdit -> viewModel.saveSpmKesehatan(entity, isEdit) },
                            onDelete = { id, nama -> viewModel.deleteSpmKesehatan(id, nama) }
                        )
                    }

                    AppScreen.SPM_PEKERJAAN_UMUM -> {
                        SpmPekerjaanUmumScreen(
                            dataList = spmPekerjaanUmumList,
                            posyanduList = allPosyandu,
                            currentUser = currentUser,
                            onSave = { entity, isEdit -> viewModel.saveSpmPekerjaanUmum(entity, isEdit) },
                            onDelete = { id, nama -> viewModel.deleteSpmPekerjaanUmum(id, nama) }
                        )
                    }

                    AppScreen.SPM_PERUMAHAN -> {
                        SpmPerumahanScreen(
                            dataList = spmPerumahanList,
                            posyanduList = allPosyandu,
                            currentUser = currentUser,
                            onSave = { entity, isEdit -> viewModel.saveSpmPerumahan(entity, isEdit) },
                            onDelete = { id, nama -> viewModel.deleteSpmPerumahan(id, nama) }
                        )
                    }

                    AppScreen.SPM_TRANTIBUMLINMAS -> {
                        SpmTrantibumScreen(
                            dataList = spmTrantibumList,
                            posyanduList = allPosyandu,
                            currentUser = currentUser,
                            onSave = { entity, isEdit -> viewModel.saveSpmTrantibum(entity, isEdit) },
                            onDelete = { id, nama -> viewModel.deleteSpmTrantibum(id, nama) }
                        )
                    }

                    AppScreen.SPM_SOSIAL -> {
                        SpmSosialScreen(
                            dataList = spmSosialList,
                            posyanduList = allPosyandu,
                            currentUser = currentUser,
                            onSave = { entity, isEdit -> viewModel.saveSpmSosial(entity, isEdit) },
                            onDelete = { id, nama -> viewModel.deleteSpmSosial(id, nama) }
                        )
                    }

                    AppScreen.PETUGAS -> {
                        PetugasScreen(
                            usersList = allUsers,
                            onAddUser = { viewModel.addUser(it) },
                            onUpdateUser = { viewModel.updateUser(it) },
                            onDeleteUser = { viewModel.deleteUser(it) }
                        )
                    }

                    AppScreen.MONEV -> {
                        MonitoringScreen(
                            totalPosyandu = totalPosyandu,
                            countPendidikan = countPendidikan,
                            countKesehatan = countKesehatan,
                            countPU = countPU,
                            countPerumahan = countPerumahan,
                            countTrantibum = countTrantibum,
                            countSosial = countSosial,
                            kesehatanList = spmKesehatanList,
                            perumahanList = spmPerumahanList
                        )
                    }

                    AppScreen.VERIFIKASI -> {
                        VerifikasiScreen(
                            laporanList = allLaporan,
                            currentUser = currentUser,
                            onVerifikasi = { id, status, catatan -> viewModel.verifikasiLaporan(id, status, catatan) }
                        )
                    }

                    AppScreen.LAPORAN -> {
                        LaporanScreen(
                            laporanList = allLaporan,
                            posyanduList = allPosyandu,
                            kecamatanList = allKecamatan,
                            currentUser = currentUser,
                            onSubmitLaporan = { viewModel.submitLaporan(it) },
                            onDeleteLaporan = { id, nomor -> viewModel.deleteLaporan(id, nomor) }
                        )
                    }

                    AppScreen.PENGATURAN -> {
                        PengaturanScreen(
                            currentUser = currentUser,
                            auditLogs = recentLogs,
                            onChangePassword = { viewModel.changePassword(it) }
                        )
                    }

                    AppScreen.LOGIN -> {
                        // Handled above
                    }
                }
            }
        }
    }

    // Quick Switch User Dialog
    if (showSwitchUserModal) {
        AlertDialog(
            onDismissRequest = { showSwitchUserModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = IndramayuNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ganti Akun Pengguna", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "Pilih profil untuk menguji hak akses Admin atau Petugas Posyandu:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    allUsers.forEach { usr ->
                        val isCurrent = currentUser?.id == usr.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) IndramayuNavy.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, IndramayuNavy) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.switchUser(usr)
                                    showSwitchUserModal = false
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
                                            if (usr.role == UserRole.ADMIN) IndramayuGold.copy(alpha = 0.2f) else PosyanduCyan.copy(alpha = 0.2f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (usr.role == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = if (usr.role == UserRole.ADMIN) IndramayuGoldDark else PosyanduCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = usr.fullName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${usr.role.name} • ${usr.assignedKecamatan ?: "DPMD Indramayu"}",
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
                TextButton(onClick = { showSwitchUserModal = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
fun SidebarContent(
    currentUser: UserEntity?,
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Sidebar Header with Indramayu Identity
        Surface(
            color = IndramayuNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_posyanduku),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "POSYANDUKU",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "KABUPATEN INDRAMAYU",
                            style = MaterialTheme.typography.labelSmall,
                            color = IndramayuGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = currentUser?.fullName ?: "Petugas",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Role: ${currentUser?.role?.name} • ${currentUser?.assignedKecamatan ?: "DPMD"}",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Navigation Items List
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(vertical = 8.dp)
        ) {
            SidebarSectionTitle("UTAMA")
            SidebarMenuItem(
                title = "Dashboard",
                icon = Icons.Default.Dashboard,
                isSelected = currentScreen == AppScreen.DASHBOARD,
                onClick = { onNavigate(AppScreen.DASHBOARD) }
            )

            if (currentUser?.role == UserRole.ADMIN) {
                SidebarMenuItem(
                    title = "Master Data Wilayah",
                    icon = Icons.Default.LocationCity,
                    isSelected = currentScreen == AppScreen.WILAYAH,
                    onClick = { onNavigate(AppScreen.WILAYAH) }
                )
            }

            SidebarMenuItem(
                title = "Data Posyandu",
                icon = Icons.Default.Storefront,
                isSelected = currentScreen == AppScreen.POSYANDU,
                onClick = { onNavigate(AppScreen.POSYANDU) }
            )

            SidebarSectionTitle("6 BIDANG SPM INDRAMAYU")
            SidebarMenuItem(
                title = "Bidang Pendidikan",
                icon = Icons.Default.School,
                color = SpmPendidikanColor,
                isSelected = currentScreen == AppScreen.SPM_PENDIDIKAN,
                onClick = { onNavigate(AppScreen.SPM_PENDIDIKAN) }
            )
            SidebarMenuItem(
                title = "Bidang Kesehatan",
                icon = Icons.Default.MedicalServices,
                color = SpmKesehatanColor,
                isSelected = currentScreen == AppScreen.SPM_KESEHATAN,
                onClick = { onNavigate(AppScreen.SPM_KESEHATAN) }
            )
            SidebarMenuItem(
                title = "Bidang Pekerjaan Umum",
                icon = Icons.Default.WaterDrop,
                color = SpmPekerjaanUmumColor,
                isSelected = currentScreen == AppScreen.SPM_PEKERJAAN_UMUM,
                onClick = { onNavigate(AppScreen.SPM_PEKERJAAN_UMUM) }
            )
            SidebarMenuItem(
                title = "Bidang Perumahan Rakyat",
                icon = Icons.Default.Home,
                color = SpmPerumahanColor,
                isSelected = currentScreen == AppScreen.SPM_PERUMAHAN,
                onClick = { onNavigate(AppScreen.SPM_PERUMAHAN) }
            )
            SidebarMenuItem(
                title = "Bidang Trantibumlinmas",
                icon = Icons.Default.Shield,
                color = SpmTrantibumColor,
                isSelected = currentScreen == AppScreen.SPM_TRANTIBUMLINMAS,
                onClick = { onNavigate(AppScreen.SPM_TRANTIBUMLINMAS) }
            )
            SidebarMenuItem(
                title = "Bidang Sosial",
                icon = Icons.Default.Diversity3,
                color = SpmSosialColor,
                isSelected = currentScreen == AppScreen.SPM_SOSIAL,
                onClick = { onNavigate(AppScreen.SPM_SOSIAL) }
            )

            SidebarSectionTitle("MANAJEMEN & EVALUASI")
            if (currentUser?.role == UserRole.ADMIN) {
                SidebarMenuItem(
                    title = "Data Petugas",
                    icon = Icons.Default.Badge,
                    isSelected = currentScreen == AppScreen.PETUGAS,
                    onClick = { onNavigate(AppScreen.PETUGAS) }
                )
            }

            SidebarMenuItem(
                title = "Monitoring & Evaluasi",
                icon = Icons.Default.Assessment,
                isSelected = currentScreen == AppScreen.MONEV,
                onClick = { onNavigate(AppScreen.MONEV) }
            )

            if (currentUser?.role == UserRole.ADMIN) {
                SidebarMenuItem(
                    title = "Verifikasi Laporan",
                    icon = Icons.Default.FactCheck,
                    isSelected = currentScreen == AppScreen.VERIFIKASI,
                    onClick = { onNavigate(AppScreen.VERIFIKASI) }
                )
            }

            SidebarMenuItem(
                title = "Laporan & Rekapitulasi",
                icon = Icons.Default.Assignment,
                isSelected = currentScreen == AppScreen.LAPORAN,
                onClick = { onNavigate(AppScreen.LAPORAN) }
            )

            SidebarMenuItem(
                title = "Pengaturan",
                icon = Icons.Default.Settings,
                isSelected = currentScreen == AppScreen.PENGATURAN,
                onClick = { onNavigate(AppScreen.PENGATURAN) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = GovBorder.copy(alpha = 0.5f))

            SidebarMenuItem(
                title = "Keluar / Logout",
                icon = Icons.Default.Logout,
                color = StatusError,
                isSelected = false,
                onClick = onLogout
            )
        }

        // Sidebar Footer Developer Credit
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "@beben_dpmd_imyu 2026",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = IndramayuNavy
                )
                Text(
                    text = "DPMD Kabupaten Indramayu",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SidebarSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
fun SidebarMenuItem(
    title: String,
    icon: ImageVector,
    color: Color = IndramayuNavy,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) IndramayuNavy.copy(alpha = 0.12f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) IndramayuNavy else color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) IndramayuNavy else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
