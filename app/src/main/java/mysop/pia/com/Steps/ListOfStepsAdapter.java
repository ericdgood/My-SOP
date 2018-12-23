package mysop.pia.com.Steps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListOfStepsAdapter extends RecyclerView.Adapter<ListOfStepsAdapter.Viewholder> {

    private List<StepsRoomData> listOfSteps;
    Context context;

    ListOfStepsAdapter(Context context, List<StepsRoomData> listOfSteps) {
        this.context = context;
        this.listOfSteps = listOfSteps;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_step_number)
        TextView textviewStepNumber;
        @BindView(R.id.textview_step_title)
        TextView textviewStepTitle;

        Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfStepsAdapter.Viewholder viewholder, int position) {
        viewholder.textviewStepNumber.setText(String.valueOf(listOfSteps.get(position).getStepNumber()));
        viewholder.textviewStepTitle.setText(listOfSteps.get(position).getStepTitle());
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
