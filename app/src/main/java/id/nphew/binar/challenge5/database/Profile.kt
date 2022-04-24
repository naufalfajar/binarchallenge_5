package id.nphew.binar.challenge5.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "account_id") val accountId: Int?,
    @ColumnInfo(name = "fullname") val fullName: String,
    @ColumnInfo(name = "birthdate") val birthDate: String,
    @ColumnInfo(name = "address") val address: String
)
