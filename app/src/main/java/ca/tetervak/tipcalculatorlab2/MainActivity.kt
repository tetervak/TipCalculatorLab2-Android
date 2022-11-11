package ca.tetervak.tipcalculatorlab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
    val costOfServiceInput = remember { mutableStateOf("") }
    val tipPercentInput =  remember { mutableStateOf("") }
    val roundUpTipInput = remember { mutableStateOf( true ) }

    val tipAmount = calculateTip(
        costOfService = costOfServiceInput.value.toDoubleOrNull() ?: 0.0,
        tipPercent = tipPercentInput.value.toDoubleOrNull() ?: 0.0,
        roundUpTip = roundUpTipInput.value
    )

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        EditNumberField(
            label = R.string.cost_of_service_label,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            value = costOfServiceInput.value,
            onValueChanged = { costOfServiceInput.value = it }
        )
        EditNumberField(
            label = R.string.service_quality_label,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            value = tipPercentInput.value,
            onValueChanged = { tipPercentInput.value = it }
        )
        RoundTheTipRow(
            roundUp = roundUpTipInput.value,
            onRoundUpChanged = { roundUpTipInput.value = it })
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.tip_amount, formatCurrency(tipAmount)),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip_label))
        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray
            )
        )
    }
}

private fun calculateTip(
    costOfService: Double,
    tipPercent: Double = 15.0,
    roundUpTip: Boolean = true): Double {
    var tipAmount = tipPercent * costOfService / 100
    if (roundUpTip){
        tipAmount = kotlin.math.ceil(tipAmount)
    }
    return tipAmount
}

private fun formatCurrency(value: Double): String =
    NumberFormat.getCurrencyInstance().format(value)

@Composable
fun EditNumberField(value: String, onValueChange: (String) -> Unit) {
    TextField(
        label = { Text(text = stringResource(id = R.string.cost_of_service_label)) },
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