package net.aohayou.collector.collections;

import android.support.annotation.NonNull;

import net.aohayou.collector.model.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private final CollectionsContract.View view;
    private final List<Collection> collections = new ArrayList<>();

    public CollectionsPresenter(@NonNull CollectionsContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadCollections();
    }

    @Override
    public void loadCollections() {
        // tmp dataset
        if (collections.size() == 0) {
            collections.add(new Collection("Collection A"));
            collections.add(new Collection("Collection B"));
            collections.add(new Collection("Collection C"));
        }

        view.showCollections(collections);
    }

    @Override
    public void addCollection(@NonNull Collection collection) {
        collections.add(collection);
        Collections.sort(collections);
        view.showCollections(collections);
    }

    @Override
    public void onRename(@NonNull Collection collection) {
        view.showRenameDialog(collection);
    }

    @Override
    public void renameCollection(@NonNull Collection collection, String newName) {
        collections.remove(collection); //TODO fix the equals method which is based on the name
        collections.add(new Collection(newName));
        Collections.sort(collections);
        view.showCollections(collections);
    }

    @Override
    public void deleteCollection(@NonNull Collection collection) {
        collections.remove(collection); //TODO fix the equals method
        view.showCollections(collections);
    }
}
