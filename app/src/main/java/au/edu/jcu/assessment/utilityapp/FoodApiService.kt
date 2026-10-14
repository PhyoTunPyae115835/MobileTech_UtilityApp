package au.edu.jcu.assessment.utilityapp

import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {

    @GET("foods/search")
    suspend fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): FoodSearchResponse
}