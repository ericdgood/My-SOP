package mysop.pia.com.ListofSOPs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.ListofSOPs.SopRoom.SopAppDatabase;
import mysop.pia.com.R;
import mysop.pia.com.Steps.ListOfSteps;

//import mysop.pia.com.Firebase.Firebase;

public class ListofSOPsAdapter extends RecyclerView.Adapter<ListofSOPsAdapter.Viewholder> {

    private List<SOPRoomData> listOfSOPS;
    private Context context;
    private SopAppDatabase db;

    ListofSOPsAdapter(Context context, List<SOPRoomData> listOfSOPs, SopAppDatabase sopAppDatabase) {
        this.context = context;
        this.listOfSOPS = listOfSOPs;
        this.db = sopAppDatabase;
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

        viewholder.constrainListOfSOPs.setOnLongClickListener(v -> {
            String sopTitle = listOfSOPS.get(position).getSopTitle();
            alertToDelete(sopTitle, position);
            return true;
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

    private void alertToDelete(String sopTitle, int position) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Share with Firebase")
                .setMessage("Are you sure you want to share " + sopTitle + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Long press to share", Toast.LENGTH_SHORT).show();
                        // SHARE WITH FIREBASE
//                        Intent shareFirebase = new Intent(context, Firebase.class);
//                        shareFirebase.putExtra("sopTitle", sopTitle);
//                        context.startActivity(shareFirebase);


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
//        return position;
    }
}
