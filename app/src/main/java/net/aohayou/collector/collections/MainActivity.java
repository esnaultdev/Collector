package net.aohayou.collector.collections;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.aohayou.collector.R;
import net.aohayou.collector.model.Collection;
import net.aohayou.collector.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;

    private CollectionsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Setup the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        CollectionFragment collectionsFragment =
                (CollectionFragment) fragmentManager.findFragmentById(R.id.contentFrame);
        if (collectionsFragment == null) {
            collectionsFragment = CollectionFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    fragmentManager, collectionsFragment, R.id.contentFrame);
        }

        // Setup the presenter
        presenter = new CollectionsPresenter(collectionsFragment);

        //TODO should be in the view
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionCreationDialogFragment creationDialog;
                creationDialog = new CollectionCreationDialogFragment();
                creationDialog.setDialogListener(new CollectionCreationDialogFragment.Listener() {
                    @Override
                    public void onCollectionCreate(@NonNull String collectionName) {
                        if (!TextUtils.isEmpty(collectionName)) {
                            presenter.addCollection(new Collection(collectionName));
                        }
                    }

                    @Override
                    public void onCancel() {
                        // Do nothing
                    }
                });
                creationDialog.show(getSupportFragmentManager(), "creation");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
