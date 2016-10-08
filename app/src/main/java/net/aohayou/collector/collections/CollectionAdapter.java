package net.aohayou.collector.collections;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.google.common.base.Preconditions.*;

import net.aohayou.collector.R;
import net.aohayou.collector.data.Collection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display {@link Collection} items.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    public interface CollectionInteractionListener {
        void onClick(@NonNull Collection collection);
        void onRename(@NonNull Collection collection);
        void onDelete(@NonNull Collection collection);
    }

    private List<Collection> values;
    private CollectionInteractionListener listener;

    public CollectionAdapter(@NonNull List<Collection> items,
                             @NonNull CollectionInteractionListener listener) {
        setValues(items);
        this.listener = listener;
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

        Resources res = holder.view.getResources();
        int count = values.get(position).getFormula().getElementCount();
        String countString = res.getQuantityString(R.plurals.numberOfItems, count, count);
        holder.countView.setText(countString);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.collection_name) TextView nameView;
        @BindView(R.id.element_count) TextView countView;
        public Collection item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);

            view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    MenuInflater menuInflater = new MenuInflater(v.getContext());
                    menuInflater.inflate(R.menu.context_menu_collection, menu);
                    for (int i = 0; i < menu.size(); i++) {
                        MenuItem item = menu.getItem(i);
                        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_rename:
                                        listener.onRename(ViewHolder.this.item);
                                        return true;
                                    case R.id.action_delete:
                                        listener.onDelete(ViewHolder.this.item);
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(ViewHolder.this.item);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
