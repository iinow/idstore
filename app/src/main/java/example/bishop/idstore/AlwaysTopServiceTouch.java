package example.bishop.idstore;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static example.bishop.idstore.registerActivity.prefs;

/**
 * Created by osan on 2016-11-07.
 */


public class AlwaysTopServiceTouch extends Service {
    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    String strI, strP;

    TextView tvID, tvPw;
    LinearLayout layout1;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private boolean isMove = false;

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;

                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;

                    break;

                case MotionEvent.ACTION_UP:
                    if (!isMove) {
                        Toast.makeText(getApplicationContext(), "터치됨",
                                Toast.LENGTH_SHORT).show();
                    }

                    break;

                case MotionEvent.ACTION_MOVE:
                    isMove = true;

                    int x = (int) (event.getRawX() - mTouchX);
                    int y = (int) (event.getRawY() - mTouchY);

                    final int num = 5;
                    if ((x > -num && x < num) && (y > -num && y < num)) {
                        isMove = false;
                        break;
                    }

                    /**
                     * mParams.gravity에 따른 부호 변경
                     *
                     * LEFT : x가 +
                     *
                     * RIGHT : x가 -
                     *
                     * TOP : y가 +
                     *
                     * BOTTOM : y가 -
                     */
                    mParams.x = mViewX + x;
                    mParams.y = mViewY + y;

                    mManager.updateViewLayout(layout1, mParams);

                    break;
            }

            return true;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // mView = mInflater.inflate(R.layout.always_on_top_view_touch, null);

        layout1=new LinearLayout(this);
        layout1.setOrientation(LinearLayout.VERTICAL);
        layout1.setPadding(10,50,10,50);

        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
        strI=prefs.getString("strI","nothing");

        prefs=getSharedPreferences("PreName",MODE_PRIVATE);
        strP=prefs.getString("strP","nothing");

        tvID.setText(strI);
        tvPw.setText(strP);

        tvID.setBackgroundColor(00000000);
        tvID.setTextColor(182654);

        layout1.addView(tvID);
        layout1.addView(tvPw);

        layout1.setOnTouchListener(mViewTouchListener);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.TOP | Gravity.LEFT;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(layout1, mParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) {
            mManager.removeView(layout1);
            mView = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
