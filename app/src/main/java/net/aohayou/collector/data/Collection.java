package net.aohayou.collector.data;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Preconditions;

import net.aohayou.collector.data.formula.Formula;

import java.util.UUID;

public class Collection {

    private CollectorProtos.Collection protoCollection;

    // Private constructor to prevent direct instantiation
    private Collection() {}

    public static Collection createCollection(@NonNull String name) {
        String uuid = UUID.randomUUID().toString();
        CollectorProtos.Collection collectionProto = CollectorProtos.Collection.newBuilder()
                .setId(uuid)
                .setName(name)
                .build();
        return fromProto(collectionProto);
    }

    public static Collection fromProto(@NonNull CollectorProtos.Collection protoCollection) {
        Collection collection = new Collection();
        collection.protoCollection = Preconditions.checkNotNull(protoCollection);
        Preconditions.checkArgument(!TextUtils.isEmpty(protoCollection.getId()));
        Preconditions.checkArgument(!TextUtils.isEmpty(protoCollection.getName()));
        return collection;
    }

    public @NonNull String getId() {
       return protoCollection.getId();
    }

    public @NonNull String getName() {
        return protoCollection.getName();
    }

    public Collection setName(@NonNull String newName) {
        Preconditions.checkNotNull(newName);
        CollectorProtos.Collection newProto = protoCollection.toBuilder()
                .setName(newName)
                .build();
        return fromProto(newProto);
    }

    public Formula getFormula() {
        if (protoCollection.hasFormula()) {
            return Formula.fromProto(protoCollection.getFormula());
        } else {
            return Formula.emptyFormula();
        }
    }

    public Collection setFormula(@NonNull Formula newFormula) {
        Preconditions.checkNotNull(newFormula);
        CollectorProtos.Collection newProto = protoCollection.toBuilder()
                .setFormula(newFormula.toProto())
                .build();
        return fromProto(newProto);
    }

    public CollectorProtos.Collection toProto() {
        return protoCollection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collection that = (Collection) o;

        return getId().equals(that.getId());
    }
}
