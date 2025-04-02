package com.example.hey_mom

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(private val context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null , DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "User"
        private const val COLUMN_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        private const val  COLUMN_PASSWORD= "password"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USERNAME TEXT NOT NULL," +
                "$COLUMN_PASSWORD TEXT NOT NULL)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply{
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return try {
            val result = db.insert(TABLE_NAME, null, values)
            db.close() // Close database to prevent memory leaks
            result
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error inserting user: ${e.message}")
            -1L
        }
    }
    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null)

        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    fun checkUsers() { // for debugging
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                Log.d("DatabaseHelper", "User found -> Username: $username, Password: $password")
            }
        } else {
            Log.e("DatabaseHelper", "No users found in database!")
        }
        cursor.close()
    }
}