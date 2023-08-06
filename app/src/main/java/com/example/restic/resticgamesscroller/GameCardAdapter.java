package com.example.restic.resticgamesscroller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.ViewHolder> {

    private List<GameCard> cardList;
    private static Context context;
    private String type;
    private static final String freeGamesTYPE = "Free Games";
    private static final String premiumGamesTYPE = "Premium Games";

    public GameCardAdapter(List<GameCard> cardList, Context context, String type) {
        this.cardList = cardList;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (type.equals(freeGamesTYPE)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_game_card_layout, parent, false);
        }
        if (type.equals(premiumGamesTYPE)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.premium_game_card_layout, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameCard GameCard = cardList.get(position);
        holder.titleTextView.setText(GameCard.getTitle());

        if (holder.metaView != null) {
            holder.metaView.setText(GameCard.getMeta().toString());
        }
        if (holder.link != null) {
            holder.link.setOnClickListener(view -> {
                String url = GameCard.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                ContextCompat.startActivity(context, intent, null);
            });
        }

        boolean playTypePC = false;
        boolean playTypeController = false;

        for (int i = 0; i < GameCard.getPlayType().length(); i++) {
            try {
                String currentItem = GameCard.getPlayType().getString(i);

                if ("pc".equals(currentItem)) {
                    playTypePC = true;
                }

                if ("controller".equals(currentItem)) {
                    playTypeController = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (playTypePC == true && holder.playTypePC != null) {
            holder.playTypePC.setVisibility(ImageView.VISIBLE);
        }
        if (playTypeController == true && holder.playTypeController != null) {
            holder.playTypeController.setVisibility(ImageView.VISIBLE);
        }

        Picasso.get()
                .load(GameCard.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // Optional placeholder image
                .error(R.drawable.ic_launcher_foreground) // Optional error image
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView metaView;
        ImageView playTypePC;
        ImageView playTypeController;
        ImageView link;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            metaView = itemView.findViewById(R.id.gameMeta);

            link = itemView.findViewById(R.id.gameLink);
            playTypePC = itemView.findViewById(R.id.gamePlayTypePCView);
            playTypeController = itemView.findViewById(R.id.gamePlayTypeControllerView);
        }
    }
}