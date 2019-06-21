package example.bishop.idstore;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by osan on 2016-11-07.
 */

public class AlwaysTopServiceNotTouch extends Service {
    private View mView;
    private WindowManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   mView = mInflater.inflate(R.layout.always_on_top_view_not_touch, null);

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                // 항상 최상위 화면에 있도록 설정합니다
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                // 터치 이벤트를 받지 않습니다
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT); // 투명

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
