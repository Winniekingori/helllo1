package com.example.helllo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

private var Any.adapter: CourseAdapter
    get() {}
    set() {}

class CoursesActivity : AppCompatActivity(), CourseItemClickListener {
    lateinit var database: HelloDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)
        database = Room.databaseBuilder(baseContext, HelloDatabase::class.java, "hello-db").build()
        fetchCourses()
    }

    fun fetchCourses() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN_KEY", "")

        val apiClient = ApiClient.buildService(ApiInterface::class.java)
        val coursesCall = apiClient.getCourses("Bearer " + accessToken)
        coursesCall.enqueue(object : Callback<CourseResponse> {
            override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                fetchCoursesFromDatabase()
            }

            override fun onResponse(call: Call<CourseResponse>, response: Response<CourseResponse>) {
                if (response.isSuccessful) {
                    var courseList = response.body()?.courses as List<Course>
                    Thread {
                        courseList.forEach { course ->
                            database.courseDao().insertCourse(course)
                        }
                    }.start()

                    displayCourses(courseList,)
                } else {
                    Toast.makeText(baseContext, response.errorBody().toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    fun fetchCoursesFromDatabase(){
        Thread{
            val courses = database.courseDao().getAllCourses()

            runOnUiThread {
                displayCourses(courses,)
            }
        }.start()
    }

    fun displayCourses(
        courses: List<Course>,
        rvCourse: Any
    ){
        var coursesAdapter = CourseAdapter(courses, this)
        rvCourse.layoutManager = LinearLayoutManager(baseContext)
        rvCourse.adapter = coursesAdapter
    }

    override fun onItemClick(course: Course) {
        //obtain student id from shared preferences
        //courseId = course.courseId
        //make a post request https://github.com/owuor91/registration-api
    }
}