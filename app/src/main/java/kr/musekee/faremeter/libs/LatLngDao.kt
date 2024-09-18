package kr.musekee.faremeter.libs

import android.content.ContentValues

class LatLngDao(private val dbHelper: DatabaseHelper) {
    fun addData(data: LatLngData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("ID", data.id)
            put("LATITUDE", data.latitude)
            put("LONGITUDE", data.longitude)
            put("TIME", data.time)
        }

        return db.insert("LatLng", null, cv)
    }

    fun getData(id: String): List<LatLngData> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM LatLng WHERE ID = ? ORDER BY TIME ASC",
            listOf(id).toTypedArray()
        )

        val data = mutableListOf<LatLngData>()
        cursor.use {
            if (it.moveToFirst())
                do {
                    data += LatLngData(
                        id = it.getString(it.getColumnIndexOrThrow("ID")),
                        latitude = it.getDouble(it.getColumnIndexOrThrow("LATITUDE")),
                        longitude = it.getDouble(it.getColumnIndexOrThrow("LONGITUDE")),
                        time = it.getLong(it.getColumnIndexOrThrow("TIME"))
                    )
                } while (it.moveToNext())
            it.close()
        }
        return data
    }
}