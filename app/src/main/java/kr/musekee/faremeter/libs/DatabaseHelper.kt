package kr.musekee.faremeter.libs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sqlRecords = """
            create table Records (
                ID TEXT primary key,
                FARE_CALC_TYPE TEXT,
                TRANSPORTATION TEXT,
                END_TIME INTEGER,
                FARE INTEGER,
                DISTANCE REAL
            );
        """.trimIndent()
        sqLiteDatabase.execSQL(sqlRecords)
        val sqlLatLng = """
            create table DrivingData (
                ID TEXT,
                LATITUDE REAL,
                LONGITUDE REAL,
                SPEED REAL,
                TIME INTEGER primary key
            );
        """.trimIndent()
        sqLiteDatabase.execSQL(sqlLatLng)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVer: Int, newVer: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Records")
        onCreate(sqLiteDatabase)
    }

    companion object {
        private const val DATABASE_NAME = "data.db"
        private const val DATABASE_VERSION = 1
    }
}