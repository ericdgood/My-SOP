package mysop.pia.com;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {

    private final Context mContext;

    public CategoryGridAdapter(Context context) {
        this.mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//      USE BUUTTERKNIFE TO BIND VIEWS
        @BindView(R.id.textview_category_title)
        TextView textviewCategoryTitle;
        @BindView(R.id.imageview_category)
        ImageView imageviewCategory;
        @BindView(R.id.constrantlayout_category)
        ConstraintLayout constraintLayoutCategory;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridAdapter.ViewHolder viewHolder, int i) {
//        LOADS THE CATEGORY NAME
        viewHolder.textviewCategoryTitle.setText("test123");
//        LOADS THE CATEGORY LOGO
        Picasso.get().load(R.drawable.ic_home).into(viewHolder.imageviewCategory);
//        SET BACKGROUND COLOR OF LAYOUT
        viewHolder.constraintLayoutCategory.setBackgroundResource(R.color.logoGreenBookColor);
    }

    @NonNull
    @Override
    public CategoryGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
