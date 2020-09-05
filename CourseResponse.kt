package com.example.helllo1

import com.google.gson.annotations.SerializedName

data class CourseResponse(
    @SerializedName("courses") var courses: List<Course>
)