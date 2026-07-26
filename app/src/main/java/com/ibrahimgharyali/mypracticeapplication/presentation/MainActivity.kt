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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibrahimgharyali.mypracticeapplication.domain.Tasks
import com.ibrahimgharyali.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.UiState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPracticeApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel : TaskViewModel = hiltViewModel()) {
    val uistate by viewModel.uistate.collectAsStateWithLifecycle()
    when(val state = uistate) {
        is UiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {Text(text = "Error: ${state.e.message}!")}
        is UiState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(modifier.height(16.dp)) }
        is UiState.Loaded -> TaskScreenComposable(
            modifier = modifier,
            tasks = state.tasks,
            onTaskCheckedChange = { taskId -> viewModel.toggleTaskCompletion(taskId) }
        )
    }
}

@Composable
fun TaskScreenComposable(
    modifier: Modifier = Modifier,
    tasks: List<Tasks>,
    onTaskCheckedChange: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(
            items = tasks,
            key = {task -> task.id}
        ){ task ->
            TaskRow(
                task = task,
                onCheckedChange = { onTaskCheckedChange(task.id) }
            )
        }
    }
}

@Composable
fun TaskRow(modifier: Modifier = Modifier, task: Tasks, onCheckedChange: (Boolean) -> Unit) {
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
        TaskScreenComposable(
            tasks = listOf(Tasks(1, "title", true), Tasks(2, "title 2", false)),
            onTaskCheckedChange = {}
        )
    }
}