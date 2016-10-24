package net.aohayou.collector.collectiondetail.view;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.aohayou.collector.R;
import net.aohayou.collector.data.formula.Formula;

/**
 * {@link RecyclerView.Adapter} that can display the elements of a {@link Formula}.
 */
public class FormulaAdapter extends RecyclerView.Adapter<FormulaAdapter.ViewHolder> {

    private Formula formula;
    private int rowCount = -1;
    private int columns = 8;

    public FormulaAdapter() {
        this.formula = null;
    }

    public FormulaAdapter(@Nullable Formula formula) {
        this.formula = formula;
    }

    public void replaceData(@Nullable Formula formula) {
        this.formula = formula;
        rowCount = -1;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate view
        FormulaRowView view = (FormulaRowView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.formula_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // position = row number
        int displayCount = columns;
        if (position == getItemCount() - 1) {
            //TODO check the displayCount
        }
        boolean acquired[] = new boolean[displayCount];
        for (int i = 0; i < displayCount; i++) {
            acquired[i] = formula.hasElement(position*columns + i + 1);
        }
        FormulaRowView.RowInfo info = new FormulaRowView.RowInfo(columns, displayCount, acquired);

        holder.info = info;
        holder.view.setRowInfo(info);
    }

    @Override
    public int getItemCount() {
        if (formula == null) {
            return 0;
        }
        if (rowCount == -1) {
            rowCount = (formula.getLastElement() / columns) + 1;
        }
        return rowCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final FormulaRowView view;
        public FormulaRowView.RowInfo info;

        public ViewHolder(FormulaRowView view) {
            super(view);
            this.view = view;
        }
    }
}
