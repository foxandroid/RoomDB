package com.example.roomdb


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao() : StudentDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null   //create a reference variable to the appDatabase class

        /* Whenever user calls the below method a new database will be created. If its already called
           then only single instance will be created at any given point of time in the whole application.
         */
        fun getDatabase(context: Context): AppDatabase{

            val currentInstance = INSTANCE
            if(currentInstance != null){
                return currentInstance
            }

            /* First time Appdatabase is created and we don't have the current instance of that, so
               create a data instance of that database.
               Synchronized means if there are 3 operations then all of them would be performed one after
               the other not at single point of time.*/

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }

}