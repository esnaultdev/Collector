package net.aohayou.collector.collectiondetail;

import android.support.annotation.NonNull;

import net.aohayou.collector.data.CollectorProtos;
import net.aohayou.collector.data.source.CollectionDataSource;

public class CollectionDetailPresenter implements CollectionDetailContract.Presenter {

    private CollectionDetailContract.View view;
    private CollectionDataSource dataSource;
    private String collectionId;

    public CollectionDetailPresenter(@NonNull CollectionDetailContract.View view,
                                     @NonNull CollectionDataSource dataSource,
                                     @NonNull String collectionId) {
        this.view = view;
        view.setPresenter(this);
        this.collectionId  = collectionId;
        this.dataSource = dataSource;
    }

    @Override
    public void start() {
        loadCollection();
    }

    @Override
    public void loadCollection() {
        dataSource.load();
        dataSource.getCollection(collectionId, new CollectionDataSource.GetCollectionCallback() {
            @Override
            public void onCollectionLoaded(@NonNull CollectorProtos.Collection collection) {
                view.displayEntryCount(collection.getEntryCount());
                view.displayCollectionName(collection.getName());
            }

            @Override
            public void onDataNotAvailable() {
                //TODO display no data
            }
        });
    }
}
