package com.example.utakata_medicine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UtakatamedicineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().fillMaxHeight(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UtakataUnderTabLayout()
                }
            }
        }
    }
}

data class Medicine (
    val name: String,
    val whentime: String,
    val piecestr: String,
    val hospital: Boolean,
    val place: String,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtakataMedicineTable(data: List<Medicine>){
    UtakatamedicineTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxHeight(0.7F)
        )
        {
            padding -> List(data.size) { index -> index to "Item $index" }
            val column1Weight = .3f
            val column2Weight = .7f

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.background(color = Color.Gray)
                ) {
                    TableCell(text = "Column 1", weight = column1Weight)
                    TableCell(text = "Column 2", weight = column2Weight)
                }

                data.map {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TableCell(text = it.name, weight = column1Weight)
                        TableCell(text = it.piecestr, weight = column2Weight)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
){
    Text(text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp))
}

@Preview(showBackground = true)
@Composable
fun UtakataUnderTabLayout(){
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("お薬表","登録","服薬リスト")
    val testData = Medicine("test","朝","2",true,"病院")
    val testData2 = Medicine("test2","昼","2",false,"ドラッグストア")
    val medicineData:List<Medicine> = listOf(
        testData,testData2,testData.copy(),testData2.copy()
        )
    // val UnitList = listOf(UtakataMedicineTable(data = medicineData),Text(text = "test2"),Text(text = "test3"))
    UtakatamedicineTheme {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Row(
                    //modifier = Modifier.fillMaxWidth(0.7f).height(LocalConfiguration.current.screenHeightDp.dp / 3)
                )
                {
                    when (selectedItem) {
                        0 -> UtakataMedicineTable(data = medicineData)
                        1 -> Text(text = "test2",modifier = Modifier.fillMaxHeight(0.7f))
                        2 -> Text(text = "test3",modifier = Modifier.fillMaxHeight(0.7f))
                    }
                }
                Row {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth().fillMaxHeight(0.3f)//LocalConfiguration.current.screenHeightDp.dp / 7)
                    ) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                                label = { Text(text = item) },
                                icon = { Icon(Icons.Filled.Favorite, item) }
                            )
                        }
                    }
                }
            }


    }
}