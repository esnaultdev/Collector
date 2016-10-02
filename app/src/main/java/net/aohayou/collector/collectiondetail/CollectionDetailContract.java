package net.aohayou.collector.collectiondetail;

import android.support.annotation.NonNull;

import net.aohayou.collector.BasePresenter;
import net.aohayou.collector.BaseView;

public interface CollectionDetailContract {
    interface View extends BaseView<Presenter> {

        void displayEntryCount(int entryCount);

        void displayCollectionName(@NonNull String collectionName);
    }

    interface Presenter extends BasePresenter {

        void loadCollection();
    }
}
