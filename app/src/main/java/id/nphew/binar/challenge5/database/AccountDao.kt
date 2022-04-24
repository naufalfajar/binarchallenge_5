package id.nphew.binar.challenge5.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface AccountDao {
    @Query("SELECT * FROM Account")
    fun getAllAccount() : List<Account>

    @Query("SELECT * FROM Account where email = :email")
    fun getAccount(email: String) : Account

    @Query("select * from Account where email = :email")
    fun checkEmailAccount(email: String) : List<Account>

    @Query("select password from Account where email = :email")
    fun getPassword(email: String?) : String

    @Query("select username from Account where email = :email")
    fun getUsername(email: String?) : String

    @Query("select id from Account where email = :email")
    fun getId(email: String?) : Int

    @Insert(onConflict = REPLACE)
    fun insertAccount(account: Account) : Long

    @Update
    fun updateAccount(account: Account) : Int

    @Query("update Account set username = :username where email = :email")
    fun updateUsername(email: String?, username: String?) : Int

    @Delete
    fun deleteAccount(account: Account) : Int
}
