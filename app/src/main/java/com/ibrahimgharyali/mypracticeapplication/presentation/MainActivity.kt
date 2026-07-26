package com.ibrahimgharyali.mypracticeapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import com.ibrahimgharyali.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UIState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.viewmodel.TaskViewModel
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
fun MainScreen(viewModel: TaskViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val uistate by  viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarhostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.snackBarEvent.collect { snackBarhostState.showSnackbar(it) }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {SnackbarHost(snackBarhostState)}
    ) { innerPadding ->

        when (val state = uistate) {
            is UIState.Error -> Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(text = state.e.message ?: "Something went wrong") }

            is UIState.Loaded -> TaskListScreen(
                modifier = modifier.padding(innerPadding),
                tasks = state.tasks,
                onTaskCheckedChange = { taskId -> viewModel.toggleTaskCompletion(taskId) }
            )
            UIState.Loading -> Box(
                modifier = modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(modifier.padding(4.dp)) }
        }
    }
}

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    tasks: List<Tasks>,
    onTaskCheckedChange: (Int) -> Unit
) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
        items(
            items = tasks,
            key = {it.id}
        ){ task ->
            TaskItem(
                task = task,
                onCheckedChange = { onTaskCheckedChange(task.id) }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Tasks,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyPracticeApplicationTheme {
        TaskListScreen(
            tasks = listOf(Tasks(1, "tite1", true), Tasks(2, "tite2", true)),
            onTaskCheckedChange = {}
        )
    }
}