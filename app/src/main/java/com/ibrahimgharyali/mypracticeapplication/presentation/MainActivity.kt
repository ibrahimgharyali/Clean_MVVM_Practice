package com.ibrahimgharyali.mypracticeapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewmodel: ProductViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val uistate by viewmodel.uiState.collectAsStateWithLifecycle()

    when(val state = uistate) {
        is UiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {Text(text = state.message)}
        is UiState.Loaded -> ProductListScreen(state.prod, modifier)
        is UiState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}

@Composable
fun ProductListScreen(prodList: List<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize()) {
        items(
            items = prodList,
            key = {it.id}
        ) {
            Card() {
                Column() {
                    Text(text = it.title)
                    Text(text = it.descr)
                    Text(text = it.price.toString())
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPracticeApplicationTheme {
        ProductListScreen(listOf(Product(1, "A", "c", 200.0)), Modifier)
    }
}