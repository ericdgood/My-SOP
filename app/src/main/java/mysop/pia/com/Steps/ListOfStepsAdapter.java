package mysop.pia.com.Steps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.net.URI;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListOfStepsAdapter extends RecyclerView.Adapter<ListOfStepsAdapter.Viewholder> {

    private List<StepsRoomData> listOfSteps;
    Context context;
    public static String sopTitle;
    public static String stepImage;
    public static String stepDescrition;
    public static String stepTitle;
    public static String stepNumber;
    public static int stepId;

    ListOfStepsAdapter(Context context, List<StepsRoomData> listOfSteps) {
        this.context = context;
        this.listOfSteps = listOfSteps;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_step_list_number)
        TextView textviewStepNumber;
        @BindView(R.id.textview_step_list_title)
        TextView textviewStepTitle;
        @BindView(R.id.constrain_layout_list_step)
        ConstraintLayout constraintListOfSteps;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfStepsAdapter.Viewholder viewholder, int position) {
//        SHOWS STEP NUMBERS
        viewholder.textviewStepNumber.setText(String.valueOf(listOfSteps.get(position).getStepNumber()));
//        SHOWS STEP TITLE
        viewholder.textviewStepTitle.setText(listOfSteps.get(position).getStepTitle());

        viewholder.constraintListOfSteps.setOnClickListener(v -> {
        Intent goToStep = new Intent(context, StepActivity.class);
        stepImage = listOfSteps.get(position).getImageURI();
        stepDescrition = listOfSteps.get(position).getStepDescription();
        stepTitle = listOfSteps.get(position).getStepTitle();
        stepNumber = String.valueOf(listOfSteps.get(position).getStepNumber());
        sopTitle = listOfSteps.get(position).getSopTitle();
        stepId = listOfSteps.get(position).getId();
        context.startActivity(goToStep);
        });
    }

    @NonNull
    @Override
    public ListOfStepsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_of_steps_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return listOfSteps.size();
    }
}
