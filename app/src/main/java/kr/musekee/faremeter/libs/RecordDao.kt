package kr.musekee.faremeter.libs

import android.content.ContentValues
import kr.musekee.faremeter.libs.DatabaseHelper.Companion.dateFormat
import java.util.Date

class RecordDao(private val dbHelper: DatabaseHelper) {
    fun saveData(data: RecordData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("FARE_CALC_TYPE", data.fareCalcType)
            put("TRANSPORTATION", data.transportation)
            put("START_TIME", dateFormat.format(data.startTime))
            put("END_TIME", dateFormat.format(data.endTime))
            put("FARE", data.fare)
            put("AVERAGE_SPEED", data.averageSpeed)
            put("TOP_SPEED", data.topSpeed)
            put("DISTANCE", data.distance)
        }

        return db.insert("Records", null, cv)
    }

    fun deleteData(id: Int) {
        val db = dbHelper.readableDatabase
        db.delete("Records", "_ID = $id", null)
        db.close()
    }

    fun getAllData(transportation: String?, limit: Int?): MutableList<RecordData> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from Records", null)
        val data = mutableListOf<RecordData>()
        cursor.use {
            if (it.moveToFirst())
                do {
                    data += RecordData(
                        _id = it.getInt(it.getColumnIndexOrThrow("_ID")),
                        fareCalcType = it.getString(it.getColumnIndexOrThrow("FARE_CALC_TYPE")),
                        transportation = it.getString(it.getColumnIndexOrThrow("TRANSPORTATION")),
                        startTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("START_TIME"))) ?: Date(System.currentTimeMillis()),
                        endTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("END_TIME"))) ?: Date(System.currentTimeMillis()),
                        fare = it.getInt(it.getColumnIndexOrThrow("FARE")),
                        averageSpeed = it.getFloat(it.getColumnIndexOrThrow("AVERAGE_SPEED")),
                        topSpeed = it.getFloat(it.getColumnIndexOrThrow("TOP_SPEED")),
                        distance = it.getDouble(it.getColumnIndexOrThrow("DISTANCE")),
                    )
                }
                while (it.moveToNext())
            it.close()
        }
        return data
    }
}