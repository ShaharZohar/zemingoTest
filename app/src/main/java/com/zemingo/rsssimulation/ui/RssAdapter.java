package com.zemingo.rsssimulation.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zemingo.rsssimulation.R;
import me.toptas.rssconverter.RssItem;

import java.util.LinkedList;
import java.util.List;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssViewHolder> {

    private final List<RssItem> mData = new LinkedList<>();

    @NonNull
    @Override
    public RssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RssViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rss, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RssViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void updateData(@NonNull List<RssItem> feed) {
        mData.clear();
        mData.addAll(feed);
        notifyDataSetChanged();
    }

    class RssViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mDate;

        RssViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.rss_title);
            mDate = itemView.findViewById(R.id.rss_date);
        }

        void bind(final RssItem rssItem) {
            mTitle.setText(rssItem.getTitle());
            mDate.setText(rssItem.getPublishDate());
        }


    }
}
