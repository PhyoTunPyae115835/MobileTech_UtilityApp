package au.edu.jcu.assessment.utilityapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException
import kotlinx.coroutines.CancellationException

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
    private val repository: CalorieRepository
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
        val query = _uiState.value.searchQuery.trim()

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a food name"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                searchResults = emptyList()
            )

            try {
                val foods = repository.searchFoods(query)

                _uiState.value = _uiState.value.copy(
                    searchResults = foods,
                    isLoading = false,
                    errorMessage = if (foods.isEmpty()) {
                        "No food results found"
                    } else {
                        null
                    }
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: HttpException) {
                val message = when (exception.code()) {
                    429 -> "API request limit reached. Please try again later."
                    401, 403 -> "API access denied. Please check the API key."
                    in 500..599 -> "USDA server error. Please try again later."
                    else -> "Food search failed (HTTP ${exception.code()})."
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message,
                    searchResults = emptyList()
                )

            } catch (exception: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error. Please check your connection.",
                    searchResults = emptyList()
                )

            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to load food data. Please try again.",
                    searchResults = emptyList()
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