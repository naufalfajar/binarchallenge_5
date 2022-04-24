package id.nphew.binar.challenge5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Account::class, Profile::class], version = 1, exportSchema = false)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private var INSTANCE: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase? {
            if (INSTANCE == null) {
                synchronized(AccountDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AccountDatabase::class.java, "Account.db").build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}