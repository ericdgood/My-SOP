package mysop.pia.com.ListofSOPs;

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
import mysop.pia.com.R;
import mysop.pia.com.Steps.ListOfSteps;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

//import mysop.pia.com.Firebase.Firebase;

public class ListofSOPsAdapter extends RecyclerView.Adapter<ListofSOPsAdapter.Viewholder> {

    private List<StepsRoomData> listOfSOPS;
    private Context context;

    ListofSOPsAdapter(Context context, List<StepsRoomData> listOfSOPs) {
        this.context = context;
        this.listOfSOPS = listOfSOPs;
    }

    @Override
    public void onBindViewHolder(@NonNull ListofSOPsAdapter.Viewholder viewholder, int position) {
        viewholder.textViewListOfSopsTitle.setText(listOfSOPS.get(position).getSopTitle());

        viewholder.constrainListOfSOPs.setOnClickListener(v -> {
            Intent listOfSteps = new Intent(context, ListOfSteps.class);
            listOfSteps.putExtra("sopTitle", listOfSOPS.get(position).getSopTitle());
            context.startActivity(listOfSteps);
        });

    }

    public class Viewholder extends RecyclerView.ViewHolder {
        //        BINDVIEWS HERE
        @BindView(R.id.textview_list_sops_title)
        TextView textViewListOfSopsTitle;
        @BindView(R.id.textview_list_sops_number_of_steps)
        TextView textviewNumberOfSteps;
        @BindView(R.id.constrain_layout_list_of_sop_layout)
        ConstraintLayout constrainListOfSOPs;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ListofSOPsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sops_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return listOfSOPS.size();
    }

}
