package mysop.pia.com.Categories;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.ListofSOPs;
import mysop.pia.com.R;
import mysop.pia.com.RoomData.CatergoryRoom.MySOPs;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.Viewholder> {

    private List<MySOPs> categoryList;
    Context context;
    public static String categoryName;

    public CategoryRecyclerAdapter(List<MySOPs> sopCategories, Context context) {
        this.context = context;
        this.categoryList = sopCategories;
    }

    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_title) TextView categoryTitle;
        @BindView(R.id.constrantlayout_category)
        ConstraintLayout categoryLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.Viewholder viewholder, int position) {
        viewholder.categoryTitle.setText(categoryList.get(position).getCategoryTitle());

        viewholder.categoryLayout.setOnClickListener((View view) -> {
            Intent categorySops = new Intent(context, ListofSOPs.class);
            categoryName = categoryList.get(position).getCategoryTitle();
            context.startActivity(categorySops);
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
}
