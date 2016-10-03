package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.aohayou.collector.data.Collection;
import net.aohayou.collector.data.source.CollectionDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private static final String BUNDLE_INDEX_TO_UPDATE = "IndexToUpdate";

    private final CollectionsContract.View view;
    private final CollectionDataSource dataSource;

    private List<Collection> collections = new ArrayList<>(); // ordered list of collections
    private Comparator<Collection> comparator;
    private int indexToUpdate = -1;

    public CollectionsPresenter(@NonNull CollectionsContract.View view,
                                @NonNull CollectionDataSource dataSource,
                                Bundle savedState) {
        this.view = view;
        view.setPresenter(this);
        this.dataSource = dataSource;

        dataSource.load();
        loadSavedState(savedState);

        comparator = new Comparator<Collection>() {
            @Override
            public int compare(Collection c1, Collection c2) {
                return c1.getName().compareTo(c2.getName());
            }
        };
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
                Collections.sort(CollectionsPresenter.this.collections, comparator);
                view.bindCollections(CollectionsPresenter.this.collections);
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
        Collection collection  = Collection.createCollection(collectionName);
        dataSource.createCollection(collection);
        int newIndex = insertInOrder(collection);

        view.displayCollectionAdded(newIndex);
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
        view.displayCollectionRemoved(indexToUpdate);

        int newIndex = insertInOrder(collection.setName(newName));
        view.displayCollectionAdded(newIndex);

        resetCollectionToUpdate();
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
        view.displayCollectionRemoved(indexToUpdate);
        resetCollectionToUpdate();
    }

    private int insertInOrder(@NonNull Collection collection) {
        ListIterator<Collection> it = collections.listIterator();
        while (it.hasNext()) {
            Collection current = it.next();
            if (comparator.compare(collection, current) < 0) {
                it.previous();
                break;
            }
        }
        it.add(collection);
        return it.previousIndex();
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
    public void onSaveState(Bundle outBundle) {
        if (indexToUpdate != -1) {
            outBundle.putInt(BUNDLE_INDEX_TO_UPDATE, indexToUpdate);
        }
    }

    private void loadSavedState(Bundle savedState) {
        if (savedState != null) {
            indexToUpdate = savedState.getInt(BUNDLE_INDEX_TO_UPDATE);
        }
    }

    @Override
    public void onSaveData() {
        dataSource.apply();
    }
}
