package ca.tetervak.tipcalculatorlab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.tetervak.tipcalculatorlab2.ui.theme.TipCalculatorLab2Theme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorLab2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorScreen() {
    var billAmountInput: String by remember { mutableStateOf("") }
    val billAmount: Double = billAmountInput.toDoubleOrNull() ?: 0.0
    val tipAmount = calculateTip(billAmount)
    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.calculate_tip), fontSize = 24.sp)
        EditNumberField(billAmountInput) { billAmountInput = it }
        Text(
            text = stringResource(
                id = R.string.tip_amount,
                formatCurrency(tipAmount)
            ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

private fun calculateTip(billAmount: Double, tipPercent: Double = 15.0) =
    billAmount * tipPercent / 100

private fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance().format(value)

@Composable
fun EditNumberField(value: String, onValueChange: (String) -> Unit) {
    TextField(
        label = { Text(text = stringResource(id = R.string.bill_amount)) },
        value = value,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorLab2Theme {
        TipCalculatorScreen()
    }
}