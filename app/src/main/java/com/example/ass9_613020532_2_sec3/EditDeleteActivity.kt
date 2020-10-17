package com.example.ass9_613020532_2_sec3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_delete.*
import retrofit2.Call
import retrofit2.Response

class EditDeleteActivity : AppCompatActivity() {
    val createClient = StudentAPI.create()
    var mID : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete)

        mID = intent.getStringExtra("mId").toString()
        val mName = intent.getStringExtra("mName")
        val mAge = intent.getStringExtra("mAge")

        edt_id.setText(mID)
        edt_id.isEnabled = false
        edt_name.setText(mName)
        edt_age.setText(mAge)
    }

    fun saveStudent(view: View) {
        createClient.updateStudent(
            edt_id.text.toString(),
            edt_name.text.toString(),
            edt_age.text.toString().toInt()
        )
            .enqueue(object : retrofit2.Callback<Student>{
                override  fun onResponse(call: Call<Student>, response: Response<Student>){
                    if (response.isSuccessful){
                        Toast.makeText(applicationContext,"Seccessfully Updated", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Update Failure", Toast.LENGTH_LONG).show()
                    }
                }
                override  fun onFailure(call: Call<Student>, t: Throwable) = t.printStackTrace()
            })
    }
    fun deleteStudent(view: View) {
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.apply {
            setTitle("Warning Message")
            setMessage("Do you want to delete the student?")
            setNegativeButton("Yes"){ dialog,which ->
                createClient.deleteStudent(mID)
                    .enqueue(object : retrofit2.Callback<Student>{
                        override fun onResponse(call: Call<Student>, response: Response<Student>) {
                            if(response.isSuccessful){
                                Toast.makeText(applicationContext,"Successfully Deleted", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Student>, t: Throwable) {
                            Toast.makeText(applicationContext,t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                finish()
            }
            setPositiveButton("No"){dialog,which->dialog.cancel()}
            show()
        }
    }
}