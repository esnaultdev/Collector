package net.aohayou.collector.collections;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import net.aohayou.collector.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateRenameCollectionDialogFragment extends DialogFragment {

    public interface BaseListener {
        void onCancel();
    }

    public interface CreateListener extends BaseListener {
        void onCollectionCreate(@NonNull String collectionName);
    }

    public interface RenameListener extends BaseListener {
        void onCollectionRename(@NonNull String newName);
    }


    private static final String ARG_COLLECTION_NAME = "CollectionName";
    private static final String ARG_IS_RENAME_DIALOG = "IsRenameDialog";
    private static final String BUNDLE_EDITED_NAME = "EditedName";
    private static final String BUNDLE_EDIT_SELECTION = "EditSelection";

    private CreateListener createListener;
    private RenameListener renameListener;

    private boolean isRename;
    private String oldName = "";
    private int oldSelection = -1;

    @BindView(R.id.collection_name_input_text) TextInputEditText editText;


    public void setDialogListener(CreateListener listener) {
        this.createListener = listener;
    }

    public void setDialogListener(RenameListener listener) {
        this.renameListener = listener;
    }

    public static CreateRenameCollectionDialogFragment getInstance(@NonNull String collectionName) {
        CreateRenameCollectionDialogFragment fragment = new CreateRenameCollectionDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_COLLECTION_NAME, collectionName);
        arguments.putBoolean(ARG_IS_RENAME_DIALOG, true);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static CreateRenameCollectionDialogFragment getInstance() {
        CreateRenameCollectionDialogFragment fragment = new CreateRenameCollectionDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(ARG_IS_RENAME_DIALOG, false);
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
            isRename = getArguments().getBoolean(ARG_IS_RENAME_DIALOG, false);
            if (isRename) {
                oldName = getArguments().getString(ARG_COLLECTION_NAME, "");
            }
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            oldName = savedInstanceState.getString(BUNDLE_EDITED_NAME, oldName);
            oldSelection = savedInstanceState.getInt(BUNDLE_EDIT_SELECTION, -1);
        }
        if (oldSelection == -1) {
            oldSelection = oldName.length();
        }
    }

    private Dialog buildDialog() {
        int positiveButtonRes = isRename ? R.string.rename : R.string.create;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setView(R.layout.dialog_collection_create_rename)
                .setPositiveButton(positiveButtonRes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = editText.getText().toString();
                        if (isRename) {
                            renameListener.onCollectionRename(newName);
                        } else {
                            createListener.onCollectionCreate(newName);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyCancel();
                        CreateRenameCollectionDialogFragment.this.getDialog().cancel();
                    }

                })
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getDialog());

        editText.setText(oldName);
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_EDIT_SELECTION, editText.getSelectionStart());
        outState.putString(BUNDLE_EDITED_NAME, editText.getText().toString());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        notifyCancel();
        super.onCancel(dialog);
    }

    private void notifyCancel() {
        if (isRename) {
            renameListener.onCancel();
        } else {
            createListener.onCancel();
        }
    }
}
