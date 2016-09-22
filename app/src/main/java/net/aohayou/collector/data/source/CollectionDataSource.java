package net.aohayou.collector.data.source;

import android.support.annotation.NonNull;

import net.aohayou.collector.data.CollectorProtos.Collection;

import java.util.List;

public interface CollectionDataSource {

    interface GetCollectionCallback {

        void onCollectionLoaded(@NonNull Collection collection);

        void onDataNotAvailable();
    }

    interface GetCollectionsCallback {

        void onCollectionsLoaded(@NonNull List<Collection> tasks);

        void onDataNotAvailable();
    }

    void createCollection(@NonNull Collection collection);

    void getCollection(@NonNull String collectionId, @NonNull GetCollectionCallback callback);

    void getCollections(@NonNull GetCollectionsCallback callback);

    void renameCollection(@NonNull Collection collection, @NonNull String newName);

    void renameCollection(@NonNull String collectionId, @NonNull String newName);

    void deleteCollection(@NonNull Collection collection);

    void deleteCollection(@NonNull String collectionId);
}
