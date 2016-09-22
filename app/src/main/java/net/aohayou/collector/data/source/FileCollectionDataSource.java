package net.aohayou.collector.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.google.common.base.Preconditions.*;

import net.aohayou.collector.data.CollectorProtos.Collection;
import net.aohayou.collector.data.CollectorProtos.Library;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileCollectionDataSource implements CollectionDataSource {

    private static final String TAG = "FileDataSource"; // Logging tag
    private static final String FILENAME  = "collection";

    private Library library;
    private final Context context;
    private boolean loaded = false;

    public FileCollectionDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void load() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            library = Library.parseFrom(fis);
        } catch (IOException e) {
            Log.w(TAG, "Could not parse " + FILENAME);
        }

        if (library == null) {
            library = Library.newBuilder().build();
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

        Collection collection = library.getCollectionsMap().get(collectionId);
        if (collection == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onCollectionLoaded(collection);
        }
    }

    @Override
    public void getCollections(@NonNull GetCollectionsCallback callback) {
        checkState(isDataLoaded());

        List<Collection> collections = new ArrayList<>(library.getCollectionsMap().values());
        if (collections.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onCollectionsLoaded(collections);
        }
    }

    @Override
    public void createCollection(@NonNull Collection collection) {
        checkState(isDataLoaded());

        library = Library.newBuilder(library)
                .putCollections(collection.getId(), collection)
                .build();
    }

    @Override
    public void renameCollection(@NonNull Collection collection, @NonNull String newName) {
        checkState(isDataLoaded());

        Collection newCollection = Collection.newBuilder(collection)
                .setName(newName)
                .build();

        library = Library.newBuilder(library)
                .putCollections(collection.getId(), newCollection)
                .build();
    }

    @Override
    public void renameCollection(@NonNull String collectionId, @NonNull String newName) {
        checkState(isDataLoaded());
        renameCollection(library.getCollectionsMap().get(collectionId), newName);
    }

    @Override
    public void deleteCollection(@NonNull Collection collection) {
        checkState(isDataLoaded());
        deleteCollection(collection.getId());
    }

    @Override
    public void deleteCollection(@NonNull String collectionId) {
        checkState(isDataLoaded());

        library = Library.newBuilder(library)
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
