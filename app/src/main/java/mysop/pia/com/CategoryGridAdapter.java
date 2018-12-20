package mysop.pia.com;

import android.app.Activity;
import android.content.Context;
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

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryGridAdapter extends ArrayAdapter<FbCategory> {

    public CategoryGridAdapter(Context context, int categories_layout, List<FbCategory> fireBaseCategory) {
        super(context, categories_layout, fireBaseCategory);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.categories_layout, parent, false);
        }

        TextView categoryTitle = convertView.findViewById(R.id.textview_category_title);

        FbCategory categories = getItem(position);

        categoryTitle.setText(categories.getCategoryName());

        return convertView;
    }
}
