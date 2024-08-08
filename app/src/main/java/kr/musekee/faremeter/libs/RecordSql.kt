package kr.musekee.faremeter.libs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RecordSql(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // array로 save하고 load할때 array를 읽어서 Data class를 return하자
    // 차후 preferancedatatable인가로 전환해서 sql 문법 써야지
    // 그냥 sqlite로 하자...
    private var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        // SQL 컬럼 작명 찾아보자
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
        // SELECT datetime('now', 'localtime')
//        sqLiteDatabase.execSQL("INSERT INTO Members VALUES (1,'Kim',20);")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
    }

    // type optional로 받기 => Data class를 return하자
    fun getAllData(type: String?, limit: Int?): MutableList<RecordData> {
        val db = this.writableDatabase
        val res = db.rawQuery("select * from Results", null)
        val data = mutableListOf<RecordData>()
        res.use {
            it.moveToFirst()
            data += RecordData(
                _id = it.getInt(it.getColumnIndexOrThrow("_ID")),
                type = it.getString(it.getColumnIndexOrThrow("TYPE")),
                startTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("START_TIME"))) ?: Date(System.currentTimeMillis()),
                endTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("END_TIME"))) ?: Date(System.currentTimeMillis()),
                fare = it.getInt(it.getColumnIndexOrThrow("FARE")),
                averageSpeed = it.getFloat(it.getColumnIndexOrThrow("AVERAGE_SPEED")),
                topSpeed = it.getFloat(it.getColumnIndexOrThrow("TOP_SPEED")),
                distance = it.getFloat(it.getColumnIndexOrThrow("DISTANCE")),
            )
        }
        return data
    }
    companion object {
        private const val DATABASE_NAME = "data.db"
        private const val DATABASE_VERSION = 1
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
    val distance: Float
)