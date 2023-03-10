package com.example.roomdb

import androidx.room.*

@Dao
interface StudentDao {
    // The functions are declared as suspend so that they can be called through coroutines.

    @Query("SELECT * FROM student_table")
    fun getAll(): List<Student>

    // If there are 5 students with the rollno as 101 the below query would fetch us only one of them.
    @Query("SELECT * FROM student_table WHERE roll_no =:roll LIMIT 1")
    suspend fun findByRoll(roll: Int): Student

    // If your table have duplicate items, use below query to avoid it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("DELETE FROM student_table")
    suspend fun deleteAll()

    @Query("UPDATE student_table SET first_name=:firstName, last_name=:lastName WHERE roll_no LIKE :roll")
    suspend fun update(firstName: String, lastName : String, roll: Int)

}