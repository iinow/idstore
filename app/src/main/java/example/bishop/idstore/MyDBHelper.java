package example.bishop.idstore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static example.bishop.idstore.workActivity.KEY_H_Address;
import static example.bishop.idstore.workActivity.KEY_H_ID;
import static example.bishop.idstore.workActivity.KEY_H_Memo;
import static example.bishop.idstore.workActivity.KEY_H_Password;
import static example.bishop.idstore.workActivity.KEY_NUM;
import static example.bishop.idstore.workActivity.TABLE_NAME;

/**
 * Created by BISHOP on 2016-10-04.
 */


class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "Mydata.db", null, 5);
    }

    public void onCreate(SQLiteDatabase db) {
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String query = String.format("CREATE TABLE %s ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "%s TEXT, "//주소
                + "%s TEXT, "//아이디
                + "%s TEXT, "//넘버
                + "%s TEXT, "//패스워드 아래는 메모임
                + "%s TEXT);", TABLE_NAME, KEY_H_Address, KEY_H_ID, KEY_NUM, KEY_H_Password, KEY_H_Memo);
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }
}

