package com.ibrahimgharyali.mypracticeapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibrahimgharyali.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.LoginViewModel
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPracticeApplicationTheme {
                    MainScreen(
                        modifier = Modifier
                    )
                }
            }
        }
}

@Composable
fun MainScreen(viewModel: LoginViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val uistate by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHoststate = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.snackbatEvent) {
        viewModel.snackbatEvent.collect{ snackbarHoststate.showSnackbar(it)}
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHoststate) }
    ) { innerPadding ->
        when (val state = uistate) {
            is UIState.Error -> Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(text = state.e.message ?: "Error") }

            is UIState.Idle -> LoginScreen(
                modifier.padding(innerPadding),
                emailError = state.emailError,
                passwordError = state.passwordError,
                performClick = { email, password -> viewModel.performLogin(email, password) }
            )

            is UIState.Loading -> Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is UIState.Success -> {}
        }
    }
}



@Composable
fun LoginScreen(modifier: Modifier = Modifier, emailError: String?=null, passwordError: String?=null,  performClick: (email: String, password: String) -> Unit) {
var email by remember { mutableStateOf("") }
var password by remember { mutableStateOf("") }
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text("Sample Login screen", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier.height(60.dp))
        OutlinedTextField(
            value = email,
            label = {Text(text = "Enter Email")},
            onValueChange = {email = it},
            isError = emailError != null,
            supportingText = {emailError?.let { Text(it) }}
        )
        Spacer(modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            label = {Text(text = "Enter password")},
            onValueChange = {password = it},
            isError = passwordError != null,
            supportingText = {passwordError?.let { Text(it) }}
        )
        Spacer(modifier.height(4.dp))
        Button(

            onClick = { performClick(email, password) },
        ) {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyPracticeApplicationTheme {
        LoginScreen(performClick = { _, _ -> })
    }
}