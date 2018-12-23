package mysop.pia.com.Steps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;

public class ListOfSteps extends AppCompatActivity {

    @BindView(R.id.textview_list_steps_title)
    TextView textviewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_steps);
        ButterKnife.bind(this);
        String sopTitle = getIntent().getStringExtra("sopTitle");

        textviewTitle.setText(sopTitle);
    }

}
