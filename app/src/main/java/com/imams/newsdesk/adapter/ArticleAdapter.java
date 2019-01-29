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
import com.imams.newsdesk.activity.WebActivity;
import com.imams.newsdesk.activity.WebViewActivity;
import com.imams.newsdesk.model.Article;
import com.imams.newsdesk.support.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Article> arrayList;

    public ArticleAdapter(Context context, ArrayList<Article> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = arrayList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvAuthor.setText(article.getAuthor());
        holder.tvPublishedDate.setText(Utility.formatDate(article.getPublishedAt()));
        if (article.getUrlToImage() != null) {
            holder.ivImageContent.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(article.getUrlToImage())
                    .centerCrop()
                    .fit()
                    .into(holder.ivImageContent);
        } else {
            holder.ivImageContent.setVisibility(View.GONE);
        }
        holder.tvDescription.setText(article.getDescription());
        holder.cardView.setOnClickListener(item -> {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra(WebActivity.KEY, article);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_published_date)
        TextView tvPublishedDate;
        @BindView(R.id.iv_image)
        ImageView ivImageContent;
        @BindView(R.id.tv_description)
        TextView tvDescription;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
