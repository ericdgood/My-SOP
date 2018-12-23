package mysop.pia.com.ListofSOPs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.Steps.AddStep;

public class AddSOP extends AppCompatActivity{

    private static final String TAG = "test";
    @BindView(R.id.edittext_add_sop_title)
    EditText editTextAddSopTitle;
    @BindView(R.id.button_add_sop_add_step)
    Button buttonAddStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sop);
        ButterKnife.bind(this);



        buttonAddStep.setOnClickListener(v -> {
            String addSopTitle = editTextAddSopTitle.getText().toString();

//            SOPRoomData newSOP = new SOPRoomData(categoryName, addSopTitle);
//            sopRoomDatabase().listOfSOPs().insertSop(newSOP);

            Intent addStep = new Intent(AddSOP.this, AddStep.class);
            addStep.putExtra("sopTitle", addSopTitle);
            startActivity(addStep);
        });
    }
}
