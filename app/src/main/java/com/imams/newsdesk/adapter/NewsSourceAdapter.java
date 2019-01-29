package com.imams.newsdesk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imams.newsdesk.R;
import com.imams.newsdesk.activity.SourcesNewsActivity;
import com.imams.newsdesk.model.Source;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Source> sourceArrayList;

    public NewsSourceAdapter(Context context, ArrayList<Source> sourceArrayList) {
        this.context = context;
        this.sourceArrayList = sourceArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_sources, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Source source = sourceArrayList.get(position);
        holder.tvSourceName.setText(source.getName());
        holder.cardView.setOnClickListener(item -> {
            Intent intent = new Intent(context, SourcesNewsActivity.class);
            intent.putExtra(SourcesNewsActivity.KEY, source);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sourceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_source_logo)
        ImageView ivSourceLogo;
        @BindView(R.id.tv_source_name)
        TextView tvSourceName;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
