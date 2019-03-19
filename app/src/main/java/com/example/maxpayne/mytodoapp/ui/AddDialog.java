package com.example.maxpayne.mytodoapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maxpayne.mytodoapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class AddDialog extends DialogFragment {
    private NoticeDialogListener mListener;
    private TextInputLayout lTaskName;
    private EditText etTaskName;
    private EditText etDescription;
    private static final int MIN_TASK_LEN = 3;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.add_dialog, null);

        lTaskName = view.findViewById(R.id.tilTaskName);
        etTaskName = view.findViewById(R.id.etTaskName);
        etDescription = view.findViewById(R.id.etTaskDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_dialog_head)
                .setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> getDialog().dismiss());

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                view -> {
                    if (okToGo()) {
                        mListener.onDialogPositiveClick(etTaskName.getText().toString(),
                                etDescription.getText().toString());
                        getDialog().dismiss();
                    }
                    else {
                        lTaskName.setError(getString(R.string.add_dialog_error));
                    }
                }
        );
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
        void onDialogPositiveClick(String taskName, String description);
    }

    private static final class ActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            return false;
        }
    }
}
