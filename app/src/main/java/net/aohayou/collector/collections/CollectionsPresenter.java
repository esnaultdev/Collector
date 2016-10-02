package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.aohayou.collector.data.CollectorProtos.Collection;
import net.aohayou.collector.data.source.CollectionDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private static final String BUNDLE_INDEX_TO_UPDATE = "IndexToUpdate";

    private final CollectionsContract.View view;
    private final CollectionDataSource dataSource;

    private List<Collection> collections = new ArrayList<>(); // ordered list of collections
    private int indexToUpdate = -1;

    public CollectionsPresenter(@NonNull CollectionsContract.View view,
                                @NonNull CollectionDataSource dataSource,
                                Bundle savedInstanceState) {
        this.view = view;
        view.setPresenter(this);
        this.dataSource = dataSource;

        dataSource.load();

        loadSavedInstanceState(savedInstanceState);
    }

    @Override
    public void start() {
        loadCollections();
    }

    @Override
    public void loadCollections() {
        dataSource.getCollections(new CollectionDataSource.GetCollectionsCallback() {
            @Override
            public void onCollectionsLoaded(@NonNull List<Collection> collections) {
                CollectionsPresenter.this.collections = new ArrayList<>(collections);
                // TODO order the list
                view.showCollections(collections);
            }

            @Override
            public void onDataNotAvailable() {
                // TODO display an informative view
            }
        });
    }

    @Override
    public void openCollectionDetails(@NonNull Collection collection) {
        view.showCollectionDetails(collection.getId());
    }

    @Override
    public void addCollection(@NonNull String collectionName) {
        Collection collection  = createCollection(collectionName);
        dataSource.createCollection(collection);
        collections.add(collection); // TODO insert at the right position

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
    public void onRenameRequest(@NonNull Collection collection) {
        setCollectionToUpdate(collection);
        view.showRenameDialog(collection.getName());
    }

    @Override
    public void onRenameCancel() {
        resetCollectionToUpdate();
    }

    @Override
    public void onRename(@NonNull String newName) {
        Collection collection = collections.get(indexToUpdate);
        dataSource.renameCollection(collection, newName);

        collections.remove(indexToUpdate);
        Collection newCollection = Collection.newBuilder(collection)
                .setName(newName)
                .build();

        collections.add(newCollection);

        resetCollectionToUpdate();

        view.showCollections(collections);
    }

    @Override
    public void onDeleteRequest(@NonNull Collection collection) {
        setCollectionToUpdate(collection);
        view.showDeleteDialog();
    }

    @Override
    public void onDeleteCancel() {
        resetCollectionToUpdate();
    }

    @Override
    public void onDelete() {
        Collection collection = collections.remove(indexToUpdate);
        dataSource.deleteCollection(collection);
        resetCollectionToUpdate();

        view.showCollections(collections);
    }

    private void setCollectionToUpdate(@NonNull Collection collection) {
        // FIXME Use the collection id as a fallback if the list has been updated. This should
        // only occur after a network update.
        indexToUpdate = collections.indexOf(collection);
    }

    private void resetCollectionToUpdate() {
        indexToUpdate = -1;
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        if (indexToUpdate != -1) {
            outBundle.putInt(BUNDLE_INDEX_TO_UPDATE, indexToUpdate);
        }
        dataSource.apply(); //FIXME should it be somewhere else?
    }

    private void loadSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            indexToUpdate = savedInstanceState.getInt(BUNDLE_INDEX_TO_UPDATE);
        }
    }
}
