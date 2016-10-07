package net.aohayou.collector.collectiondetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.google.common.base.Preconditions;

import net.aohayou.collector.R;
import net.aohayou.collector.data.source.FileCollectionDataSource;
import net.aohayou.collector.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionDetailActivity extends AppCompatActivity {

    private static final String EXTRA_COLLECTION_ID = "CollectionId";

    private String collectionId;
    private CollectionDetailContract.Presenter presenter;

    @BindView(R.id.fab) FloatingActionButton fab;

    public static void setupIntent(@NonNull Intent intent, @NonNull String collectionId) {
        Preconditions.checkArgument(!TextUtils.isEmpty(collectionId));
        Preconditions.checkNotNull(intent);
        intent.putExtra(EXTRA_COLLECTION_ID, collectionId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindArguments();

        setContentView(R.layout.activity_collection_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        // Setup the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        CollectionDetailFragment collectionsFragment =
                (CollectionDetailFragment) fragmentManager.findFragmentById(R.id.contentFrame);
        if (collectionsFragment == null) {
            collectionsFragment = CollectionDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    fragmentManager, collectionsFragment, R.id.contentFrame);
        }

        // Setup the presenter
        presenter = new CollectionDetailPresenter(collectionsFragment,
                new FileCollectionDataSource(this), collectionId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFormulaEditRequest();
            }
        });
    }

    private void bindArguments() {
        collectionId = Preconditions.checkNotNull(getIntent().getStringExtra(EXTRA_COLLECTION_ID));
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onSaveData();
    }
}
