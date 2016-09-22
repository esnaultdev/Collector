package net.aohayou.collector.collections;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.aohayou.collector.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RenameCollectionDialogFragment extends DialogFragment {

    public interface Listener {
        void onCollectionRenamed(@NonNull String newName, @NonNull String oldName);
        void onCancel();
    }

    private static final String COLLECTION_NAME_ARG = "CollectionName";

    private Listener listener;
    private String oldName = "";

    @BindView(R.id.collection_name_input_text)
    TextInputEditText editText;

    public void setDialogListener(Listener listener) {
        this.listener = listener;
    }

    public static RenameCollectionDialogFragment createInstance(@NonNull String collectionName) {
        RenameCollectionDialogFragment fragment = new RenameCollectionDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(COLLECTION_NAME_ARG, collectionName);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            oldName = getArguments().getString(COLLECTION_NAME_ARG, "");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Dialog dialog = builder.setView(R.layout.dialog_collection_create_rename)
                .setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = editText.getText().toString();
                        listener.onCollectionRenamed(newName, oldName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancel();
                        RenameCollectionDialogFragment.this.getDialog().cancel();
                    }
                })
                .create();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getDialog());
        editText.setText(oldName);
        editText.setSelection(oldName.length());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onCancel();
        super.onCancel(dialog);
    }
}
