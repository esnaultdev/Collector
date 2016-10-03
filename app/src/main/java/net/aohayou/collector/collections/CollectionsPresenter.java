package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.protobuf.InvalidProtocolBufferException;

import net.aohayou.collector.data.Collection;
import net.aohayou.collector.data.CollectorProtos;
import net.aohayou.collector.data.source.CollectionDataSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private static final String TAG = "CollectionPresenter";
    private static final String BUNDLE_COLLECTION_TO_UPDATE = "CollectionToUpdate";

    private final CollectionsContract.View view;
    private final CollectionDataSource dataSource;

    private TreeSet<Collection> collections;
    private Collection collectionToUpdate = null;

    public CollectionsPresenter(@NonNull CollectionsContract.View view,
                                @NonNull CollectionDataSource dataSource,
                                Bundle savedState) {
        this.view = view;
        view.setPresenter(this);
        this.dataSource = dataSource;

        dataSource.load();

        loadSavedState(savedState);

        collections = new TreeSet<>(new Comparator<Collection>() {
            @Override
            public int compare(Collection c1, Collection c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
    }

    @Override
    public void start() {
        loadCollections();
    }

    @Override
    public void loadCollections() {
        collections.clear();
        dataSource.getCollections(new CollectionDataSource.GetCollectionsCallback() {
            @Override
            public void onCollectionsLoaded(@NonNull List<Collection> collections) {
                for (Collection collection : collections) {
                    CollectionsPresenter.this.collections.add(collection);
                }
                showCollections();
            }

            @Override
            public void onDataNotAvailable() {
                // TODO display an informative view
            }
        });
    }

    private void showCollections() {
        view.showCollections(new ArrayList<>(collections));
    }

    @Override
    public void openCollectionDetails(@NonNull Collection collection) {
        view.showCollectionDetails(collection.getId());
    }

    @Override
    public void addCollection(@NonNull String collectionName) {
        Collection collection = Collection.createCollection(collectionName);
        dataSource.createCollection(collection);
        collections.add(collection);
        showCollections();
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
        Preconditions.checkNotNull(collectionToUpdate);

        collections.remove(collectionToUpdate);
        dataSource.renameCollection(collectionToUpdate, newName);
        collections.add(collectionToUpdate.setName(newName));
        collectionToUpdate = null;
        showCollections();
    }

    @Override
    public void onDeleteRequest(@NonNull Collection collection) {
        collectionToUpdate = collection;
        view.showDeleteDialog();
    }

    @Override
    public void onDeleteCancel() {
        collectionToUpdate = null;
    }

    @Override
    public void onDelete() {
        Preconditions.checkNotNull(collectionToUpdate);

        collections.remove(collectionToUpdate);
        dataSource.deleteCollection(collectionToUpdate);
        collectionToUpdate = null;

        showCollections();
    }

    @Override
    public void onSaveState(Bundle outBundle) {
        if (collectionToUpdate != null) {
            outBundle.putByteArray(
                    BUNDLE_COLLECTION_TO_UPDATE, collectionToUpdate.toProto().toByteArray());
        }
    }

    private void loadSavedState(Bundle savedState) {
        if (savedState != null) {
            byte[] byteArray = savedState.getByteArray(BUNDLE_COLLECTION_TO_UPDATE);
            if (byteArray != null) {
                try {
                    CollectorProtos.Collection collectionProto =
                            CollectorProtos.Collection.parseFrom(byteArray);
                    collectionToUpdate = Collection.fromProto(collectionProto);
                } catch (InvalidProtocolBufferException e) {
                    Log.e(TAG, "Invalid saved collection:" + e);
                }
            }
        }
    }

    @Override
    public void onSaveData() {
        dataSource.apply();
    }
}
