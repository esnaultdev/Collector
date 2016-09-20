package net.aohayou.collector.collections;

import android.support.annotation.NonNull;

import net.aohayou.collector.BasePresenter;
import net.aohayou.collector.BaseView;
import net.aohayou.collector.model.Collection;

import java.util.List;

public interface CollectionsContract {

    interface View extends BaseView<Presenter> {

        void showCollections(@NonNull List<Collection> collections);

        void showRenameDialog(@NonNull Collection collection);
    }

    interface Presenter extends BasePresenter {

        void loadCollections();

        // void openCollectionDetails(@NonNull Collection collection);

        void addCollection(@NonNull Collection collection);

        void onRename(@NonNull Collection collection);

        void renameCollection(@NonNull Collection collection, String newName);
    }
}
