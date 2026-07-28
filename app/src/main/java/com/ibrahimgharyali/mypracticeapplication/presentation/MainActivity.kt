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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ibrahimgharyali.mypracticeapplication.domain.Product
import com.ibrahimgharyali.mypracticeapplication.presentation.theme.MyPracticeApplicationTheme
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.ProdState
import com.ibrahimgharyali.mypracticeapplication.presentation.ui.theme.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint


// Product list -> id, title, desc, price
// fetch the last -> filter based on price > 100, descending by price
// display it the UI

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPracticeApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifer = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
// subram.lastname@wipro
//firstnamelastname@gmail.com
@Composable
fun MainScreen(viewmodel: ProductViewModel = hiltViewModel(), modifer: Modifier = Modifier) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    when(val state = uiState) {
        is ProdState.Error -> Box(modifer.fillMaxSize(), contentAlignment = Alignment.Center){ Text(text = state.message)}
        is ProdState.Loaded -> ProductListComposable(state.list, modifer)
        is ProdState.Loading -> Box(modifer.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}

@Composable
fun ProductListComposable(productList: List<Product>, modifier: Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = productList,
            key = {prod -> prod.id}
        ) { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(prod: Product) {
    Card(Modifier) {
        Column() {
            Text(text = prod.title)
            Spacer(Modifier.height(4.dp))
            Text(text = prod.desc)
            Spacer(Modifier.height(4.dp))
            Text(text = prod.price.toString())
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
        Greeting("Android")
    }
}