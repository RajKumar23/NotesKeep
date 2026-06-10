package com.example.composepractice.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composepractice.ui.viewModel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditActivity : ComponentActivity() {

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                Scaffold { innerPadding ->
                    ProfileEditScreen(
                        modifier = Modifier.padding(innerPadding),
                        onUpdateProfile = { password ->
                            viewModel.updateAccount(
                                password
                            )
                            Toast.makeText(
                                this@ProfileEditActivity,
                                "New Password: $password",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        },
                        onSignOut = {
                            val intent =
                                Intent(this@ProfileEditActivity, SignInActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        })
                }
            }
        }
    }
}

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    onUpdateProfile: (String) -> Unit,
    onSignOut: () -> Unit
) {

    var password by remember { mutableStateOf("") }
    val isPasswordInvalid = remember { mutableStateOf(false) }

    var confirmPassword by remember { mutableStateOf("") }
    val isConfirmPasswordInvalid = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column {
                Text(text = "Edit Profile", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordInvalid.value = false
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isPasswordInvalid.value
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        isConfirmPasswordInvalid.value = false
                    },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isConfirmPasswordInvalid.value
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isPasswordInvalid.value = password.isBlank()
                        isConfirmPasswordInvalid.value =
                            confirmPassword.isBlank() || password != confirmPassword

                        if (!isPasswordInvalid.value && !isConfirmPasswordInvalid.value) {
                            onUpdateProfile(password)
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Profile")
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSignOut()
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Sign out",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Sign out",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}