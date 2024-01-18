package com.intern.calculator.goods

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intern.calculator.goods.ui.home.MyTopAppBar
import com.intern.calculator.goods.ui.theme.GoodsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoodsTheme {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var presses by remember { mutableIntStateOf(0) }
                    MaterialTheme {
                        Scaffold(
                            topBar = { MyTopAppBar(
                                navigationIcon = Icons.Rounded.Search,
                                navigationIconContentDescription = "Navigation icon",
                                actionIcon = Icons.Default.MoreVert,
                                actionIconContentDescription = "Action icon",
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                                ),
                            ) },
                            bottomBar = {},
                            floatingActionButton = {
                                FloatingActionButton(onClick = { presses++ }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = androidx.compose.ui.Modifier
                                    .padding(innerPadding),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text =
                                    """
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.

                    You have pressed the floating action button $presses times.
                """.trimIndent(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
