package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.aohayou.collector.BasePresenter;
import net.aohayou.collector.BaseView;
import net.aohayou.collector.data.Collection;

import java.util.List;

public interface CollectionsContract {

    interface View extends BaseView<Presenter> {

        void showCollections(@NonNull List<Collection> collections);

        void showCollectionDetails(@NonNull String collectionId);

        void showRenameDialog(@NonNull String collectionName);

        void showDeleteDialog();
    }

    interface Presenter extends BasePresenter {

        void loadCollections();

        void openCollectionDetails(@NonNull Collection collection);

        void addCollection(@NonNull String collectionName);

        void onRenameRequest(@NonNull Collection collection);

        void onRenameCancel();

        void onRename(@NonNull String newName);

        void onDeleteRequest(@NonNull Collection collection);

        void onDeleteCancel();

        void onDelete();

        void onSaveInstanceState(Bundle outState);
    }
}
