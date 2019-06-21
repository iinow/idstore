package example.bishop.idstore;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static example.bishop.idstore.workActivity.KEY_H_Address;
import static example.bishop.idstore.workActivity.KEY_H_ID;
import static example.bishop.idstore.workActivity.KEY_H_Memo;

/**
 * Created by BISHOP on 2016-10-04.
 */

class MyCursorAdapter extends CursorAdapter
{//여기는 다 복사 완료 시킴

    @SuppressWarnings("deprecation")
    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }//생성자

    @Override
    public void bindView(View view, Context context, Cursor cursor) {//대입할 데이터
        TextView tvName = (TextView) view.findViewById( R.id.tv_name );//이름, 나이 뷰 찾기
        TextView tvAge = (TextView) view.findViewById( R.id.tv_age );
        TextView tvMemo=(TextView) view.findViewById(R.id.tv_memo);

        String name = cursor.getString( cursor.getColumnIndex( KEY_H_Address ) ); //이름 넣었음
        String age = cursor.getString( cursor.getColumnIndex( KEY_H_ID ) );  ///나이 넣었음
        String memo=cursor.getString( cursor.getColumnIndex( KEY_H_Memo));

        Log.d("스트링 확인", name + ", " + age);//디버깅할때 보이는거임 별 의미 없음

        tvName.setText( name ); //list_item 레이아웃에 데이터 설정
        tvAge.setText( age );
        tvMemo.setText( memo);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from( context );
        View v = inflater.inflate( R.layout.list_item, parent, false );//true 시 parent 참조  false 시 R.layout.List_item 참조
        return v;
    }

}