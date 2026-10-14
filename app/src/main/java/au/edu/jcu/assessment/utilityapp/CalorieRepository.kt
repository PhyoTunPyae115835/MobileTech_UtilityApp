package au.edu.jcu.assessment.utilityapp

class CalorieRepository(
    private val api: FoodApiService = RetrofitClient.foodApi
) {

    fun calculateCalories(
        caloriesPer100g: Double,
        weight: Double
    ): Double {
        return caloriesPer100g * weight / 100
    }

    suspend fun searchFoods(query: String): List<FoodItem> {
        val response = api.searchFoods(
            apiKey = "DEMO_KEY",
            query = query
        )

        return response.foods
    }
}