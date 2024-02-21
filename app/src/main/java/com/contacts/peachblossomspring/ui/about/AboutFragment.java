package com.contacts.peachblossomspring.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.contacts.peachblossomspring.R;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ImageView logoImage = view.findViewById(R.id.image_logo);
        logoImage.setImageResource(R.drawable.logo_image);

        TextView aboutTextView = view.findViewById(R.id.text_about);
        aboutViewModel.getText().observe(getViewLifecycleOwner(), aboutTextView::setText);

        return view;
    }
}
