package kr.musekee.faremeter.libs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Locale


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createRecordTable = """
            create table Records (
                _ID integer primary key autoincrement,
                FARE_CALC_TYPE TEXT,
                TRANSPORTATION TEXT,
                START_TIME TEXT,
                END_TIME TEXT,
                FARE INTEGER,
                AVERAGE_SPEED REAL,
                TOP_SPEED REAL,
                DISTANCE REAL,
                LATITUDES TEXT,
                LONGITUDES TEXT,
                NO_GPS_TIMES TEXT,
                NO_GPS_LATITUDES TEXT,
                NO_GPS_LONGITUDES TEXT
            );
        """.trimIndent()
        sqLiteDatabase.execSQL(createRecordTable)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVer: Int, newVer: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Records")
        onCreate(sqLiteDatabase)
    }

    companion object {
        private const val DATABASE_NAME = "data.db"
        private const val DATABASE_VERSION = 1
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }
}