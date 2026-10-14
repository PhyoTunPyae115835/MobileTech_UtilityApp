package au.edu.jcu.assessment.utilityapp

data class FoodSearchResponse(
    val foods: List<FoodItem>
)

data class FoodItem(
    val fdcId: Int,
    val description: String,
    val foodNutrients: List<FoodNutrient>
)

data class FoodNutrient(
    val nutrientName: String?,
    val value: Double?,
    val unitName: String?
)