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

public class CreateCollectionDialogFragment extends DialogFragment {

    public interface Listener {
        void onCollectionCreate(@NonNull String collectionName);
        void onCancel();
    }

    private Listener listener;

    @BindView(R.id.collection_name_input_text) TextInputEditText editText;

    public void setDialogListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Dialog dialog = builder.setView(R.layout.dialog_collection_create_rename)
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
                        CreateCollectionDialogFragment.this.getDialog().cancel();
                    }

                })
                .create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getDialog());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onCancel();
        super.onCancel(dialog);
    }
}
