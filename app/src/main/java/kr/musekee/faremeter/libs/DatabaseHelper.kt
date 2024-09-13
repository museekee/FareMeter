package kr.musekee.faremeter.libs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createRecordTable = """
            create table Records (
                _ID integer primary key autoincrement,
                FARE_CALC_TYPE TEXT,
                TRANSPORTATION TEXT,
                END_TIME INTEGER,
                TIMES TEXT,
                FARE INTEGER,
                AVERAGE_SPEED REAL,
                TOP_SPEED REAL,
                DISTANCE REAL,
                LATITUDES TEXT,
                LONGITUDES TEXT
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
    }
}