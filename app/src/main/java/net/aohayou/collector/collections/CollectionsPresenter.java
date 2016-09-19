package net.aohayou.collector.collections;

import android.support.annotation.NonNull;

import net.aohayou.collector.model.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private final CollectionsContract.View view;

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
        List<Collection> collections = new ArrayList<>();
        collections.add(new Collection("Collection A"));
        collections.add(new Collection("Collection B"));
        collections.add(new Collection("Collection C"));
        view.showCollections(collections);
    }
}
