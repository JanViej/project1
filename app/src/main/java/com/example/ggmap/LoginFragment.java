package com.example.ggmap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.IMainActivity;

public class LoginFragment extends Fragment implements View.OnClickListener {
    public LoginFragment() {
        // Required empty public constructor
    }
    Button login;
    IMainActivity iMainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_layout, container, false);

        login = (Button) view.findViewById(R.id.btn_login);
        login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iMainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_login){
            iMainActivity.loginClick();
        }
    }
}