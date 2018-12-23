package mysop.pia.com.ListofSOPs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mysop.pia.com.R;

public class AddStep extends AppCompatActivity {

    private static final String TAG = "ADDSTEP";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_step);

        String sopTitle = getIntent().getStringExtra("sopTitle");
        Log.i(TAG, "onCreate: " + sopTitle);
    }
}
