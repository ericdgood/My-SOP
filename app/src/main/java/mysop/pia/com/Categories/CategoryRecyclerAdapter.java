package mysop.pia.com.Categories;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.ListofSOPs.ListofSOPs;
import mysop.pia.com.R;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.Viewholder> {

    private final FrameLayout mCatOptionsFrag;
    private List<MySOPs> categoryList;
    private Context context;
    private AppDatabase db;
    public static String categoryName;

    public CategoryRecyclerAdapter(List<MySOPs> sopCategories, Context context, AppDatabase appDatabase, FrameLayout mCatOptionsFrag) {
        this.context = context;
        this.categoryList = sopCategories;
        this.db = appDatabase;
        this.mCatOptionsFrag = mCatOptionsFrag;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.Viewholder viewholder, int position) {
        viewholder.categoryTitle.setText(categoryList.get(position).getCategoryTitle());

        viewholder.categoryLayout.setOnClickListener((View view) -> {
            Intent categorySops = new Intent(context, ListofSOPs.class);
            categoryName = categoryList.get(position).getCategoryTitle();
            context.startActivity(categorySops);
        });

        viewholder.categoryLayout.setOnLongClickListener(v -> {
            mCatOptionsFrag.setVisibility(View.VISIBLE);
            categoryName = categoryList.get(position).getCategoryTitle();

            CategoryOptionsFrag categoryOptions = new CategoryOptionsFrag();
            categoryOptions.getCategoryOptionsFrag(context,categoryName, mCatOptionsFrag);
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_category_options_frag, categoryOptions)
                    .addToBackStack("tag")
                    .commit();
            return true;
        });
    }

    @NonNull
    @Override
    public CategoryRecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_title)
        TextView categoryTitle;
        @BindView(R.id.constrantlayout_category)
        ConstraintLayout categoryLayout;
        @BindView(R.id.imageview_category)
        ImageView imageviewCategory;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
