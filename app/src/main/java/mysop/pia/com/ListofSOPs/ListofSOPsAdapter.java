package mysop.pia.com.ListofSOPs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private final RecyclerView recyclerview;
    private List<StepsRoomData> listOfSOPS;
    private Context context;
    String sopTitle;
    private String alertBoxTitle;
    private String alertBoxMessage;

    ListofSOPsAdapter(Context context, List<StepsRoomData> listOfSOPs, StepsAppDatabase stepsAppDatabase, RecyclerView recyclerviewListofSOPs) {
        this.context = context;
        this.listOfSOPS = listOfSOPs;
        this.db = stepsAppDatabase;
        this.recyclerview = recyclerviewListofSOPs;
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

    @Override
    public void onBindViewHolder(@NonNull ListofSOPsAdapter.Viewholder viewholder, int position) {
        viewholder.textViewListOfSopsTitle.setText(listOfSOPS.get(position).getSopTitle());

        viewholder.constrainListOfSOPs.setOnClickListener(v -> {
            Intent listOfSteps = new Intent(context, ListOfSteps.class);
            listOfSteps.putExtra("sopTitle", listOfSOPS.get(position).getSopTitle());
            context.startActivity(listOfSteps);
        });

//        new SwipeHelper(context, recyclerview) {
//            @Override
//            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Delete",
//                        R.color.logoBlueBookColor,
//                        pos -> {
//                            // TODO: onDelete
//                            sopTitle = listOfSOPS.get(viewHolder.getAdapterPosition()).getSopTitle();
//                            alertBoxTitle = "Delete SOP";
//                            alertBoxMessage = "Are you sure you want to delete " + sopTitle + " SOP?";
//                            alertToDelete(alertBoxTitle, alertBoxMessage);
//                        }
//                ));
//
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Edit",
//                        R.color.logoBlueBookColor,
//                        pos -> {
//                            // TODO: Edit
//                            alertBoxTitle = "Edit SOP";
//                            alertToDelete(alertBoxTitle, alertBoxMessage);
//                        }
//                ));
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Share",
//                        R.color.logoYellowBookColor,
//                        pos -> {
//                            // TODO: Shared
//                            alertBoxTitle = "Share SOP";
//                            alertToDelete(alertBoxTitle, alertBoxMessage);
//                        }
//                ));
//            }
//        };
    }


    private void alertToDelete(String alertBoxTitle, String alertBoxMessage) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle(alertBoxTitle)
                .setMessage(alertBoxMessage)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        DO STEP HERE
                    if (alertBoxTitle.equals("Delete SOP")) {
                        db.listOfSteps().DeleteSOP(sopTitle);
                        notifyDataSetChanged();
                        Toast.makeText(context, sopTitle + " is deleted", Toast.LENGTH_SHORT).show();
                    } else if (alertBoxTitle.equals("Edit SOP")){

                        Toast.makeText(context, "Edit SOP", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "Share SOP", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
//        return position;
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
