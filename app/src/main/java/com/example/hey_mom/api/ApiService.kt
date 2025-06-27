package com.example.hey_mom.api

import com.example.hey_mom.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /** Authentication **/
    @FormUrlEncoded
    @POST("user/login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("user/register.php")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") userType: String,
        @Field("contact_info") contactInfo: String?
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("user/update_profile.php")
    suspend fun updateProfile(
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("contact_info") contactInfo: String
    ): Response<ApiResponse>

    /** Child (Baby) Management **/
    @FormUrlEncoded
    @POST("baby/add_baby.php")
    suspend fun addBaby(
        @Field("name") name: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("weight_kg") weight: Float,
        @Field("height_cm") height: Float,
        @Field("blood_group") bloodGroup: String,
        @Field("known_allergies") knownAllergies: String,
        @Field("medical_conditions") medicalConditions: String,
        @Field("user_id") userId: Int,
        @Field("relation_type") relationType: String = "Mother"  // default if not provided
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("baby/get_babies.php")
    suspend fun getBabies(
        @Field("user_id") userId: Int
    ): Response<List<Baby>>

    @FormUrlEncoded
    @POST("baby/update_baby.php")
    suspend fun updateBaby(
        @Field("baby_id") babyId: Int,
        @Field("name") name: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("weight_kg") weight: Float,
        @Field("height_cm") height: Float,
        @Field("blood_group") bloodGroup: String,
        @Field("known_allergies") knownAllergies: String,
        @Field("medical_conditions") medicalConditions: String
    ): Response<ApiResponse>

    /** Growth Tracking **/
    @FormUrlEncoded
    @POST("growth/add_growth.php")
    suspend fun addGrowth(
        @Field("baby_id") babyId: Int,
        @Field("recorded_on") date: String,
        @Field("weight_kg") weight: Float,
        @Field("height_cm") height: Float,
        @Field("head_circumference_cm") headCircumference: Float,
        @Field("notes") notes: String
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("growth/get_growth.php")
    suspend fun getGrowth(
        @Field("baby_id") babyId: Int
    ): Response<List<GrowthEntry>>

    /** Feeding Routine **/
    @FormUrlEncoded
    @POST("feeding/add_feeding.php")
    suspend fun addFeedingEntry(
        @Field("baby_id") babyId: Int,
        @Field("feeding_time") time: String,
        @Field("feeding_type") type: String,
        @Field("quantity_ml") quantity: Int,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("feeding/get_feeding.php")
    suspend fun getFeedingEntries(
        @Field("baby_id") babyId: Int
    ): Response<List<FeedingEntry>>

    @FormUrlEncoded
    @POST("feeding/update_feeding.php")
    suspend fun updateFeedingEntry(
        @Field("feeding_id") feedingId: Int,
        @Field("feeding_time") time: String,
        @Field("feeding_type") type: String,
        @Field("quantity_ml") quantity: Int,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    /** Sleep Routine **/
    @FormUrlEncoded
    @POST("sleep/add_sleep.php")
    suspend fun addSleepEntry(
        @Field("baby_id") babyId: Int,
        @Field("sleep_start") start: String,
        @Field("sleep_end") end: String,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("sleep/get_sleep.php")
    suspend fun getSleepEntries(
        @Field("baby_id") babyId: Int
    ): Response<List<SleepEntry>>

    @FormUrlEncoded
    @POST("sleep/update_sleep.php")
    suspend fun updateSleepEntry(
        @Field("sleep_id") sleepId: Int,
        @Field("sleep_start") start: String,
        @Field("sleep_end") end: String,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    /** Diaper Change Routine **/
    @FormUrlEncoded
    @POST("diaper/add_diaper.php")
    suspend fun addDiaperEntry(
        @Field("baby_id") babyId: Int,
        @Field("change_time") time: String,
        @Field("condition") condition: String,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("diaper/get_diaper.php")
    suspend fun getDiaperEntries(
        @Field("baby_id") babyId: Int
    ): Response<List<DiaperChange>>

    @FormUrlEncoded
    @POST("diaper/update_diaper.php")
    suspend fun updateDiaperEntry(
        @Field("diaper_ch_id") diaperChId: Int,
        @Field("change_time") time: String,
        @Field("condition") condition: String,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    /** Vaccination Scheduling **/
    @FormUrlEncoded
    @POST("vaccine/get_all_vaccines.php")
    suspend fun getAllVaccines(): Response<List<Vaccine>>

    @FormUrlEncoded
    @POST("vaccine/assign_vaccine_to_baby.php")
    suspend fun assignVaccine(
        @Field("baby_id") babyId: Int,
        @Field("vaccine_id") vaccineId: Int
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("vaccine/assign_all_vaccines_to_baby.php")
    suspend fun assignAllVaccinesToBaby(
        @Field("baby_id") babyId: Int
    ): Response<ApiResponse>

    @FormUrlEncoded
    @POST("vaccine/get_baby_vaccines.php")
    suspend fun getBabyVaccines(
        @Field("baby_id") babyId: Int
    ): Response<List<BabyVaccineStatus>>

    @FormUrlEncoded
    @POST("vaccine/update_vaccine_status.php")
    suspend fun updateVaccineStatus(
        @Field("baby_id") babyId: Int,
        @Field("vaccine_id") vaccineId: Int,
        @Field("status") status: String,
        @Field("administered_on") administeredOn: String,
        @Field("notes") notes: String?
    ): Response<ApiResponse>

    @GET("location/get_all_locations.php")
    suspend fun getAllLocations(): Response<List<LocationService>>
}

