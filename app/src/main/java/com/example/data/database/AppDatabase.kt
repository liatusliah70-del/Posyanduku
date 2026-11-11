package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AuditLogDao
import com.example.data.dao.LaporanDao
import com.example.data.dao.PosyanduDao
import com.example.data.dao.RegionDao
import com.example.data.dao.SpmDao
import com.example.data.dao.UserDao
import com.example.data.model.AuditLogEntity
import com.example.data.model.DesaEntity
import com.example.data.model.KecamatanEntity
import com.example.data.model.LaporanEntity
import com.example.data.model.PosyanduEntity
import com.example.data.model.SpmKesehatanEntity
import com.example.data.model.SpmPekerjaanUmumEntity
import com.example.data.model.SpmPendidikanEntity
import com.example.data.model.SpmPerumahanEntity
import com.example.data.model.SpmSosialEntity
import com.example.data.model.SpmTrantibumEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        KecamatanEntity::class,
        DesaEntity::class,
        PosyanduEntity::class,
        SpmPendidikanEntity::class,
        SpmKesehatanEntity::class,
        SpmPekerjaanUmumEntity::class,
        SpmPerumahanEntity::class,
        SpmTrantibumEntity::class,
        SpmSosialEntity::class,
        LaporanEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun regionDao(): RegionDao
    abstract fun posyanduDao(): PosyanduDao
    abstract fun spmDao(): SpmDao
    abstract fun laporanDao(): LaporanDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "posyanduku_indramayu.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate initial data asynchronously upon creation
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context).let { database ->
                                    InitialDataSeeder.seedDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
