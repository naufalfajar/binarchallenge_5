package id.nphew.binar.challenge5.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.nphew.binar.challenge5.database.Account
import id.nphew.binar.challenge5.repository.AccountRepo
import kotlinx.coroutines.launch

class AccountViewModel(private val accRepo: AccountRepo): ViewModel() {
    val account = MutableLiveData<List<Account>>()

    val update = MutableLiveData<Boolean>()
    val registered = MutableLiveData<Boolean>()
    val insert = MutableLiveData<Boolean>()

    val emailexist = MutableLiveData<Boolean>()
    val emailnotexist = MutableLiveData<Boolean>()

    val passmatch = MutableLiveData<Boolean>()
    val passnotmatch = MutableLiveData<Boolean>()

    fun getAllAccount() {
        viewModelScope.launch {
            account.value = accRepo.getAllAccount()
        }
    }

    fun insertAccount(account: Account){
        viewModelScope.launch {
            val result = accRepo.insertAccount(account)
            insert.value = true
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch {
            val result = accRepo.updateAccount(account)
            update.value = result != 0
        }
    }

    fun checkEmail(email: String, account: Account){
        viewModelScope.launch {
            val emailDb = accRepo.checkEmailAccount(email)
            if(!emailDb.isNullOrEmpty())
                registered.value = true
            else
                insertAccount(account)
        }
    }

    fun checkLoginEmail(email: String){
        viewModelScope.launch {
            val emailDb = accRepo.checkEmailAccount(email)
            if(!emailDb.isNullOrEmpty())
                emailexist.value = true
            else
                emailnotexist.value = true
        }
    }

    fun checkPassword(email: String, password: String){
        viewModelScope.launch {
            val pass = accRepo.getPassword(email)
            if(password == pass)
                passmatch.value = true
            else
                passnotmatch.value = true
        }
    }


}