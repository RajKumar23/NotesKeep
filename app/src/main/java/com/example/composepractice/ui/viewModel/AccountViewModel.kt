package com.example.composepractice.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    ViewModel() {

    val allAccounts = accountRepository.getAccounts()

    suspend fun getAccountByUserName(userName: String): AccountModel? {
        return accountRepository.getAccountByUserName(userName)
    }

    suspend fun insertAccount(account: AccountModel): Long {
        return accountRepository.insertAccount(account)
    }

    fun updateAccount(account: AccountModel) {
        viewModelScope.launch {
            accountRepository.updateAccount(account)
        }
    }

    fun deleteAccount(account: AccountModel) {
        viewModelScope.launch {
            accountRepository.deleteAccount(account)
        }
    }
}