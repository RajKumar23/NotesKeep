package com.example.composepractice.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.repository.AccountRepository
import com.example.composepractice.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository, private val sessionManager: SessionManager
) : ViewModel() {

    private val _userAccount = MutableStateFlow<AccountModel?>(null)
    val userAccount: StateFlow<AccountModel?> = _userAccount.asStateFlow()

    val allAccounts: StateFlow<List<AccountModel>> = accountRepository.getAccounts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun getAccountByUserName(userName: String): AccountModel? {
        return accountRepository.getAccountByUserName(userName)
    }

    suspend fun checkLogin(userName: String, password: String): AccountModel? {
        return accountRepository.checkLogin(userName, password)
    }

    fun getAccountById(id: Int) {
        viewModelScope.launch {
            _userAccount.value = accountRepository.getAccountById(id).first()
        }
    }

    suspend fun insertAccount(account: AccountModel): Long {
        return accountRepository.insertAccount(account)
    }

    fun saveUserIdSession(userId: Int) {
        viewModelScope.launch {
            sessionManager.saveUserId(userId)
        }
    }

    fun getUserIdSession(): Flow<Int> {
        return sessionManager.userIdFlow
    }

    fun updateAccount(password: String) {
        viewModelScope.launch {
            val id = getUserIdSession().first()
            accountRepository.updateAccount(id, password)
        }
    }

    fun deleteAccount(account: AccountModel) {
        viewModelScope.launch {
            accountRepository.deleteAccount(account)
        }
    }
}