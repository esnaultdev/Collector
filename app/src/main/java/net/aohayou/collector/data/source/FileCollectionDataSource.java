package net.aohayou.collector.data.source;

import android.support.annotation.NonNull;

import net.aohayou.collector.data.CollectorProtos.Collection;

public class FileCollectionDataSource implements CollectionDataSource {
    @Override
    public void createCollection(@NonNull Collection collection) {

    }

    @Override
    public void getCollection(@NonNull String collectionId,
                              @NonNull GetCollectionCallback callback) {

    }

    @Override
    public void getCollections(@NonNull GetCollectionsCallback callback) {

    }

    @Override
    public void renameCollection(@NonNull Collection collection, @NonNull String newName) {

    }

    @Override
    public void renameCollection(@NonNull String collectionId, @NonNull String newName) {

    }

    @Override
    public void deleteCollection(@NonNull Collection collection) {

    }

    @Override
    public void deleteCollection(@NonNull String collectionId) {

    }
}
