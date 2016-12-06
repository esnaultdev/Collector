package net.aohayou.collector.collectiondetail;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import net.aohayou.collector.R;
import net.aohayou.collector.collectiondetail.view.FormulaAdapter;
import net.aohayou.collector.collectiondetail.view.FormulaElementView;
import net.aohayou.collector.collectiondetail.view.TooltipOverlay;
import net.aohayou.collector.data.formula.Formula;
import net.aohayou.collector.util.ColorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionDetailFragment extends Fragment implements CollectionDetailContract.View {

    private static final String TAG_EDIT_FORMULA = "editFormula";
    private static final int[] COLUMN_COUNTS = {5, 8, 10, 12, 15, 20, 25, 30};

    @BindView(R.id.toolbar) Toolbar toolbar;
    private FormulaAdapter formulaAdapter;
    private GridLayoutManager layoutManager;
    private TooltipOverlay tooltipOverlay;

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

        formulaAdapter = new FormulaAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_collection_detail, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        tooltipOverlay = (TooltipOverlay) view.findViewById(R.id.overlay);

        layoutManager = new GridLayoutManager(getContext(), COLUMN_COUNTS[0]);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(formulaAdapter);

        // Update the column count when the view has been measured
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int columnCount = getColumnCountMax(right - left);
                layoutManager.setSpanCount(columnCount);
                view.removeOnLayoutChangeListener(this);
            }
        });

        formulaAdapter.setListener(new FormulaAdapter.Listener() {
            @Override
            public void onElementClicked(int elementNumber,
                                         @NonNull FormulaElementView elementView) {
                @ColorRes int colorRes = elementView.isAcquired() ?
                        R.color.acquired_element_focus : R.color.missing_element_focus;
                @ColorInt int color = ColorUtil.getColor(getContext(), colorRes);
                tooltipOverlay.toggleTooltip(elementView, String.valueOf(elementNumber), color);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                tooltipOverlay.translate(dx, dy);
                tooltipOverlay.hide();
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        && recyclerView.findChildViewUnder(event.getX(), event.getY()) == null) {
                    // Touch outside items
                    tooltipOverlay.hide();
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();

        bindDialogs();
    }

    private void bindDialogs() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        EditFormulaDialogFragment creationFragment;
        creationFragment =
                (EditFormulaDialogFragment) fragmentManager.findFragmentByTag(TAG_EDIT_FORMULA);
        if (creationFragment != null) {
            creationFragment.setDialogListener(getCreationDialogListener());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());
    }

    @Override
    public void displayFormula(@NonNull Formula formula) {
        displayElementCount(formula.getElementCount());
        displayElements(formula);
    }

    private void displayElementCount(int count) {
        Resources res = getResources();
        String countString = res.getQuantityString(R.plurals.numberOfItems, count, count);
        if (toolbar != null) {
            toolbar.setSubtitle(countString);
        }
    }

    private void displayElements(@NonNull Formula formula) {
        formulaAdapter.replaceData(formula);
    }

    @Override
    public void displayCollectionName(@NonNull String collectionName) {
        if (toolbar != null) {
            toolbar.setTitle(collectionName);
        }
    }

    @Override
    public void showEditFormulaDialog(@NonNull String oldFormulaString) {
        EditFormulaDialogFragment editFormulaDialog;
        editFormulaDialog = EditFormulaDialogFragment.getInstance(oldFormulaString);
        editFormulaDialog.setDialogListener(getCreationDialogListener());
        editFormulaDialog.show(getActivity().getSupportFragmentManager(), TAG_EDIT_FORMULA);
    }

    @Override
    public void displayFormulaError(@NonNull String error) {
        Snackbar.make(getView(), error, Snackbar.LENGTH_LONG).show();
    }

    private EditFormulaDialogFragment.Listener getCreationDialogListener() {
        return new EditFormulaDialogFragment.Listener() {
            @Override
            public void onCancel() {
                presenter.onFormulaEditCancel();
            }

            @Override
            public void onFormulaSet(@NonNull String newFormulaString) {
                presenter.onFormulaEdit(newFormulaString);
            }
        };
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }



    /**
     * @return the biggest possible column count for elements fitting the fragment view.
     * @param maxWidth the width available to element views.
     */
    private int getColumnCountMax(int maxWidth) {
        Resources res = getContext().getResources();
        int elementWidth = (int) res.getDimension(R.dimen.formula_element_view_size);
        int elementMargin = (int) res.getDimension(R.dimen.formula_element_view_margin);
        int elementTotalWidth = elementWidth + 2*elementMargin;

        // We iterate from the biggest count towards the smallest until the views can fit
        for (int i = COLUMN_COUNTS.length - 1; i >= 0; i--) {
            if (COLUMN_COUNTS[i]*elementTotalWidth <= maxWidth) {
                return COLUMN_COUNTS[i];
            }
        }

        // If all column counts are too big, we return the smallest
        return COLUMN_COUNTS[0];
    }
}
