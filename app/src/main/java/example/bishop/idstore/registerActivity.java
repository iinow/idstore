package example.bishop.idstore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class registerActivity extends AppCompatActivity {
    Button btnPassword_check,btnEmail_check;
    EditText etxPassword_write,etxEmail_write;
    static private SharedPreferences preferences;
    static SharedPreferences prefs;
    @Override

    protected void onCreate(Bundle savedInstanceState) {//최초실행시
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        btnPassword_check=(Button)findViewById(R.id.btnPassword_check);

        etxPassword_write=(EditText)findViewById(R.id.etxPassword_write);

        preferences=getSharedPreferences("shared",MODE_PRIVATE);//register이라는 파일명으로 저장됨
        preferences=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences=getPreferences(MODE_PRIVATE);//메서드로 파일 이름과 접근 권한을 지정

        btnPassword_check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();//수정하기 위해 에디터를 엽니다
                editor.putString("test1",etxPassword_write.getText().toString());//파라메터 앞 필드명 뒤 실 데이터
                editor.putString("hide","1");//hide 에 1이 저장됨
                editor.putString("clos","1");
                editor.commit();
               // new MainActivity().hideButton();//으아 실행해버림!버튼 사라지는 메소드 일회성으로 사용함
                etxPassword_write.setText("");
                String a=prefs.getString("test1","nothing");
                Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        preferences.getString("key","되나");

      /*  btnEmail_check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                prefs=getSharedPreferences("sEmail",MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("em",etxEmail_write.getText().toString());
                editor.commit();

                etxEmail_write.setText("");
                finish();
            }
        }); */
        }
}
