package com.example.composepractice.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.composepractice.R

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginUI(
                        name = "To Notes App!"
                    )
                }
            }
            /*ComposePracticeTheme {
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
fun LoginUI(name: String, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val userNameState = androidx.compose.runtime.remember { mutableStateOf("") }
    val isUserNameInvalid = androidx.compose.runtime.remember { mutableStateOf(false) }

    val passwordState = androidx.compose.runtime.remember { mutableStateOf("") }
    val isPasswordInvalid = androidx.compose.runtime.remember { mutableStateOf(false) }

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
            value = userNameState.value,
            onValueChange = {
                userNameState.value = it
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
            }
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
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
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                if (context is ComponentActivity) {
                    context.finish()
                }
                /*if (validateTextField(
                        userNameState.value, isUserNameInvalid
                    ) && validateTextField(
                        passwordState.value, isPasswordInvalid
                    )
                ) {
                    Toast.makeText(
                        context, "Login Successful: ${userNameState.value}", Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    if (context is ComponentActivity) {
                        context.finish()
                    }
                }*/
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Login")
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
    errorState: androidx.compose.runtime.MutableState<Boolean>,
): Boolean {
    val isValid = value.isNotEmpty() && value.isNotBlank()
    errorState.value = !isValid
    return isValid
}

