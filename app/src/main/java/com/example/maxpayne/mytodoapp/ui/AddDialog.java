package com.example.maxpayne.mytodoapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.example.maxpayne.mytodoapp.R;


public class AddDialog extends DialogFragment {
    NoticeDialogListener mListener;
    EditText etTaskName;
    EditText etDescription;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.add_dialog, null);
        etTaskName = view.findViewById(R.id.etTaskName);
        etDescription = view.findViewById(R.id.etTaskDescription);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_dialog_head)
                .setView(view)
                .setPositiveButton(R.string.ok, ((dialogInterface, i) ->
                        mListener.onDialogPositiveClick(etTaskName.getText().toString(),
                                etDescription.getText().toString())))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        return builder.create();
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
}
