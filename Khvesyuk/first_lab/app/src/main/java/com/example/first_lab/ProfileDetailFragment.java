package com.example.first_lab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.first_lab.model.Profile;

public class ProfileDetailFragment extends Fragment {
    private static final String ARG_PROFILE = "profile";

    private Profile profile;

    public static ProfileDetailFragment newInstance(Profile profile) {
        ProfileDetailFragment fragment = new ProfileDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE, profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (Profile) getArguments().getSerializable(ARG_PROFILE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        TextView title = view.findViewById(R.id.detail_title);
        TextView description = view.findViewById(R.id.detail_description);

        if (profile != null) {
            title.setText(profile.getTitle());
            description.setText(profile.getDescription());
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().findViewById(R.id.recyclerView).setVisibility(View.VISIBLE); // Показываем список обратно
        requireActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE); // Скрываем фрагмент
    }
}
