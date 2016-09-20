package net.aohayou.collector.collections;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.aohayou.collector.R;

public class CollectionCreationDialogFragment extends DialogFragment {

    public interface Listener {
        void onCollectionCreate(@NonNull String collectionName);
        void onCancel();
    }

    private Listener listener;

    private TextInputEditText editText;

    public void setDialogListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Dialog dialog = builder.setTitle(R.string.collection_creation_title)
                .setView(R.layout.dialog_collection_creation)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCollectionCreate(editText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancel();
                        CollectionCreationDialogFragment.this.getDialog().cancel();
                    }
                })
                .create();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        editText = (TextInputEditText) getDialog().findViewById(R.id.collection_name_input_text);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onCancel();
        super.onCancel(dialog);
    }
}
