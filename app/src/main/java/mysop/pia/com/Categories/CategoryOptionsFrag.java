package mysop.pia.com.Categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Firebase.Firebase;
import mysop.pia.com.R;

public class CategoryOptionsFrag extends android.support.v4.app.Fragment {

    @BindView(R.id.button_cat_opt_frag_share)
    ImageButton btnShare;
    @BindView(R.id.button_cat_opt_frag_edit)
    ImageButton btnEdit;
    @BindView(R.id.button_cat_opt_frag_delete)
    ImageButton btnDelete;
    @BindView(R.id.textview_catopt_frag_label)
    TextView tvCatOptionLabel;

    private Context context;
    private String categoryName;
    private FrameLayout mCatOptionsFrag;

    public CategoryOptionsFrag() {
    }

    public void getCategoryOptionsFrag(Context context, String categoryName, FrameLayout mCatOptionsFrag){
        this.context = context;
        this.categoryName = categoryName;
        this.mCatOptionsFrag = mCatOptionsFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create view
        View rootView = inflater.inflate(R.layout.fragment_category_options, container, false);
        ButterKnife.bind(this, rootView);

        String CatOptionsLabelConcat = categoryName + " Options";
        tvCatOptionLabel.setText(CatOptionsLabelConcat);

        btnShare.setOnClickListener(v -> {
//      DO THIS WHEN SHARE BTN IS PRESSED
            Intent shareFirebase = new Intent(context, Firebase.class);
            context.startActivity(shareFirebase);
            mCatOptionsFrag.setVisibility(View.GONE);
        });

        btnEdit.setOnClickListener(v -> {
//      DO THIS WHEN EDIT BTN IS PRESSED

        });

        btnDelete.setOnClickListener(v -> {
//      DO THIS WHEN DELETE BTN IS PRESSED

        });

        return rootView;
    }
}
