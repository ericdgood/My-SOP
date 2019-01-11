package mysop.pia.com.ListofSOPs;

import android.app.Activity;
import android.app.AlertDialog;
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
import mysop.pia.com.R;
import mysop.pia.com.Steps.ListOfSteps;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

//import mysop.pia.com.Firebase.Firebase;

public class ListofSOPsAdapter extends RecyclerView.Adapter<ListofSOPsAdapter.Viewholder> {

    private final StepsAppDatabase db;
    private List<StepsRoomData> listOfSOPS;
    private Context context;
    int savedBook = 0;

    ListofSOPsAdapter(Context context, List<StepsRoomData> listOfSOPs, StepsAppDatabase stepsAppDatabase) {
        this.context = context;
        this.listOfSOPS = listOfSOPs;
        this.db = stepsAppDatabase;
    }

    @Override
    public void onBindViewHolder(@NonNull ListofSOPsAdapter.Viewholder viewholder, int position) {
        viewholder.tvBookTitle.setText(listOfSOPS.get(position).getSopTitle());

        bookColor(viewholder, position);

        viewholder.constrainBookList.setOnClickListener(v -> {
            Intent listOfSteps = new Intent(context, ListOfSteps.class);
            listOfSteps.putExtra("sopTitle", listOfSOPS.get(position).getSopTitle());
            context.startActivity(listOfSteps);
        });

//        BOOK OPTIONS
        bookOptions(viewholder, position);

//      BOOKMARK BOOKS
        savedBook = listOfSOPS.get(position).getSavedBook();
        if (savedBook == 1) {
            viewholder.imgBookSave.setImageResource(R.drawable.ic_bookmark);
        }
        saveBook(viewholder, position, listOfSOPS.get(position).getSopTitle());
    }

    private void bookColor(Viewholder viewholder, int position) {
        String bookColor = listOfSOPS.get(position).getBookColor();
        if (bookColor != null) {
            switch (bookColor) {
                case "Red":
                    viewholder.imgHandbook.setColorFilter(context.getColor(R.color.logoRedBookColor));
                case "Blue":
                    viewholder.imgHandbook.setColorFilter(context.getColor(R.color.logoBlueBookColor));
                case "Green":
                    viewholder.imgHandbook.setColorFilter(context.getColor(R.color.logoGreenBookColor));
                case "Yellow":
                    viewholder.imgHandbook.setColorFilter(context.getColor(R.color.logoYellowBookColor));
                case "Orange":
                    viewholder.imgHandbook.setColorFilter(context.getColor(R.color.logoOrangeBookColor));
                default:
            }
        }
    }

    @NonNull
    @Override
    public ListofSOPsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.books_list_layout, viewGroup, false);
        return new Viewholder(view);
    }

    private void saveBook(Viewholder viewholder, int position, String sopTitle) {
        int id = listOfSOPS.get(position).getId();
        viewholder.imgBookSave.setOnClickListener(v -> {
            if (savedBook == 0) {
//            SAVE BOOK
                viewholder.imgBookSave.setImageResource(R.drawable.ic_bookmark);
                db.listOfSteps().updateBookSaved(1, id);
                savedBook = 1;
                Toast.makeText(context, sopTitle + " Bookmarked", Toast.LENGTH_SHORT).show();
            } else if (savedBook == 1) {
//            UNSAVE BOOK
                viewholder.imgBookSave.setImageResource(R.drawable.ic_bookmark_border);
                db.listOfSteps().updateBookSaved(0, id);
                savedBook = 0;
                Toast.makeText(context, sopTitle + " Un-Bookmarked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookOptions(Viewholder viewholder, int position) {
        viewholder.imgBookOptions.setOnClickListener(v -> {
//            OPEN OPTIONS
            PopupMenu popup = new PopupMenu(context, viewholder.imgBookOptions);
            popup.inflate(R.menu.menu_book_shelf);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.book_shelf_edit:
                        editBook(listOfSOPS.get(position).getSopTitle(), listOfSOPS.get(position).getId());
                        return true;
                    case R.id.book_shelf_share:
                        return true;
                    case R.id.book_shelf_delete:
                        alertToDelete(listOfSOPS.get(position).getSopTitle(), position);
                        return true;
                    default:
                        return false;
                }
            });
            //displaying the popup
            popup.show();
        });
    }

    private void editBook(String sopTitle, int id) {
        Intent editSOP = new Intent(context, AddSOP.class);
        editSOP.putExtra("editSop", 1);
        editSOP.putExtra("editSopTitle", sopTitle);
        editSOP.putExtra("editId", id);
        context.startActivity(editSOP);
        ((Activity) context).finish();
    }

    private void alertToDelete(String bookTitle, int position) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete " + bookTitle + "?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                    db.listOfSteps().DeleteSOP(bookTitle);
                    Toast.makeText(context, bookTitle + " is Deleted", Toast.LENGTH_SHORT).show();
                    listOfSOPS.remove(position);
                    notifyDataSetChanged();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(R.drawable.ic_delete)
                .show();
//        return position;
    }

    @Override
    public int getItemCount() {
        return listOfSOPS.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        //        BINDVIEWS HERE
        @BindView(R.id.text_booklist_title)
        TextView tvBookTitle;
        @BindView(R.id.constrain_booklist_layout)
        ConstraintLayout constrainBookList;
        @BindView(R.id.image_booklist_saved)
        ImageView imgBookSave;
        @BindView(R.id.image_booklist_options)
        ImageView imgBookOptions;
        @BindView(R.id.imageview_handbook)
        ImageView imgHandbook;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
