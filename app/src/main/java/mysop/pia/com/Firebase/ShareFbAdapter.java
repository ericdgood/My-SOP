package mysop.pia.com.Firebase;

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
import mysop.pia.com.R;

public class ShareFbAdapter extends RecyclerView.Adapter<ShareFbAdapter.Viewholder> {

    private final Context context;
    private final List<SharedInfo> sharedInfo;

    public ShareFbAdapter(Context context, List<SharedInfo> sharedInfo) {
        this.context = context;
        this.sharedInfo = sharedInfo;
    }

    @Override
    public void onBindViewHolder(@NonNull ShareFbAdapter.Viewholder viewHolder, int position) {
        viewHolder.tvSharedSopTitle.setText(sharedInfo.get(position).getSopSharedTitle());
        viewHolder.tvSharedAuthor.setText(sharedInfo.get(position).getAuthorName());
        viewHolder.tvSharedUser.setText(sharedInfo.get(position).getSharedUser());
    }

    @NonNull
    @Override
    public ShareFbAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shared_sop_layout, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public int getItemCount() {
        return sharedInfo.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_shared_soptitle)
        TextView tvSharedSopTitle;
        @BindView(R.id.textview_shared_authorname)
        TextView tvSharedAuthor;
        @BindView(R.id.textview_shared_shareduser)
        TextView tvSharedUser;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
