package au.edu.jcu.assessment.utilityapp

class CalorieRepository {

    fun calculateCalories(
        caloriesPer100g: Double,
        weight: Double
    ): Double {
        return caloriesPer100g * weight / 100
    }
}