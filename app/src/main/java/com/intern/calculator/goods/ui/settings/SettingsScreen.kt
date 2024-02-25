package com.intern.calculator.goods.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRightAlt
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.intern.calculator.goods.R
import com.intern.calculator.goods.ui.AppViewModelProvider
import com.intern.calculator.goods.ui.components.MyTopAppBar
import com.intern.calculator.goods.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.util.Locale


object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.setting_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigateUp: () -> Unit,
                   navigateToAbout: () -> Unit,
                   viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val preferences by viewModel.userPreferences.collectAsState(
        initial = UserPreferences(
            Theme.System,
            viewModel.toLanguage(Locale.getDefault().getLanguage()),
            1)
    )
    val coroutineScope = rememberCoroutineScope()
    val showThemeDialog = remember { mutableStateOf(false) }
    val showLanguageDialog = remember { mutableStateOf(false) }

    if (showThemeDialog.value) {
        AlertDialog(
            onDismissRequest = { showThemeDialog.value = false },
            title = { Text(text = stringResource(id = R.string.setting_theme)) },
            text = {
                Box(modifier = Modifier) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .selectableGroup()
                            .padding(all = 0.dp),
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = preferences.theme == Theme.System,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.updateTheme(Theme.System)
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = "Localized Description" }
                            )
                            Text(
                                text = stringResource(id = R.string.setting_theme_system),
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = preferences.theme == Theme.Light,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.updateTheme(Theme.Light)
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = "Localized Description" }
                            )
                            Text(
                                text = stringResource(id = R.string.setting_theme_light),
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = preferences.theme == Theme.Dark,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.updateTheme(Theme.Dark)
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = "Localized Description" }
                            )
                            Text(
                                text = stringResource(id = R.string.setting_theme_dark),
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        showThemeDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.nav_drawer_modal_action_cancel_text))
                }
            },
        )
    }
    if (showLanguageDialog.value) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog.value = false },
            title = { Text(text = stringResource(id = R.string.setting_language)) },
            text = {
                Box(modifier = Modifier) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .selectableGroup()
                            .padding(all = 0.dp),
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = preferences.language == Language.English,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.updateLanguage(Language.English)
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = "Localized Description" }
                            )
                            Text(
                                text = stringResource(id = R.string.setting_language_english),
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = preferences.language == Language.Русский,
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.updateLanguage(Language.Русский)
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = "Localized Description" }
                            )
                            Text(
                                text = stringResource(id = R.string.setting_language_russian),
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        showLanguageDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.nav_drawer_modal_action_cancel_text))
                }
            },
        )
    }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
                navigationIcon = Icons.Outlined.ArrowBack,
                navigationIconContentDescription = "Navigate back",
                actionIcon = null,
                actionIconContentDescription = null,
                onNavigationClick = navigateUp
            )
        }
    ) { padding ->
        val context = LocalContext.current
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .width(600.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Divider()
                ListItem(
                    headlineContent = {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.DarkMode,
                                contentDescription = stringResource(id = R.string.setting_theme),
                                modifier = Modifier.padding(end = 2.dp)
                            )
                            Text(text = stringResource(id = R.string.setting_theme))
                        }
                    },
                    supportingContent = {Text(text = stringResource(id = preferences.theme.title))},
                    modifier = Modifier.clickable {
                        showThemeDialog.value = true
                    },
                )
                Divider()
                ListItem(
                    headlineContent = {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Translate,
                                contentDescription = stringResource(id = R.string.setting_language),
                                modifier = Modifier.padding(end = 2.dp)
                            )
                            Text(text = stringResource(id = R.string.setting_language))
                        }
                    },
                    supportingContent = {Text(text = preferences.language.name)},
                    modifier = Modifier.clickable {
                        showLanguageDialog.value = true
                    },
                )
                Divider()
                ListItem(
                    headlineContent = {
                        Row {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Message,
                                contentDescription = stringResource(id = R.string.setting_send_feedback),
                                modifier = Modifier.padding(end = 2.dp)
                            )
                            Text(text = stringResource(id = R.string.setting_send_feedback))
                        }
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowRightAlt,
                            contentDescription = stringResource(id = R.string.setting_send_feedback),
                            modifier = Modifier.padding(end = 2.dp)
                        )
                    },
                    modifier = Modifier.clickable {
                        val email = "mcrn158@gmail.com"
                        val subject = context.getString(R.string.setting_mail_subject)
                        val message = context.getString(R.string.setting_mail_message)

                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$email")
                            putExtra(Intent.EXTRA_SUBJECT, subject)
                            putExtra(Intent.EXTRA_TEXT, message)
                        }
                            context.startActivity(intent)

                    },
                )
                Divider()
                ListItem(
                    headlineContent = {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = stringResource(id = R.string.setting_about_app),
                                modifier = Modifier.padding(end = 2.dp)
                            )
                            Text(text = stringResource(id = R.string.setting_about_app))
                        }
                    },
                    modifier = Modifier.clickable {
                        navigateToAbout()
                    },
                )
            }
        }
    }
}