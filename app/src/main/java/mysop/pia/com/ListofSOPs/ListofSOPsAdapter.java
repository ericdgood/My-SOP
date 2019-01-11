package mysop.pia.com.ListofSOPs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        viewholder.constrainBookList.setOnClickListener(v -> {
            Intent listOfSteps = new Intent(context, ListOfSteps.class);
            listOfSteps.putExtra("sopTitle", listOfSOPS.get(position).getSopTitle());
            context.startActivity(listOfSteps);
        });

        savedBook = listOfSOPS.get(position).getSavedBook();
        if (savedBook == 1){
            viewholder.imgBookSave.setImageResource(R.drawable.ic_bookmark);
        }
        saveBook(viewholder,position);
    }

    @NonNull
    @Override
    public ListofSOPsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.books_list_layout, viewGroup, false);
        return new Viewholder(view);
    }

    private void saveBook(Viewholder viewholder, int position) {
        int id = listOfSOPS.get(position).getId();
        viewholder.imgBookSave.setOnClickListener(v -> {
        if (savedBook == 0){
//            SAVE BOOK
            viewholder.imgBookSave.setImageResource(R.drawable.ic_bookmark);
            db.listOfSteps().updateBookSaved(1,id);
            savedBook = 1;
        } else if (savedBook == 1){
//            UNSAVE BOOK
            viewholder.imgBookSave.setImageResource(R.drawable.ic_bookmark_border);
            db.listOfSteps().updateBookSaved(0,id);
            savedBook = 0;
        }
        });
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

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
