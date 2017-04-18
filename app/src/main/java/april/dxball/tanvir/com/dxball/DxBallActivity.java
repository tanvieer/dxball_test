package april.dxball.tanvir.com.dxball;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class DxBallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameCanvas(this));
        //setContentView(R.layout.activity_dx_ball);
        Log.d("sensorval", "On create Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GameCanvas.AccelerometerCheckingStop();

        Log.d("sensorval", "On Destroy Called");
    }
}
