package net.aohayou.collector.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import net.aohayou.collector.data.Collection;
import net.aohayou.collector.data.CollectorProtos;

import static com.google.common.base.Preconditions.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileCollectionDataSource implements CollectionDataSource {

    private static final String TAG = "FileDataSource"; // Logging tag
    private static final String FILENAME  = "collection";

    private CollectorProtos.Library library;
    private final Context context;
    private boolean loaded = false;

    public FileCollectionDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void load() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            library = CollectorProtos.Library.parseFrom(fis);
        } catch (IOException e) {
            Log.w(TAG, "Could not parse " + FILENAME);
        }

        if (library == null) {
            library = CollectorProtos.Library.newBuilder().build();
        }

        loaded = true;
    }

    private boolean isDataLoaded() {
        return loaded;
    }

    @Override
    public void getCollection(@NonNull String collectionId,
                              @NonNull GetCollectionCallback callback) {
        checkState(isDataLoaded());

        CollectorProtos.Collection collectionProto = library.getCollectionsMap().get(collectionId);
        if (collectionProto == null) {
            callback.onDataNotAvailable();
        } else {
            Collection collection = Collection.fromProto(collectionProto);
            callback.onCollectionLoaded(collection);
        }
    }

    @Override
    public void getCollections(@NonNull GetCollectionsCallback callback) {
        checkState(isDataLoaded());

        List<Collection> collections = new ArrayList<>();
        List<CollectorProtos.Collection> collectionsProtos
                = new ArrayList<>(library.getCollectionsMap().values());
        //TODO avoid this loop
        for (CollectorProtos.Collection collectionProto : collectionsProtos) {
            Collection collection = Collection.fromProto(collectionProto);
            collections.add(collection);
        }

        if (collections.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onCollectionsLoaded(collections);
        }
    }

    @Override
    public void createCollection(@NonNull Collection collection) {
        checkState(isDataLoaded());

        library = CollectorProtos.Library.newBuilder(library)
                .putCollections(collection.getId(), collection.toProto())
                .build();
    }

    @Override
    public void renameCollection(@NonNull Collection collection, @NonNull String newName) {
        renameCollection(collection.toProto(), newName);
    }

    @Override
    public void renameCollection(@NonNull String collectionId, @NonNull String newName) {
        renameCollection(library.getCollectionsMap().get(collectionId), newName);
    }

    private void renameCollection(@NonNull CollectorProtos.Collection collection,
                                  @NonNull String newName) {
        checkState(isDataLoaded());

        CollectorProtos.Collection newCollection = CollectorProtos.Collection.newBuilder(collection)
                .setName(newName)
                .build();

        library = CollectorProtos.Library.newBuilder(library)
                .putCollections(collection.getId(), newCollection)
                .build();
    }

    @Override
    public void deleteCollection(@NonNull Collection collection) {
        checkState(isDataLoaded());
        deleteCollection(collection.getId());
    }

    @Override
    public void deleteCollection(@NonNull String collectionId) {
        checkState(isDataLoaded());

        library = CollectorProtos.Library.newBuilder(library)
                .removeCollections(collectionId)
                .build();
    }

    @Override
    public void apply() {
        FileOutputStream output = null;
        try {
            output = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            library.writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close " + FILENAME, e);
                }
            }
        }
    }
}
