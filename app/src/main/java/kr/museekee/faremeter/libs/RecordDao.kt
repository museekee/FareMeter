package kr.museekee.faremeter.libs

import android.content.ContentValues

class RecordDao(private val dbHelper: DatabaseHelper) {
    fun saveData(data: RecordData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("ID", data.id)
            put("FARE_CALC_TYPE", data.fareCalcType)
            put("TRANSPORTATION", data.transportation)
            put("END_TIME", data.endTime)
            put("FARE", data.fare)
            put("DISTANCE", data.distance)
            put("MEMO", data.memo)
        }

        return db.insert("Records", null, cv)
    }

    fun updateMemo(id: String, memo: String) {
        val db = dbHelper.writableDatabase
        db.update("Record", ContentValues().apply {
            put("MEMO", memo)
        }, "ID = ?", arrayOf(id))
        db.close()
    }

    fun deleteData(id: String) {
        val db = dbHelper.readableDatabase
        db.delete("Records", "ID = ?", arrayOf(id))
        db.close()
    }

    inner class DataQueryBuilder {
        private var selection: String? = null
        private var selectionArgs: Array<String>? = null
        private var orderBy: String? = null
        private var limit: String? = null

        fun transportation(transportation: String?): DataQueryBuilder {
            if (transportation != null) {
                selection = "TRANSPORTATION = ?"
                selectionArgs = arrayOf(transportation)
            }
            return this
        }

        fun orderBy(column: String, direction: String = "ASC"): DataQueryBuilder {
            orderBy = "$column $direction"
            return this
        }

        fun limit(limit: Int): DataQueryBuilder {
            this.limit = limit.toString()
            if (limit == 0) this.limit = null
            return this
        }

        fun id(id: String): DataQueryBuilder {
            selection = "ID = ?"
            selectionArgs = arrayOf(id)
            return this
        }

        fun execute(): List<RecordData> {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                "Records",
                null, // columns (null means all columns)
                selection,
                selectionArgs,
                null, // groupBy
                null, // having
                orderBy,
                limit
            )

            val data = mutableListOf<RecordData>()
            cursor.use {
                if (it.moveToFirst())
                    do {
                        data += RecordData(
                            id = it.getString(it.getColumnIndexOrThrow("ID")),
                            fareCalcType = it.getString(it.getColumnIndexOrThrow("FARE_CALC_TYPE")),
                            transportation = it.getString(it.getColumnIndexOrThrow("TRANSPORTATION")),
                            endTime = it.getLong(it.getColumnIndexOrThrow("END_TIME")),
                            fare = it.getInt(it.getColumnIndexOrThrow("FARE")),
                            distance = it.getDouble(it.getColumnIndexOrThrow("DISTANCE")),
                            memo = it.getString(it.getColumnIndexOrThrow("MEMO"))
                        )
                    } while (it.moveToNext())
                it.close()
            }
            return data
        }
    }
    fun getAllData(): DataQueryBuilder {
        return DataQueryBuilder()
    }

    fun export(id: String): String {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Records WHERE id = ?", arrayOf(id))
        cursor.moveToFirst()
        val memo = cursor.getString(cursor.getColumnIndexOrThrow("MEMO"))
        val fare = cursor.getInt(cursor.getColumnIndexOrThrow("FARE"))
        val sql = DatabaseHelper.exportAsSQL("Records", cursor)
        cursor.close()
        return "$memo,$fare\n$sql"
    }
}