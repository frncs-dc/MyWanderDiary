package com.example.mywanderdiary.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.mywanderdiary.Entry
import com.example.mywanderdiary.Location
import com.example.mywanderdiary.LocationType

class Database(context: Context) {
    // A private instance of the DB helper
    private lateinit var databaseHandler : DBHandler

    // Initializes the databaseHandler instance using the context provided.
    init {
        this.databaseHandler = DBHandler(context)
    }

    // A companion object to hold the static ArrayList of Location objects
    companion object {
        // This ArrayList will persist through the app
        var cachedLocations: ArrayList<Location> = ArrayList<Location>()
        var cachedEntry: ArrayList<Entry> = ArrayList<Entry>()
    }

    // Inserts a provided media item into the database. Returns the id provided by the DB.
    fun addLocation(location: Location) : Int {
        val db = databaseHandler.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DBHandler.LOCATION_NAME, location.LOCATION_NAME)
        contentValues.put(DBHandler.LOCATION_TYPE, location.LOCATION_TYPE.toString())
        contentValues.put(DBHandler.LOCATION_LAT, location.LOCATION_LAT)
        contentValues.put(DBHandler.LOCATION_LON, location.LOCATION_LON)

        val _id = db.insert(DBHandler.LOCATION_TABLE, null, contentValues)

        cachedLocations.add(location)

        db.close()

        return _id.toInt()
    }

    fun addEntry(entry: Entry) : Int{
        val db = databaseHandler.writableDatabase

        val values = ContentValues().apply {
            put(DBHandler.COVER_IMAGE_ID, entry.coverImageId)
            put(DBHandler.ENTRY_DATE, entry.formatDate(entry.date)) // Save as string
            put(DBHandler.ENTRY_CONTENT, entry.entryContent)
            put(DBHandler.COUNTRY_NAME, entry.countryName)
            put(DBHandler.IMAGE_ID, entry.imageId)
        }

        val _id = db.insert(DBHandler.ENTRY_TABLE, null, values)

        cachedEntry.add(entry)

        db.close()

        return _id.toInt()

    }

    /*  TODO #1
     *      Supply the logic to complete the method that updates a provided media item's variables.
     *      Do not modify any of the provided lines of code -- only add to the method.
     */
    fun updateLocation(location: Location) {
        val database = databaseHandler.writableDatabase

        val contentValues = ContentValues().apply {
            put(DBHandler.LOCATION_NAME, location.LOCATION_NAME)
            put(DBHandler.LOCATION_TYPE, location.LOCATION_TYPE.toString())
            put(DBHandler.LOCATION_LAT, location.LOCATION_LAT)
            put(DBHandler.LOCATION_LON, location.LOCATION_LON)
        }

        // Which row to update, based on the ID
        val selection = "${DBHandler.LOCATION_ID} = ?"
        val selectionArgs = arrayOf(location.LOCATION_ID.toString())
        val count = database.update(
            DBHandler.LOCATION_TABLE,
            contentValues,
            selection,
            selectionArgs
        )

        cachedLocations[location.LOCATION_ID] = location

        database.close()


    }

    /*  TODO #2
     *      Supply the logic to complete the method that deletes the provided media item from the
     *      DB. Do not modify any of the provided lines of code -- only add to the method.
     */
    fun deleteLocation(location: Location) {
        val database = databaseHandler.writableDatabase

        val selection = "${DBHandler.LOCATION_ID} = ?"

        val selectionArgs = arrayOf(location.LOCATION_ID.toString())
        Log.d("DELETE ITEM: ", location.LOCATION_ID.toString())
        database.delete(DBHandler.LOCATION_TABLE, selection, selectionArgs)
        database.close()


    }

    /*  TODO #3
     *      Supply the logic to complete the method that retrieves all media items from the DB and
     *      places them into an array list. The method also returns the created array list
     *      Do not modify any of the provided lines of code -- only add to the method.
     */
    fun getLocations(): ArrayList<Location>{
        val result = ArrayList<Location>()

        val database: SQLiteDatabase = databaseHandler.readableDatabase

        val c : Cursor = database.query(
            DBHandler.LOCATION_TABLE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while(c.moveToNext()) {
            result.add(Location(
                c.getInt(c.getColumnIndexOrThrow(DBHandler.LOCATION_ID)),
                c.getString(c.getColumnIndexOrThrow(DBHandler.LOCATION_NAME)),
                LocationType.valueOf(c.getString(c.getColumnIndexOrThrow(DBHandler.LOCATION_TYPE))),
                c.getDouble(c.getColumnIndexOrThrow(DBHandler.LOCATION_LAT)),
                c.getDouble(c.getColumnIndexOrThrow(DBHandler.LOCATION_LON)),
            ))
        }

        c.close()
        database.close()

        return result
    }

    // This method will initialize the cachedLocations list
    fun initializeCachedLocations() {
        // Lazy load the cachedLocations list if it hasn't been initialized
        cachedLocations.clear()
        cachedLocations = getLocations()
    }
}