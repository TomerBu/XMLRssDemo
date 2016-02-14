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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class YnetRecyclerViewAdapter extends RecyclerView.Adapter<YnetRecyclerViewAdapter.YnetViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Item> items;
    private Rss rss;
    private Context context;

    public YnetRecyclerViewAdapter(Rss rss, Context context) {
        this.rss = rss;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = rss.getChannel().getItems();
    }


    @Override
    public YnetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.rv_item_ynet, parent, false);
        return new YnetViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(YnetViewHolder holder, int position) {
        Item item = items.get(position);
        item.extractDescription();


        holder.tvContent.setText(item.getTitle());
        Picasso.with(context).load(item.getImage()).error(R.drawable.ic_failed).placeholder(R.drawable.ic_placeholder).into(holder.itemImage);

    }

    static class YnetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvContent)
        TextView tvContent;

        @Bind(R.id.itemImage)
        ImageView itemImage;

        public YnetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            Typeface face = Typeface.createFromAsset(itemView.getContext().getAssets(), "SimpleCLM-Medium.ttf");
            face = Typeface.createFromAsset(itemView.getContext().getAssets(), "ElliniaCLM-Light.ttf");

            tvContent.setTypeface(face);
        }
    }
}
