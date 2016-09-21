package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.aohayou.collector.model.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private static final String BUNDLE_COLLECTION_TO_UPDATE_NAME = "CollectionToUpdateName";

    private final CollectionsContract.View view;
    private final List<Collection> collections = new ArrayList<>();

    private Collection collectionToUpdate;

    public CollectionsPresenter(@NonNull CollectionsContract.View view,
                                Bundle savedInstanceState) {
        this.view = view;
        view.setPresenter(this);

        if (savedInstanceState != null) {
            String collectionToUpdateName =
                    savedInstanceState.getString(BUNDLE_COLLECTION_TO_UPDATE_NAME);
            if (collectionToUpdateName != null) {
                collectionToUpdate = new Collection(collectionToUpdateName);
            }
        }
    }

    @Override
    public void start() {
        loadCollections();
    }

    @Override
    public void loadCollections() {
        // tmp dataset
        if (collections.size() == 0) {
            collections.add(new Collection("Collection A"));
            collections.add(new Collection("Collection B"));
            collections.add(new Collection("Collection C"));
        }

        view.showCollections(collections);
    }

    @Override
    public void addCollection(@NonNull String collectionName) {
        Collection collection = new Collection(collectionName);
        collections.add(collection);
        Collections.sort(collections);
        view.showCollections(collections);
    }

    @Override
    public void onRenameRequest(@NonNull Collection collection) {
        collectionToUpdate = collection;
        view.showRenameDialog(collection.getName());
    }

    @Override
    public void onRenameCancel() {
        collectionToUpdate = null;
    }

    @Override
    public void onRename(@NonNull String newName) {
        collections.remove(collectionToUpdate);
        collections.add(new Collection(newName));
        Collections.sort(collections);
        view.showCollections(collections);
    }

    @Override
    public void onDeleteRequest(@NonNull Collection collection) {
        view.showDeleteDialog();
    }

    @Override
    public void onDeleteCancel() {
        collectionToUpdate = null;
    }

    @Override
    public void onDelete() {
        collections.remove(collectionToUpdate);
        view.showCollections(collections);
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        if (collectionToUpdate != null) {
            outBundle.putString(BUNDLE_COLLECTION_TO_UPDATE_NAME, collectionToUpdate.getName());
        }
    }
}
