package com.example.utakata_medicine

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
// import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Fts4
@Entity(tableName = "medicines")

data class MedicineClass (
    @PrimaryKey @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "time")val whentime: String? = null,
    @ColumnInfo(name = "piece")val piecestr: String? = null,
    @ColumnInfo(name = "hospital") val hospital: String? = null,
    @ColumnInfo(name = "place")val place: String? = null,
)

@Dao
interface MedicineDao{

    @Insert
    fun insertMedicine(vararg medicineClass: MedicineClass)

    /*@Delete
    fun DeleteMedicine(vararg medicineClass: MedicineClass)*/
    @Query("SELECT * FROM medicines ORDER BY name ASC")
    fun getAll(): Flow<List<MedicineClass>>

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()
}

@Database(entities = arrayOf(MedicineClass::class), version = 1)
abstract class MedicineRoomDatabase: RoomDatabase(){
    abstract fun MedicineDao(): MedicineDao
    private class MedicineDatabaseCallback(
        private val scope: CoroutineScope
    ): Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                database -> scope.launch {
                    populateDatabase(database.MedicineDao())
                }
            }
        }
        suspend fun populateDatabase(medicineDao: MedicineDao){
            medicineDao.deleteAll()
            val testmedicine = MedicineClass(
                "TESTER",
                "朝",
                "2",
                "Yes",
                "病院"
            )
            medicineDao.insertMedicine(testmedicine)
            val testmedicine2 = testmedicine.copy("TESTER2", hospital = "いいえ", place = "ドラッグストア")
            medicineDao.insertMedicine(testmedicine2)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: MedicineRoomDatabase? = null
        fun getDatabase(context: Context,scope: CoroutineScope):MedicineRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicineRoomDatabase::class.java,
                    "test.db",
                )
                .allowMainThreadQueries()
                .addCallback(MedicineDatabaseCallback(scope))
                .build()

            INSTANCE = instance
            instance
            }
        }
    }

}
