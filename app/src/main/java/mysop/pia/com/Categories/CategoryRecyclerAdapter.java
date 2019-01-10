package mysop.pia.com.Categories;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.Firebase.Firebase;
import mysop.pia.com.ListofSOPs.ListofSOPs;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.Viewholder> {

    private final FrameLayout mCatOptionsFrag;
    private final AppDatabase db;
    private List<MySOPs> categoryList;
    private Context context;
    public static String categoryName;

    public CategoryRecyclerAdapter(List<MySOPs> sopCategories, Context context, FrameLayout mCatOptionsFrag, AppDatabase appDatabase) {
        this.context = context;
        this.categoryList = sopCategories;
        this.mCatOptionsFrag = mCatOptionsFrag;
        this.db = appDatabase;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.Viewholder viewholder, int position) {
            String sharedAuthor = categoryList.get(position).getSharedAuthor();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewholder.imageviewCategory.getLayoutParams();

        if (sharedAuthor != (null) && sharedAuthor.equals("JonNyBgOoDeSHARED")) {
//            THIS IS FOR BOOK MARKED SHELF
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewholder.imageviewCategory.setImageResource(R.drawable.ic_action_share);
            viewholder.categoryTitle.setTextSize(21);
            viewholder.imgCatOptions.setVisibility(View.GONE);
        }

        if (sharedAuthor != (null) && sharedAuthor.equals("JonNyBgOoDeMARKED")) {
//            THIS IS FOR SHARED BOOKS
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewholder.categoryTitle.setTextSize(21);
            viewholder.imageviewCategory.setImageResource(R.drawable.ic_bookmark);
            viewholder.imgCatOptions.setVisibility(View.GONE);
        }

            viewholder.categoryTitle.setText(categoryList.get(position).getCategoryTitle());

            viewholder.imgCatOptions.setOnClickListener(v -> {
//            OPEN OPTIONS
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, viewholder.imgCatOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_book_shelf);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.book_shelf_edit:
                            editCategory();
                            return true;
                        case R.id.book_shelf_share:
                            Intent shareFirebase = new Intent(context, Firebase.class);
                            context.startActivity(shareFirebase);
                            mCatOptionsFrag.setVisibility(View.GONE);
                            return true;
                        case R.id.book_shelf_delete:
                            alertToDelete(categoryList.get(position).getCategoryTitle(), position);
                            return true;
                        default:
                            return false;
                    }
                });
                //displaying the popup
                popup.show();
            });


//        DO THIS ON PRESS
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

    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_title)
        TextView categoryTitle;
        @BindView(R.id.constrantlayout_category)
        ConstraintLayout categoryLayout;
        @BindView(R.id.imageview_category)
        ImageView imageviewCategory;
        @BindView(R.id.imageview_cat_options)
        ImageView imgCatOptions;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void editCategory() {
        CategoryOptionsFrag categoryOptions = new CategoryOptionsFrag();
            categoryOptions.getCategoryOptionsFrag(context);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout_category_options_frag, categoryOptions)
                    .addToBackStack("tag")
                    .commit();

    }

    private void alertToDelete(String categoryName, int position) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete " + categoryName + "?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                    db.mysopDao().deleteCategory(categoryName);
                    stepsRoomDatabase().listOfSteps().DeleteShelfBooks(categoryName);
                    Toast.makeText(context, categoryName + " is Deleted" + position, Toast.LENGTH_SHORT).show();
                    categoryList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context,  categoryName + " Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(R.drawable.ic_delete)
                .show();
//        return position;
    }

    private StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(context, StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

}
