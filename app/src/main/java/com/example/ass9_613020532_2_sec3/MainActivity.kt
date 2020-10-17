package com.example.ass9_613020532_2_sec3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var studentList = arrayListOf<Student>()
    val createClient = StudentAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.addItemDecoration(
            DividerItemDecoration(
                recycler_view.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onResume() {
        super.onResume()
        callStudentdata()
    }

    fun callStudentdata() {
        studentList.clear()
        val serv: StudentAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StudentAPI::class.java)

        serv.retrieveStudent()
            .enqueue(object : Callback<List<Student>> {
                override fun onResponse(
                    call: Call<List<Student>>,
                    response: Response<List<Student>>
                ) {
                    response.body()?.forEach {
                        studentList.add(Student(it.std_id, it.std_name, it.std_age))
                    }

                    recycler_view.adapter = EditStudentsAdapter(studentList, applicationContext)
                }

                override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                    return t.printStackTrace()
                    Toast.makeText(applicationContext, "Error2", Toast.LENGTH_LONG).show()
                }
            })
    }

    fun clickSearch(v: View) {
        studentList.clear()
        if (edt_search.text.isEmpty()) {
            callStudentdata()
        } else {
            createClient.retrieveStudentID(edt_search.text.toString())
                .enqueue(object : retrofit2.Callback<Student> {
                    override fun onResponse(
                        call: retrofit2.Call<Student>,
                        response: Response<Student>
                    ) {
                        if (response.isSuccessful) {
                            studentList.add(
                                Student(
                                    response.body()?.std_id.toString(),
                                    response.body()?.std_name.toString(),
                                    response.body()?.std_age.toString().toInt()
                                )
                            )
                            recycler_view.adapter =
                                EditStudentsAdapter(studentList, applicationContext)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<Student>, t: Throwable) =
                        t.printStackTrace()
                })
        }
    }
}