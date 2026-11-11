package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.*
import com.example.data.repository.PosyanduRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen(val title: String, val adminOnly: Boolean = false) {
    LOGIN("Login Masuk"),
    DASHBOARD("Dashboard Utama"),
    WILAYAH("Master Data Wilayah", adminOnly = true),
    POSYANDU("Data Posyandu"),
    SPM_PENDIDIKAN("Bidang Pendidikan"),
    SPM_KESEHATAN("Bidang Kesehatan"),
    SPM_PEKERJAAN_UMUM("Bidang Pekerjaan Umum"),
    SPM_PERUMAHAN("Bidang Perumahan Rakyat"),
    SPM_TRANTIBUMLINMAS("Bidang Trantibumlinmas"),
    SPM_SOSIAL("Bidang Sosial"),
    PETUGAS("Data Petugas", adminOnly = true),
    MONEV("Monitoring dan Evaluasi"),
    VERIFIKASI("Verifikasi Laporan", adminOnly = true),
    LAPORAN("Laporan & Rekapitulasi"),
    PENGATURAN("Pengaturan & Sinkronisasi")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = PosyanduRepository(db)

    // Current Navigation Screen
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Navigation back stack for BackHandler
    private val screenStack = mutableListOf<AppScreen>()

    // Current Logged-in User
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Message Toast / Snackbar state
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Database flows
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allKecamatan: StateFlow<List<KecamatanEntity>> = repository.allKecamatan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDesa: StateFlow<List<DesaEntity>> = repository.allDesa
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPosyandu: StateFlow<List<PosyanduEntity>> = repository.allPosyandu
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 6 SPM flows
    val spmPendidikanList: StateFlow<List<SpmPendidikanEntity>> = repository.allPendidikan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spmKesehatanList: StateFlow<List<SpmKesehatanEntity>> = repository.allKesehatan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spmPekerjaanUmumList: StateFlow<List<SpmPekerjaanUmumEntity>> = repository.allPekerjaanUmum
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spmPerumahanList: StateFlow<List<SpmPerumahanEntity>> = repository.allPerumahan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spmTrantibumList: StateFlow<List<SpmTrantibumEntity>> = repository.allTrantibum
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spmSosialList: StateFlow<List<SpmSosialEntity>> = repository.allSosial
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Laporan & Logs
    val allLaporan: StateFlow<List<LaporanEntity>> = repository.allLaporan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentAuditLogs: StateFlow<List<AuditLogEntity>> = repository.recentAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Aggregates & Counts
    val totalPosyanduCount: StateFlow<Int> = repository.totalPosyanduCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5)

    val totalKecamatanCount: StateFlow<Int> = repository.totalKecamatanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 8)

    val totalDesaCount: StateFlow<Int> = repository.totalDesaCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 10)

    val totalPetugasCount: StateFlow<Int> = repository.totalPetugasCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)

    val totalSasaranSum: StateFlow<Int> = repository.totalSasaranSum
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 450)

    val totalLaporanCount: StateFlow<Int> = repository.totalLaporanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)

    val verifiedLaporanCount: StateFlow<Int> = repository.verifiedLaporanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val pendingLaporanCount: StateFlow<Int> = repository.pendingLaporanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val revisionLaporanCount: StateFlow<Int> = repository.revisionLaporanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    // SPM counts
    val countPendidikan: StateFlow<Int> = repository.totalPendidikanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)
    val countKesehatan: StateFlow<Int> = repository.totalKesehatanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)
    val countPU: StateFlow<Int> = repository.totalPekerjaanUmumCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)
    val countPerumahan: StateFlow<Int> = repository.totalPerumahanCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)
    val countTrantibum: StateFlow<Int> = repository.totalTrantibumCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)
    val countSosial: StateFlow<Int> = repository.totalSosialCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    init {
        // Initialize default user with Admin to allow immediate exploration
        viewModelScope.launch {
            allUsers.collect { users ->
                if (_currentUser.value == null && users.isNotEmpty()) {
                    _currentUser.value = users.firstOrNull { it.role == UserRole.ADMIN } ?: users.first()
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        return if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            true
        } else if (_currentScreen.value != AppScreen.DASHBOARD && _currentUser.value != null) {
            _currentScreen.value = AppScreen.DASHBOARD
            true
        } else {
            false
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    // Auth methods
    fun login(identifier: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.login(identifier, pass)
            if (user != null) {
                _currentUser.value = user
                _currentScreen.value = AppScreen.DASHBOARD
                showMessage("Selamat datang, ${user.fullName}!")
                onSuccess()
            } else {
                onError("Email/Username atau Kata Sandi salah. Silakan periksa kembali.")
            }
        }
    }

    fun switchUser(user: UserEntity) {
        _currentUser.value = user
        showMessage("Beralih profil ke: ${user.fullName} (${user.role.name})")
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = AppScreen.LOGIN
        showMessage("Anda telah keluar dari aplikasi")
    }

    // CRUD Posyandu
    fun addPosyandu(posyandu: PosyanduEntity) {
        viewModelScope.launch {
            repository.insertPosyandu(posyandu, _currentUser.value)
            showMessage("Posyandu ${posyandu.namaPosyandu} berhasil ditambahkan")
        }
    }

    fun updatePosyandu(posyandu: PosyanduEntity) {
        viewModelScope.launch {
            repository.updatePosyandu(posyandu, _currentUser.value)
            showMessage("Data Posyandu ${posyandu.namaPosyandu} berhasil diperbarui")
        }
    }

    fun deletePosyandu(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deletePosyandu(id, nama, _currentUser.value)
            showMessage("Posyandu $nama telah dihapus")
        }
    }

    // CRUD SPM 1: Pendidikan
    fun saveSpmPendidikan(entity: SpmPendidikanEntity, isEdit: Boolean) {
        viewModelScope.launch {
            if (isEdit) {
                repository.updatePendidikan(entity, _currentUser.value)
                showMessage("Data SPM Pendidikan anak ${entity.namaAnak} diperbarui")
            } else {
                repository.insertPendidikan(entity, _currentUser.value)
                showMessage("Data SPM Pendidikan anak ${entity.namaAnak} berhasil disimpan")
            }
        }
    }

    fun deleteSpmPendidikan(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deletePendidikan(id, nama, _currentUser.value)
            showMessage("Data SPM Pendidikan telah dihapus")
        }
    }

    // CRUD SPM 2: Kesehatan
    fun saveSpmKesehatan(entity: SpmKesehatanEntity, isEdit: Boolean) {
        viewModelScope.launch {
            if (isEdit) {
                repository.updateKesehatan(entity, _currentUser.value)
                showMessage("Data SPM Kesehatan an. ${entity.namaSasaran} diperbarui")
            } else {
                repository.insertKesehatan(entity, _currentUser.value)
                showMessage("Data SPM Kesehatan an. ${entity.namaSasaran} berhasil disimpan")
            }
        }
    }

    fun deleteSpmKesehatan(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deleteKesehatan(id, nama, _currentUser.value)
            showMessage("Data SPM Kesehatan telah dihapus")
        }
    }

    // CRUD SPM 3: Pekerjaan Umum
    fun saveSpmPekerjaanUmum(entity: SpmPekerjaanUmumEntity, isEdit: Boolean) {
        viewModelScope.launch {
            if (isEdit) {
                repository.updatePekerjaanUmum(entity, _currentUser.value)
                showMessage("Data SPM Pekerjaan Umum diperbarui")
            } else {
                repository.insertPekerjaanUmum(entity, _currentUser.value)
                showMessage("Data SPM Pekerjaan Umum berhasil disimpan")
            }
        }
    }

    fun deleteSpmPekerjaanUmum(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deletePekerjaanUmum(id, nama, _currentUser.value)
            showMessage("Data SPM Pekerjaan Umum telah dihapus")
        }
    }

    // CRUD SPM 4: Perumahan Rakyat
    fun saveSpmPerumahan(entity: SpmPerumahanEntity, isEdit: Boolean) {
        viewModelScope.launch {
            if (isEdit) {
                repository.updatePerumahan(entity, _currentUser.value)
                showMessage("Data SPM Perumahan RTLH diperbarui")
            } else {
                repository.insertPerumahan(entity, _currentUser.value)
                showMessage("Data SPM Perumahan RTLH berhasil disimpan")
            }
        }
    }

    fun deleteSpmPerumahan(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deletePerumahan(id, nama, _currentUser.value)
            showMessage("Data SPM Perumahan telah dihapus")
        }
    }

    // CRUD SPM 5: Trantibumlinmas
    fun saveSpmTrantibum(entity: SpmTrantibumEntity, isEdit: Boolean) {
        viewModelScope.launch {
            if (isEdit) {
                repository.updateTrantibum(entity, _currentUser.value)
                showMessage("Data SPM Trantibumlinmas diperbarui")
            } else {
                repository.insertTrantibum(entity, _currentUser.value)
                showMessage("Data SPM Trantibumlinmas berhasil disimpan")
            }
        }
    }

    fun deleteSpmTrantibum(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deleteTrantibum(id, nama, _currentUser.value)
            showMessage("Data SPM Trantibumlinmas telah dihapus")
        }
    }

    // CRUD SPM 6: Sosial
    fun saveSpmSosial(entity: SpmSosialEntity, isEdit: Boolean) {
        viewModelScope.launch {
            if (isEdit) {
                repository.updateSosial(entity, _currentUser.value)
                showMessage("Data SPM Sosial warga diperbarui")
            } else {
                repository.insertSosial(entity, _currentUser.value)
                showMessage("Data SPM Sosial warga berhasil disimpan")
            }
        }
    }

    fun deleteSpmSosial(id: Long, nama: String) {
        viewModelScope.launch {
            repository.deleteSosial(id, nama, _currentUser.value)
            showMessage("Data SPM Sosial telah dihapus")
        }
    }

    // Laporan & Verifikasi
    fun submitLaporan(laporan: LaporanEntity) {
        viewModelScope.launch {
            repository.insertLaporan(laporan, _currentUser.value)
            showMessage("Laporan ${laporan.nomorLaporan} berhasil dikirim ke Admin DPMD")
        }
    }

    fun verifikasiLaporan(id: Long, status: StatusVerifikasi, catatan: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.verifikasiLaporan(id, status, catatan, user)
            showMessage("Laporan telah diverifikasi dengan status: ${status.name}")
        }
    }

    fun deleteLaporan(id: Long, nomor: String) {
        viewModelScope.launch {
            repository.deleteLaporan(id, nomor, _currentUser.value)
            showMessage("Laporan $nomor telah dihapus")
        }
    }

    // Wilayah
    fun addKecamatan(kec: KecamatanEntity) {
        viewModelScope.launch {
            repository.insertKecamatan(kec, _currentUser.value)
            showMessage("Kecamatan ${kec.namaKecamatan} berhasil ditambahkan")
        }
    }

    fun addDesa(desa: DesaEntity) {
        viewModelScope.launch {
            repository.insertDesa(desa, _currentUser.value)
            showMessage("Desa ${desa.namaDesa} berhasil ditambahkan")
        }
    }

    fun deleteKecamatan(id: String) {
        viewModelScope.launch {
            repository.deleteKecamatan(id, _currentUser.value)
            showMessage("Kecamatan telah dihapus")
        }
    }

    fun deleteDesa(id: String) {
        viewModelScope.launch {
            repository.deleteDesa(id, _currentUser.value)
            showMessage("Desa telah dihapus")
        }
    }

    // Petugas & Pengguna
    fun addUser(user: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user, _currentUser.value)
            showMessage("Akun petugas ${user.fullName} berhasil ditambahkan")
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user, _currentUser.value)
            showMessage("Data akun ${user.fullName} berhasil diperbarui")
        }
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            repository.deleteUser(id, _currentUser.value)
            showMessage("Akun pengguna telah dihapus")
        }
    }

    fun changePassword(newPass: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.changePassword(user.id, newPass, user)
            showMessage("Kata sandi berhasil diperbarui!")
        }
    }
}
