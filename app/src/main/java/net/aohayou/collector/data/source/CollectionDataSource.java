package net.aohayou.collector.data.source;

import android.support.annotation.NonNull;

import net.aohayou.collector.data.Collection;

import java.util.List;

public interface CollectionDataSource {

    void load();

    interface GetCollectionCallback {

        void onCollectionLoaded(@NonNull Collection collection);

        void onDataNotAvailable();
    }

    interface GetCollectionsCallback {

        void onCollectionsLoaded(@NonNull List<Collection> collections);

        void onDataNotAvailable();
    }

    void getCollection(@NonNull String collectionId, @NonNull GetCollectionCallback callback);

    void getCollections(@NonNull GetCollectionsCallback callback);

    void createCollection(@NonNull Collection collection);

    void saveCollection(@NonNull Collection collection);

    void deleteCollection(@NonNull Collection collection);

    void deleteCollection(@NonNull String collectionId);

    void apply();
}
