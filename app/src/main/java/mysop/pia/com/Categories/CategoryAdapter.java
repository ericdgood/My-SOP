//package mysop.pia.com.Categories;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.constraint.ConstraintLayout;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.List;
//
//import mysop.pia.com.FbCategory;
//import mysop.pia.com.ListofSOPs.ListofSOPs;
//import mysop.pia.com.R;
//import mysop.pia.com.RoomData.MySOPs;
//
//public class CategoryAdapter extends ArrayAdapter<FbCategory> {
//
//    public CategoryAdapter(Context context, int categories_layout, List<MySOPs> fireBaseCategory) {
////        super(context, categories_layout, fireBaseCategory);
//    }
//
//    @NonNull
//    @Override
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.categories_layout, parent, false);
//        }
//
//        TextView textViewcategoryTitle = convertView.findViewById(R.id.textview_category_title);
//        ConstraintLayout constraintLayoutCategory = convertView.findViewById(R.id.constrantlayout_category);
//
////        GETS POSITION AND SHOWS CATEGORY NAME
//        FbCategory categories = getItem(position);
//        String categoryTitle = categories.getCategoryName();
//        textViewcategoryTitle.setText(categoryTitle);
//
////        ONCLICK CATEGORY. PASS NAME
//        onClickCategory(constraintLayoutCategory, categoryTitle);
//
//        return convertView;
//    }
//
//    private void onClickCategory(ConstraintLayout constraintLayoutCategory, final String categoryTitle){
//        constraintLayoutCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent listOfSOPs = new Intent(getContext(), ListofSOPs.class);
//                listOfSOPs.putExtra("categoryTitle", categoryTitle);
//                getContext().startActivity(listOfSOPs);
//            }
//        });
//    }
//
//}
