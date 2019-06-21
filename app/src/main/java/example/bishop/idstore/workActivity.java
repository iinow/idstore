package example.bishop.idstore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static example.bishop.idstore.registerActivity.prefs;

public class workActivity extends AppCompatActivity {
    MyDBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Cursor cursor1;
    MyCursorAdapter myAdapter;
    int num0;//마찬가지로 넘버 저장용
    String Strnum0;//넘버 저장용인데...흠.
    ListView list;
    String string_sort;

    SharedPreferences preferences;

    String strI, strP;//서비스에서 화면이 보여지기 위해 프리페런스 변수 이름 설정

    final static String KEY_ID = "_id";
    final static String KEY_H_Address = "Address";
    final static String KEY_H_ID = "ID";
    final static String KEY_H_Password="Password";
    final static String KEY_H_Memo="Memo";
    final static String TABLE_NAME = "mytable";
    final static String KEY_NUM="num";          //숫자 지정할꺼임

    final static String querySelectAll = String.format("SELECT * FROM %s", TABLE_NAME);
    final static String queryNum=String.format("SELECT COUNT(*) FROM %s",TABLE_NAME);
    final static String querySort=String.format("SELECT * FROM %S ORDER BY %S ASC",TABLE_NAME,KEY_H_Address);

    final static String queryCounter=String.format("SELECT COUNT(*) FROM %s",TABLE_NAME);

    public static final String saveRoot= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/IDStore저장.xls";
    String dbAttribute[]={KEY_H_Address,KEY_H_ID,KEY_NUM,KEY_H_Password,KEY_H_Memo};                //엑셀 저장시 상위 속성 이름 지정 5개
    WritableWorkbook workbook=null;
    Workbook workbook_load=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_work_layout);

        /*final ListView*/ list = (ListView) findViewById(R.id.lv_name_age);

        preferences=getSharedPreferences("shared",MODE_PRIVATE);//register이라는 파일명으로 저장됨
        preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences=getPreferences(MODE_PRIVATE);//메서드로 파일 이름과 접근 권한을 지정

        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();//수정하기 위해 에디터를 엽니다
        editor.putString("hide","1");//hide 에 1이 저장됨
        // new MainActivity().hideButton();//으아 실행해버림!버튼 사라지는 메소드 일회성으로 사용함

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LinearLayout layout=new LinearLayout(workActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText etxAddress=new EditText(workActivity.this);
                etxAddress.setHint("홈페이지 주소를 입력하세요");
                final EditText etxID=new EditText(workActivity.this);
                etxID.setHint("아이디를 입력하세요");
                final EditText etxPw=new EditText(workActivity.this);
                etxPw.setHint("비밀번호를 입력하세요");
                final EditText etxMemo=new EditText(workActivity.this);
                etxMemo.setHint("메모");

                layout.addView(etxAddress);//------------다이얼에 editText 4개 추가
                layout.addView(etxID);
                layout.addView(etxPw);
                layout.addView(etxMemo);

                AlertDialog.Builder dialog=new AlertDialog.Builder(workActivity.this);
                dialog.setTitle("정보 입력")
                        .setView(layout)
                        .setPositiveButton("등록",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String Address=etxAddress.getText().toString();
                                String ID=etxID.getText().toString();
                                String Password=etxPw.getText().toString();
                                String Memo=etxMemo.getText().toString();

                                db=mHelper.getWritableDatabase();
                                cursor1=db.rawQuery(queryNum,null);
                                cursor1.moveToLast();
                                num0=cursor1.getInt(0);      //위에 데이터를 기반으로 +1을 해줌 이거라면 가능!
                                num0+=1;
                                Strnum0=String.valueOf(num0);//정수를 문자열로 바꿈

                                try {//데이터 삽입시 행하는 거임

                                    // 문자열은 ''로 감싸야 한다.
                                    String query = String.format(
                                            "INSERT INTO %s VALUES ( null, '%s', '%s', '%s','%s','%s' );", TABLE_NAME, Address,ID,Strnum0,Password,Memo);
                                    db.execSQL(query);  //데이터 삽입시

                                    // 아래 메서드를 실행하면 리스트가 갱신된다. 하지만 구글은 이 메서드를 deprecate한다. 고로 다른 방법으로 해보자.
                                    // cursor.requery();
                                    //cursor = db.rawQuery(querySelectAll, null);//화면에 입력한 데이터가 보여지게 됨
                                    //myAdapter.changeCursor(cursor);
                                    prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                                    string_sort=prefs.getString("clos","nothing");
                                    if (string_sort.equals("1")){//나머지가 1일 경우
                                        list_sort();
                                    }
                                    else {
                                        list_all();
                                    }
                                } catch (NumberFormatException e) {
                                    Toast.makeText(getApplicationContext(), "데이터를 입력하세요", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNeutralButton("취소",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
                /*AlertDialog dialog2=dialog.create();
                dialog2.show();
                dialog2.setCancelable(false);
                dialog2.dismiss();*/
            }
        });

        //------------------------------------------------------------------------------------------------리스트 클릭시 이벤트
        mHelper = new MyDBHelper(this);
        db = mHelper.getWritableDatabase();



        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
        string_sort=prefs.getString("clos","nothing");
        if (string_sort.equals("1")){//나머지가 1일 경우
            list_sort();
        }
        else {

            list_all();
        }
      //  cursor = db.rawQuery(querySelectAll, null);
        //myAdapter = new MyCursorAdapter(this, cursor);

       // list.setAdapter(myAdapter);         //초기화면 데이터가 리스트뷰에 뜨게끔 하는거임
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {//id가 힌트다 이것이 안드로이드다 책에서 p1044

                //  db=mHelper.getWritableDatabase();
                //    String name2= KEY_NAME.toString();
                //  String queryClick=String.format("SELECT %s FROM %s WHERE %s=%s",KEY_NAME,TABLE_NAME,KEY_ID,id);
                //  String args[]={"20"};
                //    cursor1=db.rawQuery(queryClick,args);
                //   String name=cursor1.getString(0);
                db=mHelper.getReadableDatabase();
                String query=String.format("SELECT * FROM %s WHERE %s='"+id+"'",TABLE_NAME,KEY_ID);
                cursor=db.rawQuery(query,null);
                cursor.moveToNext();
                final String Address0=cursor.getString(1);
                String ID0=cursor.getString(2);
                String abc=cursor.getString(3); //넘버링임
                String Pw0=cursor.getString(4);
                String Memo0=cursor.getString(5);
                cursor.moveToFirst();

                Toast.makeText(workActivity.this,"onItemClick Item :"+position+" "+id+" "+Address0+" "+ID0+" "+abc,Toast.LENGTH_LONG).show();

                LinearLayout layout=new LinearLayout(workActivity.this);
                LinearLayout layout2=new LinearLayout(workActivity.this);
                LinearLayout layout3=new LinearLayout(workActivity.this);
                LinearLayout layout4=new LinearLayout(workActivity.this);
                LinearLayout layout5=new LinearLayout(workActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);//첫번째 레이아웃
                layout2.setOrientation(LinearLayout.VERTICAL);//
                layout3.setOrientation(LinearLayout.VERTICAL);
                layout4.setOrientation(LinearLayout.VERTICAL);
                layout5.setOrientation(LinearLayout.VERTICAL);

                final EditText etAddress=new EditText(workActivity.this);
                final EditText etID=new EditText(workActivity.this);
                final EditText etPw=new EditText(workActivity.this);
                final EditText etMemo=new EditText(workActivity.this);

                TextView txvAddress=new TextView(workActivity.this);
                TextView txvID=new TextView(workActivity.this);
                TextView txvPw=new TextView(workActivity.this);
                TextView txvMemo=new TextView(workActivity.this);

                txvAddress.setText("홈페이지 주소");
                txvID.setText("아이디");
                txvPw.setText("비밀번호");
                txvMemo.setText("메모");

                layout2.addView(txvAddress);
                layout2.addView(etAddress);
                layout.addView(layout2);

                layout3.addView(txvID);
                layout3.addView(etID);
                layout.addView(layout3);

                layout4.addView(txvPw);
                layout4.addView(etPw);
                layout.addView(layout4);

                layout5.addView(txvMemo);
                layout5.addView(etMemo);
                layout.addView(layout5);

                etAddress.setMinWidth(300);
                etAddress.setText(Address0);
                etID.setMinWidth(300);
                etID.setText(ID0);
                etPw.setMinWidth(300);
                etPw.setText(Pw0);
                etMemo.setMinWidth(300);
                etMemo.setText(Memo0);

                AlertDialog.Builder dialog=new AlertDialog.Builder(workActivity.this);
                dialog.setTitle("정보수정")
                        .setView(layout)
                        .setPositiveButton("수정",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db=mHelper.getWritableDatabase();       //쓰기 db가져오기

                                String name0=etAddress.getText().toString();     //입력한 에디트 텍스트 를 스트링 타입의 변수에 저장
                                String id0=etID.getText().toString();
                                String password0=etPw.getText().toString();
                                String memo0=etMemo.getText().toString();

                                String queryUp=String.format("UPDATE %s SET %s='"+name0+"' WHERE %s='"+id+"'",TABLE_NAME,KEY_H_Address,KEY_ID);//데이터 업데이트 쿼리문
                                String queryUp0=String.format("UPDATE %s SET %s='"+id0+"' WHERE %s='"+id+"'",TABLE_NAME,KEY_H_ID,KEY_ID);
                                String queryUp1=String.format("UPDATE %s SET %s='"+password0+"' WHERE %s='"+id+"'",TABLE_NAME,KEY_H_Password,KEY_ID);
                                String queryUp2=String.format("UPDATE %s SET %s='"+memo0+"' WHERE %s='"+id+"'",TABLE_NAME,KEY_H_Memo,KEY_ID);
                                db.execSQL(queryUp);//쿼리문 실행
                                db.execSQL(queryUp0);
                                db.execSQL(queryUp1);
                                db.execSQL(queryUp2);

                                prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                                string_sort=prefs.getString("clos","nothing");
                                if (string_sort.equals("1")){//나머지가 1일 경우
                                    list_sort();
                                }
                                else {

                                    list_all();
                                }
                                //cursor = db.rawQuery(querySelectAll, null);//여기서 아래 3개 까지는 Mainactivity 의 리스트뷰 업데이트 자료 !!! 으앙 굳
                                //myAdapter = new MyCursorAdapter(workActivity.this, cursor);//여기서는 MainActivity.this 라고 해야지 맞음 시발!!!
                                //list.setAdapter(myAdapter);

                                //  cursor=db.rawQuery(querySelectAll,null);
                                //  myAdapter.changeCursor(cursor);

                                Toast.makeText(getApplicationContext(),"수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();

                            }
                        }).setNeutralButton("취소(삭제)",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//삭제 버튼?
                        db=mHelper.getWritableDatabase();
                        String queryDelete=String.format("DELETE FROM %s WHERE %s='"+id+"'",TABLE_NAME,KEY_ID);
                        db.execSQL(queryDelete);

                        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                        string_sort=prefs.getString("clos","nothing");
                        if (string_sort.equals("1")){//나머지가 1일 경우
                            list_sort();
                        }
                        else {

                            list_all();
                        }
                        //list_all();//리스트 보여줌


                    }
                }).setNegativeButton("바로가기",new DialogInterface.OnClickListener(){//------------------------------------------------------------------------------------------바로가기 버튼 이다다다다다다닫ㄷ다다
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//-----------------------------------------------------------------------------------------------------바로가기
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+Address0));

                        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                        strI=prefs.getString("strI","nothing");
                        SharedPreferences.Editor store_editor=prefs.edit();
                        store_editor.putString("strI",strI);
                        store_editor.commit();

                        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                        strP=prefs.getString("strP","nothing");
                        SharedPreferences.Editor store_editor1=prefs.edit();
                        store_editor1.putString("strP",strP);
                        store_editor1.commit();

                        stopService(new Intent(getApplicationContext(),AlwaysTopServiceTouch.class));
                        startService(new Intent(getApplicationContext(),AlwaysTopServiceTouch.class));
                        startActivity(myIntent);
                    }
                }).create().show();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"안녕 길게 눌렀음",Toast.LENGTH_SHORT).show();

/*

                for(int i=0; i<num0;i++)
                {
                    db.execSQL(querySelectAll);
                    cursor.moveToFirst();
                    String queryAlter=String.format("UPDATE %s SET %s='"+i+"'",TABLE_NAME,KEY_NUM);
                    db.execSQL(queryAlter);
                    if(i==num0-1){}
                    else{
                    cursor.moveToNext();}
                }

*/
                return false;
            }
        });

    }


/*
    public void mOnClick(View v) {
        EditText eName = (EditText) findViewById(R.id.et_name);
        EditText eAge = (EditText) findViewById(R.id.et_age);
        EditText ePass=(EditText) findViewById(R.id.et_pw);
        EditText eMemo=(EditText) findViewById(R.id.et_memo);
        String name = eName.getText().toString();
        String pass=ePass.getText().toString();
        String memo=eMemo.getText().toString();

        db=mHelper.getWritableDatabase();
        cursor1=db.rawQuery(queryNum,null);
        cursor1.moveToLast();
        num0=cursor1.getInt(0);      //위에 데이터를 기반으로 +1을 해줌 이거라면 가능!
        num0+=1;
        Strnum0=String.valueOf(num0);//정수를 문자열로 바꿈

        try {//데이터 삽입시 행하는 거임
            int age = Integer.parseInt(eAge.getText().toString());


            // 문자열은 ''로 감싸야 한다.
            String query = String.format(
                    "INSERT INTO %s VALUES ( null, '%s', %s, '%s','%s','%s' );", TABLE_NAME, name, age,Strnum0,pass,memo);
            db.execSQL(query);  //데이터 삽입시

            // 아래 메서드를 실행하면 리스트가 갱신된다. 하지만 구글은 이 메서드를 deprecate한다. 고로 다른 방법으로 해보자.
            // cursor.requery();
            cursor = db.rawQuery(querySelectAll, null);//화면에 입력한 데이터가 보여지게 됨
            myAdapter.changeCursor(cursor);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "나이는 정수를 입력해야 합니다", Toast.LENGTH_LONG).show();
        }

        eName.setText("");
        eAge.setText("");

        // 저장 버튼 누른 후 키보드 안보이게 하기
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(eAge.getWindowToken(), 0);
    }
 */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.action_settings://---------------------------------설정 액티비티로
                Intent intent1 = new Intent(getApplicationContext(), registerActivity.class);
                startActivity((intent1));
                return true;
            case R.id.action_bar://--------------------------------------도움말 액티비티로
                Intent intent2 = new Intent(getApplicationContext(), adviseActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menu_Sort://---------------------------------------정렬
                prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                string_sort=prefs.getString("clos","nothing");
                Toast.makeText(getApplicationContext(),string_sort,Toast.LENGTH_LONG).show();
                if (string_sort.equals("1")){//나머지가 1일 경우
                    String two="2";
                    list_all();
                    SharedPreferences.Editor sort_editor=prefs.edit();
                    sort_editor.putString("clos",two);
                    sort_editor.commit();
                 }
                else{
                    String one="1";
                    list_sort();
                    SharedPreferences.Editor sort_editor=prefs.edit();
                    sort_editor.putString("clos",one);
                    sort_editor.commit();
                }
                return true;
            case R.id.action_Save:
                try{
                    workbook= Workbook.createWorkbook(new File(saveRoot));//파일 저장
                    WritableSheet sheet=workbook.createSheet("Sheet",0);
                    jxl.write.WritableCellFormat format=new WritableCellFormat();
                    jxl.write.Label lbl=null;

                    for(int i=0;i<dbAttribute.length;i++){      //엑셀에 속성 작성
                        lbl=new jxl.write.Label(0+i,0,dbAttribute[i],format);
                        sheet.addCell(lbl);
                    }
                    db=mHelper.getReadableDatabase();
                    cursor=db.rawQuery(querySelectAll,null);
                    cursor.moveToNext();
                    cursor1=db.rawQuery(queryCounter,null);
                    String count;
                    cursor1.moveToNext();
                    count=cursor1.getString(0);
                    int a= Integer.valueOf(count);
                    jxl.write.Label lblPlot=null;
                    jxl.write.Label lblPlot1=null;
                    jxl.write.Label lblPlot2=null;
                    jxl.write.Label lblPlot3=null;
                    jxl.write.Label lblPlot4=null;
                 //   Strnum0=String.valueOf(num0);//정수를 문자열로 바꿈
                    for(int i=0;i<a;i++) {                      //--------------------DB 자료 입력하는 소스
                        int num0=cursor.getInt(0);
                        String Address0 = cursor.getString(1);
                        String ID0 = cursor.getString(2);
                        String abc = cursor.getString(3); //넘버링임
                        String Pw0 = cursor.getString(4);
                        String Memo0 = cursor.getString(5);

                        lblPlot=new jxl.write.Label(0,1+i, Address0,format);
                        lblPlot1=new jxl.write.Label(1,1+i, ID0,format);
                        lblPlot2=new jxl.write.Label(2,1+i, abc,format);
                        lblPlot3=new jxl.write.Label(3,1+i, Pw0,format);
                        lblPlot4=new jxl.write.Label(4,1+i, Memo0,format);
                        sheet.addCell(lblPlot);
                        sheet.addCell(lblPlot1);
                        sheet.addCell(lblPlot2);
                        sheet.addCell(lblPlot3);
                        sheet.addCell(lblPlot4);

                        cursor.moveToNext();
                    }
                    workbook.write();
                    workbook.close();
                    Toast.makeText(getApplicationContext(),saveRoot+"에 저장되었습니다"+a,Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_Load:                          //---------------------------불러오기!!!!!!!!!!!!!!!!!!!!!
                File filePath=new File(saveRoot);
                try {
                    workbook_load=Workbook.getWorkbook(filePath);
                    Sheet sheet_load=workbook_load.getSheet(0);
                    //Excel File Total row & Column count
                    int rowLength=sheet_load.getRows();
                    int columnsLength=sheet_load.getColumns();
                    //Reading
                    String[][] data=new String[rowLength][columnsLength];

                    db=mHelper.getWritableDatabase();
                    cursor1=db.rawQuery(queryNum,null);
                    cursor1.moveToLast();
                    num0=cursor1.getInt(0);      //위에 데이터를 기반으로 +1을 해줌 이거라면 가능!
                    num0+=1;
                    Strnum0=String.valueOf(num0);//정수를 문자열로 바꿈
                    Toast.makeText(getApplicationContext(),rowLength+" "+columnsLength,Toast.LENGTH_LONG).show();
           //      Toast.makeText(getApplicationContext(),"데이터를 불러왔습니다"+data[1][0],Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"오류남",Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return true;
    }
    public void list_sort(){//정렬 메소드
        cursor = db.rawQuery(querySort, null);
        myAdapter = new MyCursorAdapter(this, cursor);
        list.setAdapter(myAdapter);         //초기화면 데이터가 리스트뷰에 뜨게끔 하는거임
    }
    public void list_all(){//조회 메소드
        cursor = db.rawQuery(querySelectAll, null);
        myAdapter = new MyCursorAdapter(this, cursor);
        list.setAdapter(myAdapter);         //초기화면 데이터가 리스트뷰에 뜨게끔 하는거임
    }
}
