package mysop.pia.com.Categories;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.ShelfRoom.AppDatabase;
import mysop.pia.com.Categories.ShelfRoom.MySOPs;
import mysop.pia.com.Firebase.Firebase;
import mysop.pia.com.ListofHandbooks.ListofHandbooks;
import mysop.pia.com.ListofHandbooks.ListofHandbooksAdapter;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.R;

public class ShelfRecyclerAdapter extends RecyclerView.Adapter<ShelfRecyclerAdapter.Viewholder> {

    private final AppDatabase db;
    private List<MySOPs> categoryList;
    private Context context;
    public static String categoryName;

    public ShelfRecyclerAdapter(List<MySOPs> sopCategories, Context context, AppDatabase appDatabase) {
        this.context = context;
        this.categoryList = sopCategories;
        this.db = appDatabase;
    }

    @Override
    public void onBindViewHolder(@NonNull ShelfRecyclerAdapter.Viewholder viewholder, int position) {
        String sharedAuthor = categoryList.get(position).getSharedAuthor();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewholder.imageviewCategory.getLayoutParams();

        viewholder.categoryTitle.setText(categoryList.get(position).getCategoryTitle());

        if (categoryList.get(position).getSharedAuthor() == null) {
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
                            editCategory(categoryList.get(position).getCategoryTitle(), categoryList.get(position).getId());
                            return true;
                        case R.id.book_shelf_collab:
                            Intent shareFirebase = new Intent(context, Firebase.class);
                            ListofHandbooksAdapter.bookShare = 0;
                            categoryName = categoryList.get(position).getCategoryTitle();
                            context.startActivity(shareFirebase);
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
        } else {
            viewholder.imgCatOptions.setVisibility(View.INVISIBLE);
            viewholder.imgCatShared.setVisibility(View.VISIBLE);
            viewholder.imgCatShared.setOnClickListener(v -> Toast.makeText(context, categoryList.get(position).getCategoryTitle() + context.getString(R.string.sharedBy) + sharedAuthor, Toast.LENGTH_LONG).show());
        }


        if (sharedAuthor != (null) && sharedAuthor.equals(context.getString(R.string.sharedpass))) {
//            THIS IS FOR SHARED SHELF
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewholder.imageviewCategory.setImageResource(R.drawable.ic_action_share);
            viewholder.categoryTitle.setTextSize(21);
            viewholder.imgCatOptions.setVisibility(View.GONE);
            viewholder.imgCatShared.setVisibility(View.GONE);
        }

        if (sharedAuthor != (null) && sharedAuthor.equals(context.getString(R.string.bookmarkpass))) {
//            THIS IS FOR BOOKMARKED BOOKS
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewholder.categoryTitle.setTextSize(21);
            viewholder.imageviewCategory.setImageResource(R.drawable.baseline_bookmarks_black_36dp);
            viewholder.imgCatOptions.setVisibility(View.GONE);
            viewholder.imgCatShared.setVisibility(View.GONE);
        }


//        DO THIS ON PRESS
        viewholder.categoryLayout.setOnClickListener((View view) -> {
            Intent categorySops = new Intent(context, ListofHandbooks.class);
            categoryName = categoryList.get(position).getCategoryTitle();
            context.startActivity(categorySops);
        });
    }

    private void editCategory(String categoryTitle, int id) {
        Intent editShelf = new Intent(context, AddShelf.class);
        editShelf.putExtra(context.getString(R.string.shelftitle), categoryTitle);
        editShelf.putExtra(context.getString(R.string.d), id);
        editShelf.putExtra(context.getString(R.string.edit1), false);
        context.startActivity(editShelf);
        ((Activity) context).finish();
    }

    @NonNull
    @Override
    public ShelfRecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
        @BindView(R.id.imageview_cat_shared)
        ImageView imgCatShared;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void alertToDelete(String categoryName, int position) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle(R.string.deleteentry)
                .setMessage(context.getString(R.string.rusuredelete) + categoryName + context.getString(R.string.questionmark))
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                    db.mysopDao().deleteCategory(categoryName);
                    stepsRoomDatabase().listOfSteps().DeleteShelfBooks(categoryName);
                    Toast.makeText(context, categoryName + context.getString(R.string.isdeleted), Toast.LENGTH_SHORT).show();
                    categoryList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, categoryName + context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(R.drawable.ic_delete)
                .show();
//        return position;
    }

    private StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(context, StepsAppDatabase.class, context.getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

}
