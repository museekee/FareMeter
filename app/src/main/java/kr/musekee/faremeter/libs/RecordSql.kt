package kr.musekee.faremeter.libs

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RecordSql(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL("""
            create table Results (
                _ID integer primary key autoincrement,
                TYPE TEXT,
                START_TIME TEXT,
                END_TIME TEXT,
                FARE INTEGER,
                AVERAGE_SPEED REAL,
                TOP_SPEED REAL,
                DISTANCE REAL
            );
        """.trimIndent())
        sqLiteDatabase.execSQL("INSERT INTO Results VALUES (1, 'TAXI', '2024-08-03 23:00:00', '2024-08-04 00:01:00', 54166, 32.2, 70.3, 102.4)")
        sqLiteDatabase.execSQL("INSERT INTO Results VALUES (2, 'TAXI', '2024-08-07 04:00:00', '2024-08-07 05:00:00', 35000, 50.2, 104.3, 62.1)")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
    }

    fun saveData(data: RecordData) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("TYPE", data.type)
        cv.put("START_TIME", dateFormat.format(data.startTime))
        cv.put("END_TIME", dateFormat.format(data.endTime))
        cv.put("FARE", data.fare)
        cv.put("AVERAGE_SPEED", data.averageSpeed)
        cv.put("TOP_SPEED", data.topSpeed)
        cv.put("DISTANCE", data.distance)

        db.insert("Results", null, cv)
        db.close()
    }

    fun getAllData(type: String?, limit: Int?): MutableList<RecordData> {
        val db = this.writableDatabase
        val res = db.rawQuery("select * from Results", null)
        val data = mutableListOf<RecordData>()
        res.use {
            if (it.moveToFirst())
                do {
                    data += RecordData(
                        _id = it.getInt(it.getColumnIndexOrThrow("_ID")),
                        type = it.getString(it.getColumnIndexOrThrow("TYPE")),
                        startTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("START_TIME"))) ?: Date(System.currentTimeMillis()),
                        endTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("END_TIME"))) ?: Date(System.currentTimeMillis()),
                        fare = it.getInt(it.getColumnIndexOrThrow("FARE")),
                        averageSpeed = it.getFloat(it.getColumnIndexOrThrow("AVERAGE_SPEED")),
                        topSpeed = it.getFloat(it.getColumnIndexOrThrow("TOP_SPEED")),
                        distance = it.getDouble(it.getColumnIndexOrThrow("DISTANCE")),
                    )
                }
                while (it.moveToNext())
        }
        db.close()
        return data
    }
    companion object {
        private const val DATABASE_NAME = "data.db"
        private const val DATABASE_VERSION = 1
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }
}

data class RecordData(
    val _id: Int,
    val type: String,
    val startTime: Date,
    val endTime: Date,
    val fare: Int,
    val averageSpeed: Float,
    val topSpeed: Float,
    val distance: Double
)