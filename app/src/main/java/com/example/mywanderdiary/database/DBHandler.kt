package com.example.mywanderdiary.database
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler (context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME , null, DATABASE_VERSION){

        // All constant variables needed for the database; Do not modify
        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "MyWanderDiaryDB"
            const val LOCATION_TABLE = "locations_table"

            const val LOCATION_ID = "location_id"
            const val LOCATION_NAME = "location_name"
            const val LOCATION_TYPE = "location_type"
            const val LOCATION_LAT = "location_lat"
            const val LOCATION_LON = "location_lon"

            const val ENTRY_TABLE = "entry_table"

            const val ENTRY_ID = "entry_id"
            const val ENTRY_NAME = "entry_name"
            const val ENTRY_DATE = "entry_date"
            const val ENTRY_CONTENT = "entry_content"
            const val COUNTRY_NAME = "country_name"
            const val IMAGE_ID = "image_id"
        }

        // Handles creation of the database
        override fun onCreate(db: SQLiteDatabase?) {
            val CREATE_MEDIA_TABLE =
                "CREATE TABLE IF NOT EXISTS " + LOCATION_TABLE + " (" +
                        LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LOCATION_NAME + " TEXT, " +
                        LOCATION_TYPE + " TEXT, " +
                        LOCATION_LAT + " DOUBLE, " +
                        LOCATION_LON + " DOUBLE) "

            val CREATE_ENTRY_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ENTRY_TABLE + " (" +
                        ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ENTRY_NAME + " TEXT, " +
                        ENTRY_DATE + " TEXT, " +
                        ENTRY_CONTENT + " TEXT, " +
                        COUNTRY_NAME + " TEXT, " +
                        LOCATION_ID + " INTEGER, " +
                        IMAGE_ID + " TEXT) "

            db?.execSQL(CREATE_MEDIA_TABLE)
            db?.execSQL(CREATE_ENTRY_TABLE)

        }

        /*  Do not modify this method.
         *
         *  Handles the logic needed when updating the database.
         */
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $LOCATION_TABLE")
            db!!.execSQL("DROP TABLE IF EXISTS $ENTRY_TABLE")
            onCreate(db)
        }

        // Delete all tables
        // For debugging
        private fun deleteAllTables(db: SQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = OFF;") // Disable foreign key constraints temporarily
            val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
            cursor.use {
                while (it.moveToNext()) {
                    val tableName = it.getString(0)
                    if (tableName != "sqlite_sequence") { // Skip internal SQLite table
                        db.execSQL("DROP TABLE IF EXISTS $tableName")
                    }
                }
            }
            db.execSQL("PRAGMA foreign_keys = ON;") // Re-enable foreign key constraints
        }
        // Clear all data at startup
        // For debugging
        fun clearDatabase() {
            writableDatabase.use { db ->
                deleteAllTables(db)
                onCreate(db)
            }
        }
    }