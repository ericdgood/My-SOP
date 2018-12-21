package mysop.pia.com.ListofSOPs;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;

public class ListofSOPs extends Activity {

    @BindView(R.id.listsop_categorytitle)
    TextView textViewCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_sops);
        ButterKnife.bind(this);

       String categoryTitle = getIntent().getStringExtra("categoryTitle");

       textViewCategoryTitle.setText(categoryTitle);
    }

}
