package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class CalorieUiState(
    val caloriesPer100g: String = "",
    val weightInput: String = "",
    val estimatedCalories: Double = 0.0,
    val showDecimals: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<FoodItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CalorieViewModel(
    private val repository: CalorieRepository = CalorieRepository()
) : ViewModel() {

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

    fun updateShowDecimals(value: Boolean) {
        _uiState.value = _uiState.value.copy(
            showDecimals = value
        )
    }

    private fun calculateCalories() {
        val calories = _uiState.value.caloriesPer100g.toDoubleOrNull()
        val weight = _uiState.value.weightInput.toDoubleOrNull()

        val result =
            if (calories != null && weight != null) {
                repository.calculateCalories(
                    caloriesPer100g = calories,
                    weight = weight
                )
            } else {
                0.0
            }

        _uiState.value = _uiState.value.copy(
            estimatedCalories = result
        )
    }

    fun updateSearchQuery(value: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = value
        )
    }

    fun searchFoods() {
        val query = _uiState.value.searchQuery

        if (query.isBlank()) {
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val foods = repository.searchFoods(query)

                _uiState.value = _uiState.value.copy(
                    searchResults = foods,
                    isLoading = false
                )

            } catch (exception: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to load food data"
                )
            }
        }
    }

    fun selectFood(food: FoodItem) {
        val energy = food.foodNutrients.firstOrNull { nutrient ->
            nutrient.nutrientName?.contains(
                "Energy",
                ignoreCase = true
            ) == true &&
                    nutrient.unitName?.equals(
                        "KCAL",
                        ignoreCase = true
                    ) == true
        }

        val calories = energy?.value

        if (calories == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No calorie data available for this food"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            caloriesPer100g = calories.toString(),
            searchQuery = food.description,
            searchResults = emptyList(),
            errorMessage = null
        )

        calculateCalories()
    }
}