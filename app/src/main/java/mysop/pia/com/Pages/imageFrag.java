package mysop.pia.com.Pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;

public class imageFrag extends android.support.v4.app.Fragment {

    @BindView(R.id.imagezoom)
    ImageView imgImageZoom;
    @BindView(R.id.closeimg)
    ImageView imgClose;
    String stringImage;

    public imageFrag(){}

    public void getImageZoom(String stringPicture){
        this.stringImage = stringPicture;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create view
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, rootView);

        Glide.with(this).load(stringImage).into(imgImageZoom);

        imgClose.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return rootView;
    }
}
