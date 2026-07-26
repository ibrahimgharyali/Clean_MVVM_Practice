package com.ibrahimgharyali.mypracticeapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibrahimgharyali.mypracticeapplication.domain.model.User
import com.ibrahimgharyali.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UiState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPracticeApplicationTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewmodel: UserViewModel = hiltViewModel(), modifier : Modifier = Modifier){
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewmodel.snackBarEvent.collect() { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when(val state = uiState) {
            is UiState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = state.e.message?: "Error loading content") }
            is UiState.Loaded -> PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = {viewmodel.loadData(true)},
                modifier = Modifier.padding(innerPadding)
            ) {
                UsersComposable(state.users, modifier)
            }
        }
    }
}

@Composable
fun UsersComposable(users: List<User>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
        ) {
        items(
            items = users,
            key = { contact -> contact.id } // Prevents unnecessary recompositions
        ) { user ->
            UserRow(user = user)
        }

    }
}

@Composable
fun UserRow(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = user.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = user.body,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersComposablePreview() {
    MyPracticeApplicationTheme {
        UsersComposable(listOf( User(1, "title1", "body1")))
    }
}