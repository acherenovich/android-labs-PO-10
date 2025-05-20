package com.example.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    private static final String ARG_ITEM_ID = "item_id";
    private static final String ARG_ITEM_TITLE = "item_title";
    private static final String ARG_ITEM_DESCRIPTION = "item_description";

    private int itemId;
    private String itemTitle;
    private String itemDescription;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(Item item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_ID, item.getId());
        args.putString(ARG_ITEM_TITLE, item.getTitle());
        args.putString(ARG_ITEM_DESCRIPTION, item.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getInt(ARG_ITEM_ID);
            itemTitle = getArguments().getString(ARG_ITEM_TITLE);
            itemDescription = getArguments().getString(ARG_ITEM_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tvDetail = rootView.findViewById(R.id.tv_detail);
        String detailText = "ID: " + itemId + "\n\n" +
                "Name: " + itemTitle + "\n\n" +
                "Description: " + itemDescription;
        tvDetail.setText(detailText);

        Button btnBack = rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            if(getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
}
