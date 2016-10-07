package net.aohayou.collector.collectiondetail;

import android.support.annotation.NonNull;

import net.aohayou.collector.data.Collection;
import net.aohayou.collector.data.formula.Formula;
import net.aohayou.collector.data.source.CollectionDataSource;

import java.util.Date;

public class CollectionDetailPresenter implements CollectionDetailContract.Presenter {

    private CollectionDetailContract.View view;
    private CollectionDataSource dataSource;
    private String collectionId;

    private Collection collection;

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
            public void onCollectionLoaded(@NonNull Collection collection) {
                CollectionDetailPresenter.this.collection = collection;
                view.displayCollectionName(collection.getName());
                view.displayFormula(collection.getFormula());
            }

            @Override
            public void onDataNotAvailable() {
                //TODO display no data
            }
        });
    }

    @Override
    public void onFormulaEditRequest() {
        view.showEditFormulaDialog(collection.getFormula().getFormulaString());
    }

    @Override
    public void onFormulaEditCancel() {
        // Do nothing
    }

    @Override
    public void onFormulaEdit(@NonNull String newFormulaString) {
        Formula formula = new Formula(newFormulaString, new Date().getTime());
        collection = collection.setFormula(formula);
        dataSource.saveCollection(collection);
        view.displayFormula(formula);
    }

    @Override
    public void onSaveData() {
        dataSource.apply();
    }
}
