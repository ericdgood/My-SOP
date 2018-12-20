package mysop.pia.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends ArrayAdapter<FbCategory> {

    public CategoryAdapter(Context context, int categories_layout, List<FbCategory> fireBaseCategory) {
        super(context, categories_layout, fireBaseCategory);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.categories_layout, parent, false);
        }

        TextView textViewcategoryTitle = convertView.findViewById(R.id.textview_category_title);
        ConstraintLayout constraintLayoutCategory = convertView.findViewById(R.id.constrantlayout_category);

//        GETS POSITION AND SHOWS CATEGORY NAME
        FbCategory categories = getItem(position);
        String categoryTitle = categories.getCategoryName();
        textViewcategoryTitle.setText(categoryTitle);

//        ONCLICK CATEGORY. PASS NAME
        onClickCategory(constraintLayoutCategory, categoryTitle);

        return convertView;
    }

    private void onClickCategory(ConstraintLayout constraintLayoutCategory, final String categoryTitle){
        constraintLayoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listOfSOPs = new Intent(getContext(), ListofSOPs.class);
                listOfSOPs.putExtra("categoryTitle", categoryTitle);
                getContext().startActivity(listOfSOPs);
            }
        });
    }

}
