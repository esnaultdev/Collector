package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.aohayou.collector.BasePresenter;
import net.aohayou.collector.BaseView;
import net.aohayou.collector.data.Collection;

import java.util.List;

public interface CollectionsContract {

    interface View extends BaseView<Presenter> {

        void bindCollections(@NonNull List<Collection> collections);

        void displayCollectionAdded(int index);

        void displayCollectionRemoved(int index);

        void showCollectionDetails(@NonNull String collectionId);

        void showCreateDialog();

        void showRenameDialog(@NonNull String collectionName);

        void showDeleteDialog();
    }

    interface Presenter extends BasePresenter {

        void loadCollections();

        void openCollectionDetails(@NonNull Collection collection);

        void onCreateRequest();

        void onCreate(@NonNull String collectionName);

        void onCreateCancel();

        void onRenameRequest(@NonNull Collection collection);

        void onRenameCancel();

        void onRename(@NonNull String newName);

        void onDeleteRequest(@NonNull Collection collection);

        void onDeleteCancel();

        void onDelete();

        void onSaveState(Bundle outState);

        void onSaveData();
    }
}
