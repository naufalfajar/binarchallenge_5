package id.nphew.binar.challenge5.repository

import android.content.Context
import id.nphew.binar.challenge5.database.Account
import id.nphew.binar.challenge5.database.AccountDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepo(context: Context) {
    private val db = AccountDatabase.getInstance(context)

    suspend fun getAllAccount() = withContext(Dispatchers.IO) {
        db?.accountDao()?.getAllAccount()
    }

    suspend fun checkEmailAccount(email: String) = withContext(Dispatchers.IO) {
        db?.accountDao()?.checkEmailAccount(email)
    }

    suspend fun getPassword(email: String) = withContext(Dispatchers.IO) {
        db?.accountDao()?.getPassword(email)
    }

    suspend fun getUsername(email: String) = withContext(Dispatchers.IO) {
        db?.accountDao()?.getUsername(email)
    }

    suspend fun getId(email: String) = withContext(Dispatchers.IO) {
        db?.accountDao()?.getId(email)
    }

    suspend fun insertAccount(account: Account) = withContext(Dispatchers.IO) {
        db?.accountDao()?.insertAccount(account)
    }

    suspend fun updateAccount(account: Account) = withContext(Dispatchers.IO) {
        db?.accountDao()?.updateAccount(account)
    }

    suspend fun deleteAccount(account: Account) = withContext(Dispatchers.IO) {
        db?.accountDao()?.deleteAccount(account)
    }

    suspend fun updateUsername(email: String, username: String) = withContext(Dispatchers.IO) {
        db?.accountDao()?.updateUsername(email, username)
    }
}