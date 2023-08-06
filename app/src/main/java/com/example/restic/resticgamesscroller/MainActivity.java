package com.example.restic.resticgamesscroller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String freeGamesTYPE = "Free Games";
    private static final String premiumGamesTYPE = "Premium Games";

    private RecyclerView freeGamesRecyclerView;
    private List<GameCard> freeGamesCardList;
    private GameCardAdapter freeGamesCardAdapter;

    private RecyclerView premiumGamesRecyclerView;
    private List<GameCard> premiumGamesCardList;
    private GameCardAdapter premiumGamesCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        freeGamesRecyclerView = findViewById(R.id.freeGamesRecyclerView);
        freeGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        premiumGamesRecyclerView = findViewById(R.id.premiumGamesRecyclerView);
        premiumGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Thread thread = new Thread(() -> {
            loadGameCardsData();
        });

        thread.start();
    }

    private void loadGameCardsData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://assets.gamery.ir/api/v1/data").build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String jsonString = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);

                            runOnUiThread(() -> {
                                renderGameCards(jsonObject);
                            });
                        } catch (JSONException e) {
                            Log.e(TAG, "Error converting JSON data: " + e);
                        }

                    } else {
                        Log.e(TAG, "Error loading JSON data: " + response.message());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error processing JSON data: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Error loading JSON data: " + e.getMessage());
            }
        });
    }

    private void renderGameCards(JSONObject data) {
        freeGamesCardList = new ArrayList<GameCard>();
        premiumGamesCardList = new ArrayList<GameCard>();

        JSONArray jsonGameList = null;
        try {
            jsonGameList = data.getJSONArray("data");
        } catch (JSONException e) {
            Log.e(TAG, "Error converting JSON data: " + e);
        }

        for (int i = 0; i < jsonGameList.length(); i++) {
            try {
                JSONObject jsonObject = jsonGameList.getJSONObject(i);

                String name = jsonObject.getString("name");
                JSONArray playType = jsonObject.getJSONArray("play type");
                String imageUrl = jsonObject.getString("mobile-image");
                String type = jsonObject.getString("type");
                String link = jsonObject.getString("link");
                Number meta = jsonObject.getInt("meta");

                if (type.equals(freeGamesTYPE)) {
                    freeGamesCardList.add(new GameCard(name, playType, imageUrl, meta, link));
                }

                if (type.equals(premiumGamesTYPE)) {
                    premiumGamesCardList.add(new GameCard(name, playType, imageUrl, meta, link));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error converting JSON data: " + e);
            }
        }

        freeGamesCardAdapter = new GameCardAdapter(freeGamesCardList, this, freeGamesTYPE);
        freeGamesRecyclerView.setAdapter(freeGamesCardAdapter);

        premiumGamesCardAdapter = new GameCardAdapter(premiumGamesCardList, this, premiumGamesTYPE);
        premiumGamesRecyclerView.setAdapter(premiumGamesCardAdapter);
    }
}