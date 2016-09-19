package net.aohayou.collector.collections;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.google.common.base.Preconditions.*;

import net.aohayou.collector.model.Collection;
import net.aohayou.collector.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display {@link Collection} items.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> values;

    public CollectionAdapter(@NonNull List<Collection> items) {
        setValues(items);
    }

    public void replaceData(@NonNull List<Collection> items) {
        setValues(items);
        notifyDataSetChanged();
    }

    private void setValues(@NonNull List<Collection> items) {
        values = checkNotNull(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = values.get(position);
        holder.nameView.setText(values.get(position).getName());

        //TODO setup listeners
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView nameView;
        public Collection item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            nameView = (TextView) view.findViewById(R.id.id);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
