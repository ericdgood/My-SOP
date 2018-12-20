package mysop.pia.com;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {


    public CategoryGridAdapter(Context context) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_category_title)
        TextView textviewCategoryTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryGridAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textviewCategoryTitle.setText("test123");
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
