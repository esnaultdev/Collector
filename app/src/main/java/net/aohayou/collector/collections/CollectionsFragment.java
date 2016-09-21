package net.aohayou.collector.collections;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.aohayou.collector.model.Collection;
import net.aohayou.collector.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Collection items.
 */
public class CollectionsFragment extends Fragment implements CollectionsContract.View {

    private static final String TAG_RENAME_COLLECTION = "renameCollection";
    private static final String TAG_DELETE_COLLECTION = "deleteCollection";

    private CollectionsContract.Presenter presenter;
    private CollectionAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CollectionsFragment() {
    }

    public static CollectionsFragment newInstance() {
        return new CollectionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CollectionAdapter(new ArrayList<Collection>(),
                new CollectionAdapter.CollectionInteractionListener() {
            @Override
            public void onRename(@NonNull Collection collection) {
                presenter.onRenameRequest(collection);
            }

            @Override
            public void onDelete(@NonNull Collection collection) {
                presenter.onDeleteRequest(collection);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void setPresenter(CollectionsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();

        bindDialogs();
    }

    private void bindDialogs() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        RenameCollectionDialogFragment renameDialog;
        renameDialog = (RenameCollectionDialogFragment) fragmentManager
                .findFragmentByTag(TAG_RENAME_COLLECTION);
        if (renameDialog != null) {
            //FIXME bad collection parameter
            renameDialog.setDialogListener(getRenameDialogListener(new Collection("")));
        }
        DeleteCollectionDialogFragment deleteDialog;
        deleteDialog = (DeleteCollectionDialogFragment) fragmentManager
                .findFragmentByTag(TAG_DELETE_COLLECTION);
        if (deleteDialog != null) {
            //FIXME bad collection parameter
            deleteDialog.setListener(getDeleteDialogListener(new Collection("")));
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showCollections(@NonNull List<Collection> collections) {
        adapter.replaceData(collections);
    }

    @Override
    public void showRenameDialog(@NonNull Collection collection) {
        RenameCollectionDialogFragment renameDialog;
        renameDialog = RenameCollectionDialogFragment.createInstance(collection.getName());
        renameDialog.setDialogListener(getRenameDialogListener(collection));
        renameDialog.show(getActivity().getSupportFragmentManager(), TAG_RENAME_COLLECTION);
    }

    @NonNull
    private RenameCollectionDialogFragment.Listener
    getRenameDialogListener(@NonNull final Collection collection) {
        return new RenameCollectionDialogFragment.Listener() {
            @Override
            public void onCollectionRenamed(@NonNull String newName, @NonNull String oldName) {
                presenter.renameCollection(collection, newName);
            }

            @Override
            public void onCancel() {
                // Do nothing
            }
        };
    }

    @Override
    public void showDeleteDialog(@NonNull Collection collection) {
        DeleteCollectionDialogFragment deleteFragment;
        deleteFragment = new DeleteCollectionDialogFragment();
        deleteFragment.setListener(getDeleteDialogListener(collection));
        deleteFragment.show(getActivity().getSupportFragmentManager(), TAG_DELETE_COLLECTION);
    }

    @NonNull
    private DeleteCollectionDialogFragment.Listener
    getDeleteDialogListener(@NonNull final Collection collection) {
        return new DeleteCollectionDialogFragment.Listener() {
            @Override
            public void onConfirm() {
                presenter.deleteCollection(collection);
            }

            @Override
            public void onCancel() {
                // Do nothing
            }
        };
    }
}
