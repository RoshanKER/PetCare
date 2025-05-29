package com.rosh2.petcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rosh2.petcare.ui.theme.PetCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            PetCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestLayout(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
// iuyoiuyiuyi
@Composable
fun TestLayout(name: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxHeight().width(100.dp).background(Color.Yellow)) {
            Text(text = "Column 1")
        }
        Column(modifier = Modifier.fillMaxHeight().width(100.dp).background(Color.Gray)) {
            Text(text = "Column 2")
        }
        Column(modifier = Modifier.fillMaxHeight().fillMaxWidth().background(Color.Green),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Column 3")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PetCareTheme{
        TestLayout("Android")
    }
}