package com.example.utakata_medicine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.utakata_medicine.ui.theme.UtakatamedicineTheme
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UtakatamedicineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

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
    UtakatamedicineTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun UtakataUnderTabLayout(){
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("お薬表","登録","服薬リスト")
    UtakatamedicineTheme {
        NavigationBar {
            items.forEachIndexed {index,item ->
                NavigationBarItem(
                    selected = selectedItem == index ,
                    onClick = { selectedItem = index },
                    label = { Text(text = item)},
                    icon = { Icon(Icons.Filled.Favorite, item)}
                )
            }
        }
    }
}