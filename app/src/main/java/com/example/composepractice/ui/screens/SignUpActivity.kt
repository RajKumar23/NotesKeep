package com.example.composepractice.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.composepractice.R
import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.ui.viewModel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    SignUpScreen(signUpProfile = { accountObject ->
                        lifecycleScope.launch {
                            val rowId = viewModel.insertAccount(
                                AccountModel(
                                    userName = accountObject.userName,
                                    password = accountObject.password
                                )
                            )
                            if (rowId > 0) {
                                viewModel.saveUserNameSession(accountObject.userName)
                                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Failed to create account",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(signUpProfile: (AccountModel) -> Unit) {
    val context = LocalContext.current

    var userName by remember { mutableStateOf("") }
    val isUserNameInvalid = remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    val isPasswordInvalid = remember { mutableStateOf(false) }

    var confirmPassword by remember { mutableStateOf("") }
    val isConfirmPasswordInvalid = remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
                isUserNameInvalid.value = false
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = isUserNameInvalid.value,
            trailingIcon = {
                if (isUserNameInvalid.value) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isUserNameInvalid.value) {
                    Text(text = stringResource(R.string.error_username))
                }
            })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordInvalid.value = false
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = isPasswordInvalid.value,
            trailingIcon = {
                if (isPasswordInvalid.value) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isPasswordInvalid.value) {
                    Text(text = stringResource(R.string.error_password))
                }
            })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                isConfirmPasswordInvalid.value = false
            },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = isConfirmPasswordInvalid.value,
            trailingIcon = {
                if (isConfirmPasswordInvalid.value) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isConfirmPasswordInvalid.value) {
                    Text(text = stringResource(R.string.error_password_mismatch))
                }
            })

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (validateTextFieldSignUpScreen(
                        userName, isUserNameInvalid
                    ) && validateTextFieldSignUpScreen(
                        password, isPasswordInvalid
                    ) && validateTextFieldSignUpScreen(
                        confirmPassword, isConfirmPasswordInvalid
                    ) && (password == confirmPassword).also { isConfirmPasswordInvalid.value = !it }
                ) {
                    signUpProfile(AccountModel(userName = userName, password = password))
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Text(
            text = "Already have account? Just Sign in now!",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    val intent = Intent(context, SignInActivity::class.java)
                    context.startActivity(intent)
                },
            color = Color.Blue
        )
    }
}

fun validateTextFieldSignUpScreen(
    value: String,
    errorState: androidx.compose.runtime.MutableState<Boolean>,
): Boolean {
    val isValid = value.isNotEmpty() && value.isNotBlank()
    errorState.value = !isValid
    return isValid
}
