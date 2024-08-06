package kr.musekee.faremeter.libs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable


class SQLiteHelper(context: Context?) :
    // array로 save하고 load할때 array를 읽어서 Data class를 return하자
    // 차후 preferancedatatable인가로 전환해서 sql 문법 써야지
    // 그냥 sqlite로 하자...
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Members")

        // SQL 컬럼 작명 찾아보자
        sqLiteDatabase.execSQL("""
            create table Results (
                mID integer primary key autoincrement,
                type TEXT,
                startTime TEXT,
                endTime TEXT,
                fare INTEGER
            );
        """.trimIndent())
        // SELECT datetime('now', 'localtime')
//        sqLiteDatabase.execSQL("INSERT INTO Members VALUES (1,'Kim',20);")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
    }

    // type optional로 받기 => Data class를 return하자
    val Results: ArrayList<String>
        get() {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT Name FROM Members", null)
            val result = ArrayList<String>()
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0))
            }
            cursor.close()
            return result
        }

    companion object {
        private const val DATABASE_NAME = ".db"
        private const val DATABASE_VERSION = 1
    }
}