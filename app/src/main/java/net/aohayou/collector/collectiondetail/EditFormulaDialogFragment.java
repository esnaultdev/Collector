package net.aohayou.collector.collectiondetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import net.aohayou.collector.R;
import net.aohayou.collector.help.HelpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditFormulaDialogFragment extends DialogFragment {

    public interface Listener {
        void onCancel();
        void onFormulaSet(@NonNull String newFormulaString);
    }

    private static final String ARG_FORMULA_STRING = "CollectionName";
    private static final String BUNDLE_EDITED_FORMULA = "EditedName";
    private static final String BUNDLE_EDIT_SELECTION = "EditSelection";

    private Listener listener;

    private String oldFormulaString = "";
    private int oldSelection = -1;

    @BindView(R.id.formula_string_input_text) TextInputEditText editText;
    @BindView(R.id.help_icon) ImageView helpIcon;


    public void setDialogListener(Listener listener) {
        this.listener = listener;
    }

    public static EditFormulaDialogFragment getInstance(@NonNull String formulaString) {
        EditFormulaDialogFragment fragment = new EditFormulaDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_FORMULA_STRING, formulaString);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bindArguments();
        restoreState(savedInstanceState);
        return buildDialog();
    }

    private void bindArguments() {
        if (getArguments() != null) {
            oldFormulaString = getArguments().getString(ARG_FORMULA_STRING, "");
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            oldFormulaString = savedInstanceState.getString(
                    BUNDLE_EDITED_FORMULA, oldFormulaString);
            oldSelection = savedInstanceState.getInt(BUNDLE_EDIT_SELECTION, -1);
        }
        if (oldSelection == -1) {
            oldSelection = oldFormulaString.length();
        }
    }

    private Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setView(R.layout.dialog_formula_edit)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newFormulaString = editText.getText().toString();
                        listener.onFormulaSet(newFormulaString);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancel();
                        EditFormulaDialogFragment.this.getDialog().cancel();
                    }

                })
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getDialog());

        editText.setText(oldFormulaString);
        editText.setSelection(oldSelection);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AlertDialog dialog = (AlertDialog) getDialog();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                    handled = true;
                }
                return handled;
            }
        });

        helpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Should go back to the presenter
                displayHelp();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_EDIT_SELECTION, editText.getSelectionStart());
        outState.putString(BUNDLE_EDITED_FORMULA, editText.getText().toString());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onCancel();
        super.onCancel(dialog);
    }

    private void displayHelp() {
        Intent intent = new Intent(
                EditFormulaDialogFragment.this.getActivity(), HelpActivity.class);
        startActivity(intent);
    }
}
