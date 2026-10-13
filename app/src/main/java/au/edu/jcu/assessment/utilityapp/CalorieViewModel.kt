package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CalorieUiState(
    val caloriesPer100g: String = "",
    val weightInput: String = "",
    val estimatedCalories: Double = 0.0
)

class CalorieViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalorieUiState())
    val uiState: StateFlow<CalorieUiState> = _uiState.asStateFlow()

    fun updateCaloriesPer100g(value: String) {
        _uiState.value = _uiState.value.copy(
            caloriesPer100g = value
        )
        calculateCalories()
    }

    fun updateWeight(value: String) {
        _uiState.value = _uiState.value.copy(
            weightInput = value
        )
        calculateCalories()
    }

    private fun calculateCalories() {
        val calories = _uiState.value.caloriesPer100g.toDoubleOrNull()
        val weight = _uiState.value.weightInput.toDoubleOrNull()

        val result =
            if (calories != null && weight != null) {
                calories * weight / 100
            } else {
                0.0
            }

        _uiState.value = _uiState.value.copy(
            estimatedCalories = result
        )
    }
}