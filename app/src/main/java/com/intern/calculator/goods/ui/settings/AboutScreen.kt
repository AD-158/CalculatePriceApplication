package com.intern.calculator.goods.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.intern.calculator.goods.R
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

// Define the destination for the about screen
object AboutDestination : NavigationDestination {
    override val route = "about_app"
    override val titleRes = R.string.about_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(AboutDestination.titleRes),
                navigationIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                navigationIconContentDescription = "Navigate back",
                actionIcon = null,
                actionIconContentDescription = null,
                onNavigationClick = navigateUp
            )
        },
    ) { padding ->
        // Retrieve the application context
        val context = LocalContext.current
        // Retrieve package information to display version and last update date
        val info = context.packageManager.getPackageInfo(
            context.packageName, 0
        )
        val versionName = info.versionName
        val versionDate = SimpleDateFormat
            .getDateInstance(DateFormat.LONG, context.resources.configuration.locale)
            .format(Date(info.lastUpdateTime ?: 0))
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(10.dp)
                .wrapContentHeight(Alignment.Top)
        ) {
            // Display the app icon
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Display the app name
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // Display the app version
            Text(
                text = (stringResource(id = R.string.about_version) + versionName),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // Display the last update date
            Text(
                text = (stringResource(id = R.string.about_time) + versionDate),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
