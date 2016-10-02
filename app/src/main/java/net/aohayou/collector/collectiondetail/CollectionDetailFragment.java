package net.aohayou.collector.collectiondetail;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.aohayou.collector.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionDetailFragment extends Fragment implements CollectionDetailContract.View {

    @BindView(R.id.entry_count) TextView entryCountView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private CollectionDetailContract.Presenter presenter;

    public CollectionDetailFragment() {
        // Required empty public constructor
    }

    public static CollectionDetailFragment newInstance() {
        return new CollectionDetailFragment();
    }

    @Override
    public void setPresenter(CollectionDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection_detail, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());
    }

    @Override
    public void displayEntryCount(int entryCount) {
        Resources res = getResources();
        String itemsCount = res.getQuantityString(R.plurals.numberOfItems, entryCount, entryCount);
        if (toolbar != null) {
            toolbar.setSubtitle(itemsCount);
        }
    }

    @Override
    public void displayCollectionName(@NonNull String collectionName) {
        if (toolbar != null) {
            toolbar.setTitle(collectionName);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
