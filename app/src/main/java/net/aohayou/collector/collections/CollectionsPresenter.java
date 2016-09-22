package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.aohayou.collector.data.CollectorProtos.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private static final String BUNDLE_COLLECTION_TO_UPDATE_ID = "CollectionToUpdateId";

    private final CollectionsContract.View view;
    private final List<Collection> collections = new ArrayList<>();

    private String collectionToUpdateId;

    public CollectionsPresenter(@NonNull CollectionsContract.View view,
                                Bundle savedInstanceState) {
        this.view = view;
        view.setPresenter(this);

        if (savedInstanceState != null) {
            String collectionToUpdateId =
                    savedInstanceState.getString(BUNDLE_COLLECTION_TO_UPDATE_ID);
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
            collections.add(createCollection("Collection A"));
            collections.add(createCollection("Collection B"));
            collections.add(createCollection("Collection C"));
        }

        view.showCollections(collections);
    }

    private Collection createCollection(@NonNull String name) {
        String uuid = UUID.randomUUID().toString();
        return Collection.newBuilder()
                        .setId(uuid)
                        .setName(name)
                        .build();
    }

    @Override
    public void addCollection(@NonNull String collectionName) {
        Collection collection  = createCollection(collectionName);
        collections.add(collection);
        // Collections.sort(collections);  TODO create a CollectionComparator
        view.showCollections(collections);
    }

    @Override
    public void onRenameRequest(@NonNull Collection collection) {
        collectionToUpdateId = collection.getId();
        view.showRenameDialog(collection.getName());
    }

    @Override
    public void onRenameCancel() {
        collectionToUpdateId = null;
    }

    @Override
    public void onRename(@NonNull String newName) {
        Collection newCollection =
                Collection.newBuilder()
                        .setId(collectionToUpdateId)
                        .setName(newName)
                        .build();
        // collections.remove(collectionToUpdate); TODO
        collections.add(newCollection);
        // Collections.sort(collections);  TODO create a CollectionComparator
        collectionToUpdateId = null;

        view.showCollections(collections);
    }

    @Override
    public void onDeleteRequest(@NonNull Collection collection) {
        view.showDeleteDialog();
    }

    @Override
    public void onDeleteCancel() {
        collectionToUpdateId = null;
    }

    @Override
    public void onDelete() {
        // collections.remove(collectionToUpdate); TODO
        view.showCollections(collections);
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        if (collectionToUpdateId != null) {
            outBundle.putString(BUNDLE_COLLECTION_TO_UPDATE_ID, collectionToUpdateId);
        }
    }
}
