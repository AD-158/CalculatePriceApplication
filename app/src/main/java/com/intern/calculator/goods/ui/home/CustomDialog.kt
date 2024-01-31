import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.intern.calculator.goods.R

@Composable
fun CustomDialog(
    oldValue: String,
    neededAction: Int,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(onDismissRequest = {}) {
        CustomDialogUI(
            oldValue = oldValue,
            neededAction = neededAction,
            onConfirmation = onConfirmation,
            onCancel = onCancel,
        )
    }
}

@Composable
fun CustomDialogUI(
    modifier: Modifier = Modifier,
    oldValue: String,
    neededAction: Int,
    onConfirmation: () -> Unit,
    onCancel: () -> Unit,
) {
    var newValue by remember {
        mutableStateOf("")
    }
    Card(
        shape = MaterialTheme.shapes.medium,
        // modifier = modifier.size(280.dp, 240.dp)
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
    ) {
        Column(modifier.background(Color.White)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (neededAction) {
                        0 -> stringResource(R.string.nav_drawer_modal_action_0_title)
                        1 -> stringResource(R.string.nav_drawer_modal_action_1_title)
                        else -> stringResource(R.string.nav_drawer_modal_action_2_title)
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if ((neededAction == 0) or (neededAction == 1)) {
                    OutlinedTextField(
                        value = newValue,
                        onValueChange = { newValue = it }, modifier = Modifier.padding(8.dp),
                        label = {
                            Text(
                                text = when (neededAction) {
                                    0 -> stringResource(R.string.nav_drawer_modal_action_0_input_value)
                                    1 -> stringResource(R.string.nav_drawer_modal_action_1_input_value)
                                    else -> stringResource(R.string.nav_drawer_modal_action_0_input_value)
                                },
                            )
                        },
                    )
                }
                Text(
                    text = when (neededAction) {
                        0 -> stringResource(R.string.nav_drawer_modal_action_0_text)
                        1 -> stringResource(R.string.nav_drawer_modal_action_1_text) + oldValue
                        else -> stringResource(R.string.nav_drawer_modal_action_2_text) + oldValue + "?"
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 0.dp, start = 5.dp, end = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp)
                    .background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                TextButton(onClick = {
                    onCancel()
                }, Modifier.fillMaxWidth().weight(1f)) {
                    Text(
                        text = stringResource(R.string.nav_drawer_modal_action_cancel_text),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
                TextButton(onClick = {
                    onConfirmation()
                }, Modifier.fillMaxWidth().weight(1f)) {
                    Text(
                        text = stringResource(when (neededAction) {
                            0 -> R.string.nav_drawer_modal_action_0_title
                            1 -> R.string.nav_drawer_modal_action_1_title
                            else -> R.string.nav_drawer_modal_action_2_title
                        }),
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }
            }
        }
    }
}