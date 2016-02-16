package com.example.tomerbuzaglo.xmlrssdemo.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomerbuzaglo.xmlrssdemo.R;
import com.example.tomerbuzaglo.xmlrssdemo.model.Item;
import com.example.tomerbuzaglo.xmlrssdemo.model.Rss;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class YnetRecyclerViewAdapter extends RecyclerView.Adapter<YnetRecyclerViewAdapter.YnetViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Item> adapterLocalItems;
    private final List<Item> originalItems;
    private Context context;
    private RecyclerView recyclerView;

    public YnetRecyclerViewAdapter(Rss rss, Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.originalItems = rss.getChannel().getItems();
        this.adapterLocalItems = (Rss.fromJson(rss.json())).getChannel().getItems(); //copy using json serialization and deserialization. this can be more efficiently done with Parcelable interface, but will add tons of code.
    }


    @Override
    public YnetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.rv_item_ynet, parent, false);
        this.recyclerView = (RecyclerView) parent;
        return new YnetViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return adapterLocalItems.size();
    }

    @Override
    public void onBindViewHolder(YnetViewHolder holder, int position) {
        Item item = adapterLocalItems.get(position);
        item.extractDescription();


        holder.tvContent.setText(item.getTitle());
        Picasso.with(context).load(item.getImage()).error(R.drawable.ic_failed).placeholder(R.drawable.ic_placeholder).into(holder.itemImage);
    }

    /**
     * Add an Item to adapterLocalItems and notify the adapter
     */
    private void addItem(int position, Item item) {
        adapterLocalItems.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * remove Item from adapterLocalItems and notify the adapter
     */
    private Item removeItem(int position) {
        final Item item = adapterLocalItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    /**
     * move Item inside adapterLocalItems and notify the adapter
     */
    private void moveItem(int fromPosition, int toPosition) {
        //remove from position
        final Item item = adapterLocalItems.remove(fromPosition);
        ///add to position
        adapterLocalItems.add(toPosition, item);
        //notify the adapter
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Iterate to compare localAdapterItems with filteredItems - for each item that needs to be removed - call the {@link #removeItem(int)}  method.
     *
     * @param filteredItems The Filtered Items used to compare with adapterLocalItems in order to sync the localAdapterItems
     */
    private void applyAndAnimateRemovals(List<Item> filteredItems) {
        for (int i = adapterLocalItems.size() - 1; i >= 0; i--) {
            final Item item = adapterLocalItems.get(i);
            if (!filteredItems.contains(item)) {
                removeItem(i);
            }
        }
    }

    /**
     * Iterate to compare localAdapterItems with filteredList - for each item that needs to be Added - call the {@link #addItem(int, Item)} method.
     *
     * @param filteredList The Filtered Items used to compare with adapterLocalItems in order to sync the localAdapterItems
     */
    private void applyAndAnimateAdditions(List<Item> filteredList) {
        for (int i = 0, count = filteredList.size(); i < count; i++) {
            final Item model = filteredList.get(i);
            if (!adapterLocalItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    /**
     * Iterate to compare localAdapterItems with filteredList - for each item that needs to be moved - call the {@link #moveItem(int, int)} method.
     *
     * @param filteredList The Filtered Items used to compare with adapterLocalItems in order to sync the localAdapterItems
     */
    private void applyAndAnimateMovedItems(List<Item> filteredList) {
        for (int toPosition = filteredList.size() - 1; toPosition >= 0; toPosition--) {
            final Item item = filteredList.get(toPosition);
            final int fromPosition = adapterLocalItems.indexOf(item);
            if (fromPosition >= 0 && toPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    /**
     * Call all the applyAndAnimate Methods: {@link #applyAndAnimateRemovals(List)},  {@link #applyAndAnimateAdditions(List)} ,  {@link #applyAndAnimateMovedItems(List)}
     *
     * @param filteredList The Filtered Items used to compare with adapterLocalItems in order to sync the localAdapterItems
     */
    private void applySearchFilters(List<Item> filteredList) {
        applyAndAnimateRemovals(filteredList);
        applyAndAnimateAdditions(filteredList);
        applyAndAnimateMovedItems(filteredList);
    }

    /**
     * generates a new {@code filteredItemsList} composed of all the {@code query} matches from the {@code originalItems}
     *
     * @param originalItems The List that contains all the items prior to any search
     * @param query         The Search Query
     * @return filteredItemsList
     */
    private List<Item> filter(List<Item> originalItems, String query) {
        query = query.toLowerCase();

        final List<Item> filteredItemsList = new ArrayList<>();
        for (Item item : originalItems) {
            final String text = item.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredItemsList.add(item);
            }
        }
        return filteredItemsList;
    }

    /**
     * Obtain a {@code filteredList} using the {@code query} And  {@code originalItems}
     * Apply The {@code filteredList} on the {@code localAdapterItems}
     *
     * @param query The Search Query
     */
    public void doSearch(String query) {
        List<Item> filteredList = filter(originalItems, query);
        this.applySearchFilters(filteredList);
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    static class YnetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvContent)
        TextView tvContent;

        @Bind(R.id.itemImage)
        ImageView itemImage;

        public YnetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //setup font on tvContnent:
            Typeface face = Typeface.createFromAsset(itemView.getContext().getAssets(), "ElliniaCLM-Light.ttf");
            tvContent.setTypeface(face);
        }
    }
}
