package com.example.ggmap;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class InforFragment extends Fragment implements View.OnClickListener{
    public InforFragment(){

    }

    private final String SIMPLE_FRAGMENT_TAG = "INFO";
    private int idInputText;
    private EditText date_dau_nhot;
    private EditText date_dau_lap;
    private EditText date_lop;
    private EditText date_dau_xich;

    private int mYear, mMonth, mDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_infor,container,false);

        date_dau_nhot = view.findViewById(R.id.input_date_dau_nhot);
        date_dau_lap = view.findViewById(R.id.input_date_dau_lap);
        date_lop = view.findViewById(R.id.input_date_lop);
        date_dau_xich = view.findViewById(R.id.input_date_xich);

        date_dau_nhot.setOnClickListener(this);
        date_dau_lap.setOnClickListener(this);
        date_lop.setOnClickListener(this);
        date_dau_xich.setOnClickListener(this);

        return view;
    }

    private void updateText(int year, int month, int day) {
        if (idInputText == 0) {
            date_dau_nhot.setText(day + "/" + (month + 1) + "/" + year);
        }
        if (idInputText == 1) {
            date_dau_lap.setText(day + "/" + (month + 1) + "/" + year);
        }
        if (idInputText == 2) {
            date_lop.setText(day + "/" + (month + 1) + "/" + year);
        }
        if (idInputText == 3) {
            date_dau_xich.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == date_dau_nhot) {
            idInputText = 0;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            updateText(year, monthOfYear, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (view == date_dau_lap) {
            idInputText = 1;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            updateText(year, monthOfYear, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (view == date_lop) {
            idInputText = 2;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            updateText(year, monthOfYear, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (view == date_dau_xich) {
            idInputText = 3;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            updateText(year, monthOfYear, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}
