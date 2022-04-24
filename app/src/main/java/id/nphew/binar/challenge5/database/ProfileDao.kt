package id.nphew.binar.challenge5.database

import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile Where account_id = :accountId")
    fun getProfile(accountId: Int?) : List<Profile>

    @Query("SELECT id FROM Profile Where account_id = :accountId")
    fun getPK(accountId: Int?) : Int

    @Query("SELECT fullname FROM Profile Where account_id = :accountId")
    fun getFullname(accountId: Int?) : String

    @Query("SELECT address FROM Profile Where account_id = :accountId")
    fun getAddress(accountId: Int?) : String

    @Query("SELECT birthdate FROM Profile Where account_id = :accountId")
    fun getBirthdate(accountId: Int?) : String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(Profile: Profile) : Long

    @Update
    fun updateProfile(Profile: Profile) : Int

    @Delete
    fun deleteProfile(Profile: Profile) : Int
}