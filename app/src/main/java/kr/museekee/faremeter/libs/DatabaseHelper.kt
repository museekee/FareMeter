package kr.museekee.faremeter.libs

import android.content.Context
import android.database.Cursor
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
                DISTANCE REAL,
                MEMO TEXT
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

        fun exportAsSQL(tableName: String, cursor: Cursor): String {
            val stringBuilder = StringBuilder()
            cursor.use {
                if (it.moveToFirst())
                    do {
                        val colListPart = StringBuilder("INSERT INTO $tableName (")
                        val valListPart = StringBuilder("VALUES (")

                        for (i in 0 until it.columnCount) {
                            colListPart.append(it.getColumnName(i)).append(", ")

                            when (it.getType(i)) {
                                Cursor.FIELD_TYPE_STRING -> {
                                    valListPart.append("'").append(it.getString(i).replace("'", "''")).append("', ")
                                }
                                Cursor.FIELD_TYPE_INTEGER -> {
                                    valListPart.append(cursor.getLong(i)).append(", ")
                                }
                                Cursor.FIELD_TYPE_FLOAT -> {
                                    valListPart.append(cursor.getDouble(i)).append(", ")
                                }
                                Cursor.FIELD_TYPE_NULL -> {
                                    valListPart.append("NULL, ")
                                }
                                Cursor.FIELD_TYPE_BLOB -> {
                                    continue
                                }
                            }
                        }

                        colListPart.setLength(colListPart.length - 2)
                        valListPart.setLength(valListPart.length - 2)

                        colListPart.append(") ").append(valListPart.append(");").toString())
                        stringBuilder.append(colListPart).append("\n")
                    } while (it.moveToNext())
                it.close()
            }
            return stringBuilder.toString()
        }
    }
}