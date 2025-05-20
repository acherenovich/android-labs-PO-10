package com.example.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    private TextView nameTextView, countryTextView, webPageTextView, domainTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        nameTextView = view.findViewById(R.id.textViewName);
        countryTextView = view.findViewById(R.id.textViewCountry);
        webPageTextView = view.findViewById(R.id.textViewWebPage);
        domainTextView = view.findViewById(R.id.textViewDomain);

        Bundle bundle = getArguments();
        if (bundle != null) {
            nameTextView.setText(bundle.getString("name"));
            countryTextView.setText(bundle.getString("country"));
            webPageTextView.setText(bundle.getString("webPage"));
            domainTextView.setText(bundle.getString("domain"));
        }

        return view;
    }
}