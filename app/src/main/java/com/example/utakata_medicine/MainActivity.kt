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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview
import com.example.utakata_medicine.ui.theme.UtakatamedicineTheme
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityScope = CoroutineScope(SupervisorJob())
        val db by lazy {MedicineRoomDatabase.getDatabase(this,activityScope)}
        val repository by lazy {MedicineRepository(db.MedicineDao())}

        setContent {
            UtakatamedicineTheme {
                UtakataUnderTabLayout(repository)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label:  String
){
    val foucusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions= KeyboardActions(
            onDone = {
                foucusManager.moveFocus(FocusDirection.Down)
            }
        ),
        label = { Text(text = label)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalCustomInput(
    value: String,
    onValueChange: (String) -> Unit,
    submit: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                submit()
            }
        ),
        label = { Text("場所") },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputForm(modifier: Modifier,medicineRepository: MedicineRepository){

    var ajax by remember { mutableStateOf(false) }
    var inputname by remember {mutableStateOf("")}
    var inputwhentime by remember {mutableStateOf("")}
    var inputpiecestr by remember {mutableStateOf("")}
    var inputhospital by remember {mutableStateOf("")}
    var inputplace by remember {mutableStateOf("")}

    val scope = rememberCoroutineScope()
    val submit = {
        scope.launch(Dispatchers.IO) {
            ajax = true
            val SubmitData = MedicineClass (
                inputname,
                inputwhentime,
                inputpiecestr,
                inputhospital,
                inputplace
            )

            medicineRepository.insert(SubmitData)
            // 送信処理
            ajax = false
        }
        Unit
    }
    UtakatamedicineTheme{
        Text(text = "お薬登録")
        Column(modifier = modifier.padding(25.dp,15.dp,0.dp,0.dp)) {
            Text(text = "名前")
            CustomTextInput(value = inputname, onValueChange = {inputname = it}, label = "名前")
            Text(text = "時間")
            CustomTextInput(value = inputwhentime, onValueChange = {inputwhentime = it}, label = "時間")
            Text(text = "錠数")
            CustomTextInput(value = inputpiecestr, onValueChange = {inputpiecestr = it}, label = "錠数")
            Text(text = "処方箋かそうでないか")
            CustomTextInput(value = inputhospital, onValueChange = {inputhospital = it}, label = "処方箋ですか？")
            Text(text = "場所")
            FinalCustomInput(value = inputplace, onValueChange = {inputplace = it},submit = submit)
            FilledTonalButton(onClick = submit) {
                Text(text = "登録")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineTable(repository: MedicineRepository){
    val MedicineAllData: LiveData<List<MedicineClass>> = repository.allMedcines.asLiveData()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        //これ消すとダメっぽい。
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
    )
    {
        padding -> MedicineAllData.value?.size?.let { List(it) { index -> index to "Item $index" } }
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
                TableCell(text = "名前", weight = column1Weight)
                TableCell(text = "錠数", weight = column2Weight)
            }

            MedicineAllData.value?.map {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    it.let { it -> it.name?.let { it1 -> TableCell(text = it1, weight = column1Weight) } }
                    it.let {it -> it.piecestr?.let { it1 -> TableCell(text = it1, weight = column2Weight) } }
                }
            }
        }
    }
}

// @Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtakataUnderTabLayout(medicineRepository: MedicineRepository) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("お薬表", "登録", "服薬リスト")
    /*val medicineHonbanData = arrayListOf<MedicineClass>(
        MedicineClass(
            "test",
            "朝",
            "2",
            "はい",
            "病院"
            ))*/
    UtakatamedicineTheme {
        Scaffold(
            bottomBar = {NavigationBar(/*modifier = Modifier.fillMaxWidth().fillMaxHeight()*/) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        label = { Text(text = item) },
                        icon = { Icon(Icons.Filled.Favorite, item) }
                    )
                }
            }},) {
                padding -> List(items.size) { index -> index to "Item $index" }
                when (selectedItem) {
                    0 -> MedicineTable(repository = medicineRepository)
                    1 -> InputForm(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(16.dp),medicineRepository)  //Text(text = "test2", modifier = Modifier.fillMaxHeight(0.7f))
                    2 -> Text(text = "test3", modifier = Modifier.fillMaxHeight(0.7f))
                }

        }
    }
}