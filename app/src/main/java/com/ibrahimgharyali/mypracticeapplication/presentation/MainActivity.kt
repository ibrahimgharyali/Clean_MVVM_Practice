package com.ibrahimgharyali.mypracticeapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.ProductViewModel
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.UiState
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
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewmodel: ProductViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val uiState by viewmodel.uistate.collectAsStateWithLifecycle()

    when(val state = uiState) {
        is UiState.Error -> Box() { Text(text = state.message)}
        is UiState.Loaded -> ProductListScreen(state.prodList, modifier)
        UiState.Loading -> Box() { CircularProgressIndicator() }
    }
}

@Composable
fun ProductListScreen(list: List<Product>, modifier: Modifier) {
    LazyColumn(modifier) {
        items(items = list,
            key = {it.id}) {
            Card() {
                Column() {
                    Text(text = it.title)
                    Spacer(Modifier.height(4.dp))
                    Text(text = it.desc)
                    Spacer(Modifier.height(4.dp))
                    Text(text = it.price.toString())
                    Spacer(Modifier.height(12.dp))

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPracticeApplicationTheme {
        ProductListScreen(listOf(Product(1, "A", "c", 100.0)), Modifier)
    }
}