package kr.musekee.faremeter.libs

import android.content.ContentValues
import android.util.Log

class DrivingDataDao(private val dbHelper: DatabaseHelper) {
    fun addData(data: DrivingData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("ID", data.id)
            put("LATITUDE", data.latitude)
            put("LONGITUDE", data.longitude)
            put("SPEED", data.speed)
            put("TIME", data.time)
        }

        return db.insert("DrivingData", null, cv)
    }

    fun getData(id: String): List<DrivingData> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM DrivingData WHERE ID = ? ORDER BY TIME ASC", arrayOf(id))

        val data = mutableListOf<DrivingData>()
        cursor.use {
            if (it.moveToFirst())
                do {
                    data += DrivingData(
                        id = it.getString(it.getColumnIndexOrThrow("ID")),
                        latitude = it.getDouble(it.getColumnIndexOrThrow("LATITUDE")),
                        longitude = it.getDouble(it.getColumnIndexOrThrow("LONGITUDE")),
                        speed = it.getDouble(it.getColumnIndexOrThrow("SPEED")),
                        time = it.getLong(it.getColumnIndexOrThrow("TIME"))
                    )
                } while (it.moveToNext())
            it.close()
        }
        return data
    }

    fun getTopSpeed(id: String): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT SPEED FROM DrivingData WHERE ID = ? ORDER BY SPEED DESC", arrayOf(id))
        cursor.moveToFirst()
        val speed = cursor.getDouble(cursor.getColumnIndexOrThrow("SPEED"))
        cursor.close()
        return speed
    }

    fun getAverageSpeed(id: String): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("""
            SELECT AVG(speed) AS AVG_SPEED
            FROM (
                SELECT 
                    s1.SPEED,
                    s1.TIME,
                    (SELECT s2.TIME 
                     FROM DrivingData s2 
                     WHERE s2.TIME < s1.TIME 
                     AND s2.ID = s1.ID
                     ORDER BY s2.TIME DESC
                     LIMIT 1) AS PREV_TIME
                FROM DrivingData s1
                WHERE s1.ID = ?
            )
            WHERE (TIME - IFNULL(PREV_TIME, TIME)) < 5000;
        """.trimIndent(), arrayOf(id))
        cursor.moveToFirst()
        val speed = cursor.getDouble(cursor.getColumnIndexOrThrow("AVG_SPEED"))
        cursor.close()
        return speed
    }

    fun avgTEST(id: String) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("""
            SELECT s1.speed
            FROM DrivingData s1
            WHERE s1.id = ? 
            AND EXISTS (
                SELECT 1
                FROM DrivingData s2 
                WHERE s2.time < s1.time 
                AND s2.id = s1.id  
                AND (s1.time - s2.time) < 5000
            )
            ORDER BY s1.time;
        """.trimIndent(), arrayOf(id))
        if (cursor.moveToFirst()) {
            do {
                val speed = cursor.getDouble(cursor.getColumnIndexOrThrow("SPEED"))
                Log.d("SPD", speed.toString())
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}