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
import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.Steps.ListOfSteps;

public class ListofSOPsAdapter extends RecyclerView.Adapter<ListofSOPsAdapter.Viewholder> {

    private List<SOPRoomData> listOfSOPS;
    Context context;

    public ListofSOPsAdapter(Context context, List<SOPRoomData> listOfSOPs) {
        this.context = context;
        this.listOfSOPS = listOfSOPs;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
//        BINDVIEWS HERE
        @BindView(R.id.textview_list_sops_title)
        TextView textViewListOfSopsTitle;
        @BindView(R.id.textview_list_sops_number_of_steps)
        TextView textviewNumberOfSteps;
        @BindView(R.id.constrain_layout_list_of_sop_layout)
        ConstraintLayout constrainListOfSOPs;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ListofSOPsAdapter.Viewholder viewholder, int postion) {
        viewholder.textViewListOfSopsTitle.setText(listOfSOPS.get(postion).getSopTitle());
        String numberOfSteps = String.valueOf(listOfSOPS.get(postion).getStepNumber());
        viewholder.textviewNumberOfSteps.setText(numberOfSteps);

        viewholder.constrainListOfSOPs.setOnClickListener(v -> {
            Intent listOfSteps = new Intent(context, ListOfSteps.class);
            listOfSteps.putExtra("sopTitle", listOfSOPS.get(postion).getSopTitle());
            context.startActivity(listOfSteps);
        });
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
