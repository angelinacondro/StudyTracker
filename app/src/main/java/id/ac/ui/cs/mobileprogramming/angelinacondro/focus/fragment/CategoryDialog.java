package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.R;

import id.ac.ui.cs.mobileprogramming.angelinacondro.focus.viewModel.ScheduleCategoryViewModel;

public class CategoryDialog extends AppCompatDialogFragment {

    private EditText editCategory;
    private ExampleDialogListener listener;
    ScheduleCategoryViewModel scheduleCategoryViewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_dialog, null);

        builder.setView(view)
                .setTitle("Add Category")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String category = editCategory.getText().toString();
                        listener.applyTexts(category);
                    }
                });
        editCategory = view.findViewById(R.id.edit_category);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String category);
    }



}
