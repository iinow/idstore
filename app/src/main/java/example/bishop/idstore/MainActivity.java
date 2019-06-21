package example.bishop.idstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button button3,btnLogin,btnRegister;
    EditText etxPassword;
    SharedPreferences prefs;
    String text1;
    String hideButton;
    static private SharedPreferences preferences;

    //private List<DatabaseOpenHelper> DBRegi; // 데이터베이스 오픈 헬퍼 꺼낸다!!!!!!!!시발
    //EditText editText=(EditText) findViewById(R.id.editText);
    //helpActivity A=new helpActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) { //처음 실행시
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//여긴 괜찮았는데...........머지

        btnLogin=(Button)findViewById(R.id.btnLogin);       //로그인 버튼
        btnRegister=(Button)findViewById(R.id.btnRegister); //회원가입 버튼

        etxPassword=(EditText)findViewById(R.id.etxPassword);

        btnLogin.setOnClickListener(new View.OnClickListener(){//로그인시
            @Override
            public void onClick(View v) {
                prefs=getSharedPreferences("PreName",MODE_PRIVATE);
                text1=prefs.getString("test1","nothing");

                if(text1.equals(etxPassword.getText().toString())) {
                    etxPassword.setText("");
                    Intent intent = new Intent(getApplicationContext(), workActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 틀립니다",Toast.LENGTH_SHORT).show();
                }
                }
            }
        );
        hideButton();

    }
    public void hideButton()
    {

        preferences=getSharedPreferences("shared",MODE_PRIVATE);//register이라는 파일명으로 저장됨
        preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        preferences=getPreferences(MODE_PRIVATE);//메서드로 파일 이름과 접근 권한을 지정

        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
        hideButton=prefs.getString("hide","nothing");
        if(hideButton.equals("1")){
            btnRegister.setVisibility(View.INVISIBLE);
        }
        else{
            btnRegister.setVisibility(View.VISIBLE);
        }
    }
    public void btnEmRadio(View v){

    }

    public void btnPwRadio(View v){

    }

    public void visible()
    {
        /*if(A.a==0) {//1이 아닐 경우에 회원가입을 안했을경우
            button3.setVisibility(View.VISIBLE);
        }
        else {*/
            button3.setVisibility(View.INVISIBLE);


    }
    public void onButton1Clicked(View v) //인증번호 클릭시 메소드 실행!!!
    {
        Toast.makeText(getApplicationContext(),"이메일이 전송되었습니다.",Toast.LENGTH_LONG).show();
    }
    public void onButton2Clicked(View v)//work 액티비티로 들어가는 버튼
    {

            Intent intent1 = new Intent(getApplicationContext(), workActivity.class);
            startActivity(intent1);
     }

    public void onButton3Clicked(View v)//처음 사용자 용
    {
        Intent intent = new Intent(getApplicationContext(), registerActivity.class);
        startActivity(intent);
    }

    public void onButton1Radio(View v)
    {
        inflateLayout();
    }
    public void onButton2Radio(View v)
    {
        button3.setVisibility(View.INVISIBLE);
    }

    private  void inflateLayout()
    {
        LinearLayout contentsLayout=(LinearLayout) findViewById(R.id.contentsLayout);
        LayoutInflater inflater=(LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button,contentsLayout,true);

        Button selectButton=(Button) findViewById(R.id.selectButton);
    }
}
