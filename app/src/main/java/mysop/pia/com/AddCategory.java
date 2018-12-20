package mysop.pia.com;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class AddCategory extends Activity {

    @BindView(R.id.editText_category_name)
    EditText editTextCategoryName;
    @BindView(R.id.button_save)
    Button buttonSave;


    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCategoryDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        ButterKnife.bind(this);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCategoryDatabaseReference = mFirebaseDatabase.getReference().child("categories");

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                THIS WILL SAVE THE CATEGORY INFO
                FbCategory category = new FbCategory(editTextCategoryName.getText().toString());
                mCategoryDatabaseReference.push().setValue(category);

                Intent returnHome = new Intent(AddCategory.this, MainActivity.class);
                startActivity(returnHome);
                finish();
            }
        });

    }

}
