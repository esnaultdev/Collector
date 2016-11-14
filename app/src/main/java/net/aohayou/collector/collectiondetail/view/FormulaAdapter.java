package net.aohayou.collector.collectiondetail.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.aohayou.collector.R;
import net.aohayou.collector.data.formula.Formula;

/**
 * {@link RecyclerView.Adapter} that can display the elements of a {@link Formula}.
 */
public class FormulaAdapter extends RecyclerView.Adapter<FormulaAdapter.ViewHolder> {

    public interface Listener {
        void onElementClicked(int elementNumber, @NonNull FormulaElementView elementView);
    }

    private Formula formula;
    private Listener listener;

    private int elementCount = -1;

    public FormulaAdapter() {
        this(null, null);
    }

    public FormulaAdapter(@Nullable Formula formula, @Nullable Listener listener) {
        this.formula = formula;
        this.listener = listener;
    }

    public void replaceData(@Nullable Formula formula) {
        this.formula = formula;
        elementCount = -1;
        notifyDataSetChanged();
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FormulaElementView view = (FormulaElementView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.formula_element_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        boolean acquired = formula.hasElement(position + 1);

        holder.view.setAcquired(acquired);
    }

    @Override
    public int getItemCount() {
        if (formula == null) {
            return 0;
        }
        if (elementCount == -1) {
            // avoid getting the formula's last element multiple times
            elementCount = formula.getLastElement();
        }
        return elementCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final FormulaElementView view;

        public ViewHolder(FormulaElementView view) {
            super(view);
            this.view = view;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onElementClicked(getAdapterPosition(), ViewHolder.this.view);
                    }
                }
            });
        }
    }
}
