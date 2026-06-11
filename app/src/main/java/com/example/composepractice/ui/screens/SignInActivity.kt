package com.example.composepractice.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.composepractice.R
import com.example.composepractice.ui.viewModel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    SignInUI(
                        name = "To Notes App!", viewModel = viewModel
                    )
                }
            }/*ComposePracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginUI(
                        name = "To Notes App!", modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
        }
    }
}

@Composable
fun SignInUI(name: String, modifier: Modifier = Modifier, viewModel: AccountViewModel) {

    val context = LocalContext.current
    var userNameState by remember { mutableStateOf("") }
    val isUserNameInvalid = remember { mutableStateOf(false) }

    var passwordState by remember { mutableStateOf("") }
    val isPasswordInvalid = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Text(
            text = "Hello $name!",
            modifier = Modifier.padding(top = 16.dp)
        )*/
        Text(
            text = "Welcome $name!!!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        OutlinedTextField(
            value = userNameState,
            onValueChange = {
                userNameState = it
                isUserNameInvalid.value = false
            },
            label = { Text("Enter Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            // 2. Pass the validation boolean here
            isError = isUserNameInvalid.value,
            // 3. Optional: Add a warning icon inside the field
            trailingIcon = {
                if (isUserNameInvalid.value) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            // 4. Optional: Display the error text directly beneath the field
            supportingText = {
                if (isUserNameInvalid.value) {
                    Text(
                        text = stringResource(R.string.error_username),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            })
        OutlinedTextField(
            value = passwordState,
            onValueChange = {
                passwordState = it
                isPasswordInvalid.value = false
            },
            label = { Text("Enter Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
                    Text(
                        text = stringResource(R.string.error_password),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
        Button(
            onClick = {
                if (validateTextField(
                        userNameState, isUserNameInvalid
                    ) && validateTextField(
                        passwordState, isPasswordInvalid
                    )
                ) {
                    scope.launch {
                        val tableAccountDetails = viewModel.checkLogin(
                            userNameState, passwordState
                        )
                        if (tableAccountDetails != null) {
                            viewModel.saveUserIdSession(tableAccountDetails.id)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            if (context is ComponentActivity) {
                                context.finish()
                            }
                        } else {
                            isUserNameInvalid.value = true
                            isPasswordInvalid.value = true
                            Toast.makeText(
                                context, "Invalid username or password", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Sign In")
        }
        Text(
            text = "Don't have account? Sign up now!",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable {
                    val intent = Intent(context, SignUpActivity::class.java)
                    context.startActivity(intent)
                },
            color = Color.Blue
        )
    }
}

fun validateTextField(
    value: String,
    errorState: MutableState<Boolean>,
): Boolean {
    val isValid = value.isNotEmpty() && value.isNotBlank()
    errorState.value = !isValid
    return isValid
}

