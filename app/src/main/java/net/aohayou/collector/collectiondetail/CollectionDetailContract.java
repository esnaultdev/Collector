package net.aohayou.collector.collectiondetail;

import android.support.annotation.NonNull;

import net.aohayou.collector.BasePresenter;
import net.aohayou.collector.BaseView;
import net.aohayou.collector.data.formula.Formula;

public interface CollectionDetailContract {
    interface View extends BaseView<Presenter> {

        void displayFormula(@NonNull Formula formula);

        void displayCollectionName(@NonNull String collectionName);

        void showEditFormulaDialog(@NonNull String oldFormulaString);
    }

    interface Presenter extends BasePresenter {

        void loadCollection();

        void onFormulaEditRequest();

        void onFormulaEditCancel();

        void onFormulaEdit(@NonNull String newFormulaString);

        void onSaveData();
    }
}
