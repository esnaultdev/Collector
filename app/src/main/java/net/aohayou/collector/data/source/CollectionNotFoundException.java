package net.aohayou.collector.data.source;

import android.support.annotation.NonNull;

public class CollectionNotFoundException extends RuntimeException {

    public CollectionNotFoundException(@NonNull String collectionId) {
        super("Collection not found: " + collectionId);
    }
}
