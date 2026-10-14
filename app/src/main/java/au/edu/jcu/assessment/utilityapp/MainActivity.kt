package au.edu.jcu.assessment.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.assessment.utilityapp.ui.theme.MobileTech_UtilityAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MobileTech_UtilityAppTheme {
                UtilityApp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    MobileTech_UtilityAppTheme {
        UtilityApp()
    }
}


@Composable
fun UtilityApp() {

    var selectedTab by remember {
        mutableStateOf("Utility")
    }

    val calorieViewModel: CalorieViewModel = viewModel()

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Utility"
                        )
                    },
                    label = {
                        Text("Utility")
                    },
                    selected = selectedTab == "Utility",
                    onClick = {
                        selectedTab = "Utility"
                    }
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = {
                        Text("Settings")
                    },
                    selected = selectedTab == "Settings",
                    onClick = {
                        selectedTab = "Settings"
                    }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding)
        ) {

            when (selectedTab) {

                "Utility" -> UtilityScreen(
                    calorieViewModel = calorieViewModel
                )

                "Settings" -> SettingsScreen(
                    calorieViewModel = calorieViewModel
                )
            }
        }
    }
}


@Composable
fun UtilityScreen(
    calorieViewModel: CalorieViewModel
) {

    val uiState by calorieViewModel.uiState.collectAsState()

    val calorieText =
        if (uiState.showDecimals) {
            "%.1f kcal".format(uiState.estimatedCalories)
        } else {
            "%.0f kcal".format(uiState.estimatedCalories)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Calorie Calculator",
            style = MaterialTheme.typography.headlineMedium
        )


        // Food search field

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { newValue ->
                calorieViewModel.updateSearchQuery(newValue)
            },
            label = {
                Text("Search food")
            },
            modifier = Modifier.fillMaxWidth()
        )


        // Search button

        Button(
            onClick = {
                calorieViewModel.searchFoods()
            }
        ) {
            Text("Search")
        }


        // Loading message

        if (uiState.isLoading) {
            Text(
                text = "Loading..."
            )
        }


        // Error message

        uiState.errorMessage?.let { message ->

            Text(
                text = message
            )
        }


        // Display first 5 API results

        uiState.searchResults
            .take(5)
            .forEach { food ->

                Text(
                    text = food.description
                )
            }


        // Calories per 100g

        OutlinedTextField(
            value = uiState.caloriesPer100g,
            onValueChange = { newValue ->

                if (
                    newValue.all {
                        it.isDigit() || it == '.'
                    }
                ) {

                    calorieViewModel.updateCaloriesPer100g(
                        newValue
                    )
                }
            },
            label = {
                Text("Calories per 100g")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )


        // Food weight

        OutlinedTextField(
            value = uiState.weightInput,
            onValueChange = { newValue ->

                if (
                    newValue.all {
                        it.isDigit() || it == '.'
                    }
                ) {

                    calorieViewModel.updateWeight(
                        newValue
                    )
                }
            },
            label = {
                Text("Food weight (g)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )


        // Result card

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Estimated calories",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = calorieText,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}


@Composable
fun SettingsScreen(
    calorieViewModel: CalorieViewModel
) {

    val uiState by calorieViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Show decimal calories"
            )

            Switch(
                checked = uiState.showDecimals,
                onCheckedChange = { checked ->

                    calorieViewModel.updateShowDecimals(
                        checked
                    )
                }
            )
        }
    }
}