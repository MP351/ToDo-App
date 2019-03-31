package com.example.maxpayne.mytodoapp.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.example.maxpayne.mytodoapp.db.DbContract;
import com.example.maxpayne.mytodoapp.recycler_view.DateUtils;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class AddDialog extends DialogFragment {
    private NoticeDialogListener mListener;
    private TextInputLayout lTaskName;
    private EditText etTaskName;
    private EditText etDescription;
    private Spinner spDeadline;
    private long deadline;
    private DatePickerDialog tpd;
    private Calendar cal;
    private ArrayAdapter spDeadlineAdapter;
    private static final int MIN_TASK_LEN = 3;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.add_dialog, null);

        lTaskName = view.findViewById(R.id.tilTaskName);
        etTaskName = view.findViewById(R.id.etTaskName);
        etDescription = view.findViewById(R.id.etTaskDescription);
        spDeadline = view.findViewById(R.id.spDeadline);

        spDeadlineAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.deadline_spinner_menu, android.R.layout.simple_spinner_dropdown_item);
        spDeadline.setAdapter(spDeadlineAdapter);

        spDeadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cal = new GregorianCalendar();
                switch (position) {
                    case 0:
                        deadline = DbContract.ToDoEntry.TIMELESS_CODE;
                        break;
                    case 1:
                        deadline = cal.getTimeInMillis();
                        break;
                    case 2:
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
                        deadline = cal.getTimeInMillis();
                        break;
                    case 3:
                        tpd = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
                                        String str = DateUtils.convertDate(cal.getTimeInMillis());
                                        ((TextView) view).setText(str);
                                        deadline = cal.getTimeInMillis();
                                    }
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH));
                        tpd.show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_dialog_head)
                .setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> getDialog().dismiss());

        etTaskName.requestFocus();
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                view -> {
                    if (okToGo()) {
                        mListener.onDialogPositiveClick(etTaskName.getText().toString(),
                                etDescription.getText().toString(), deadline);
                        getDialog().dismiss();
                    }
                    else {
                        lTaskName.setError(getString(R.string.add_dialog_error));
                    }
                }
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private boolean okToGo() {
        int len = etTaskName.getText().length();
        return len > MIN_TASK_LEN;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(String taskName, String description, long deadline);
    }
}
