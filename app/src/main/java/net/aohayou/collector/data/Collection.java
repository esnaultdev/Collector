package net.aohayou.collector.data;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Preconditions;

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

    public CollectorProtos.Collection toProto() {
        return protoCollection;
    }

    public Formula getFormula() {
        if (protoCollection.hasFormula()) {
            return Formula.fromProto(protoCollection.getFormula());
        } else {
            return Formula.emptyFormula();
        }
    }

    public static class Formula {

        private String formulaString;
        private int elementCount;

        private static Formula fromProto(CollectorProtos.Collection.Formula protoFormula) {
            Formula formula = new Formula();
            formula.formulaString = protoFormula.getFormulaString();
            formula.elementCount = protoFormula.getElementCount();
            return formula;
        }

        private static Formula emptyFormula() {
            Formula formula = new Formula();
            formula.formulaString = "";
            formula.elementCount = 0;
            return formula;
        }

        public int getElementCount() {
            return elementCount;
        }

        public boolean hasElement(int elementNumber) {
            //TODO parse formula
            return false;
        }

        public String getFormulaString() {
            return formulaString;
        }
    }
}
