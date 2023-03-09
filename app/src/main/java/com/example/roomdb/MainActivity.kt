package com.example.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var appDb : AppDatabase // reference variable for our database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        appDb = AppDatabase.getDatabase(this)  // initialise our reference variable

        binding.btnWriteData.setOnClickListener {
            writeData()
        }

        binding.btnReadData.setOnClickListener {
            readData()
        }

        binding.btnDeleteAll.setOnClickListener {

            GlobalScope.launch {

                appDb.studentDao().deleteAll()

            }

        }

        binding.btnUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun writeData(){

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty())
        {
            val student = Student(null, firstName, lastName, rollNo.toInt() )

            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().insert(student)
            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this@MainActivity,"Successfully written",Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()

    }

    private fun readData(){

        val rollNo = binding.etRollNoRead.text.toString()

        if (rollNo.isNotEmpty()){

            lateinit var student : Student

            GlobalScope.launch {

                student = appDb.studentDao().findByRoll(rollNo.toInt())
                Log.d("Robin Data", student.toString())
                displayData(student)

            }

        }
        else
            Toast.makeText(this@MainActivity,"Please enter the data", Toast.LENGTH_SHORT).show()

    }

    private suspend fun displayData(student: Student){

        withContext(Dispatchers.Main){

            binding.tvFirstName.text = student.firstName
            binding.tvLastName.text = student.lastName
            binding.tvRollNo.text = student.rollNo.toString()

        }

    }

    private fun updateData(){
        // get all the 3 values entered by the user
        // similar to the writeData method with few changes

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty())
        {

            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().update(firstName,lastName, rollNo.toInt())
            }

            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this@MainActivity,"Successfully Updated",Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()

    }

}