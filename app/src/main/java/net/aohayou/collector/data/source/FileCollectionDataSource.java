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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileCollectionDataSource implements CollectionDataSource {

    private static final String TAG = "FileDataSource"; // Logging tag
    private static final String FILENAME  = "collection";

    private CollectorProtos.Library library;
    private Map<String, Collection> collections;

    private final Context context;
    private boolean loaded = false;

    public FileCollectionDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void load() {
        loadLibrary();
        convertCollections();
        loaded = true;
    }

    private void loadLibrary() {
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            library = CollectorProtos.Library.parseFrom(fis);
        } catch (IOException e) {
            Log.w(TAG, "Could not parse " + FILENAME);
        }

        if (library == null) {
            library = CollectorProtos.Library.newBuilder().build();
        }
    }

    private void convertCollections() {
        collections = new HashMap<>();
        List<CollectorProtos.Collection> collectionsProtos = library.getCollectionList();
        for (CollectorProtos.Collection collectionProto : collectionsProtos) {
            Collection collection = Collection.fromProto(collectionProto);
            collections.put(collection.getId(), collection);
        }
    }

    private boolean isDataLoaded() {
        return loaded;
    }

    @Override
    public void getCollection(@NonNull String collectionId,
                              @NonNull GetCollectionCallback callback) {
        checkState(isDataLoaded());

        Collection collection = collections.get(collectionId);
        if (collection == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onCollectionLoaded(collection);
        }
    }

    @Override
    public void getCollections(@NonNull GetCollectionsCallback callback) {
        checkState(isDataLoaded());

        List<Collection> collectionsList = new ArrayList<>(collections.values());
        if (collectionsList.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onCollectionsLoaded(collectionsList);
        }
    }

    @Override
    public void createCollection(@NonNull Collection collection) {
        checkState(isDataLoaded());

        collections.put(collection.getId(), collection);
        library = CollectorProtos.Library.newBuilder(library)
                .addCollection(collection.toProto())
                .build();
    }

    @Override
    public void saveCollection(@NonNull Collection collection) {
        collections.put(collection.getId(), collection);
        library = CollectorProtos.Library.newBuilder(library)
                .removeCollection(getCollectionIndexById(collection.getId()))
                .addCollection(collection.toProto())
                .build();
    }

    private int getCollectionIndexById(@NonNull String id) throws CollectionNotFoundException {
        int index = -1;
        for (int i = 0; i < library.getCollectionCount(); i++) {
            if (library.getCollection(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new CollectionNotFoundException(id);
        }
        return index;
    }

    @Override
    public void deleteCollection(@NonNull Collection collection) {
        deleteCollection(collection.getId());
    }

    @Override
    public void deleteCollection(@NonNull String collectionId) {
        checkState(isDataLoaded());

        collections.remove(collectionId);
        library = CollectorProtos.Library.newBuilder(library)
                .removeCollection(getCollectionIndexById(collectionId))
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
