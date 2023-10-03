package com.example.newsnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//decleration
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calling there declared  variable and linking with there respective layout
        recyclerView = findViewById(R.id.news_recycler_view);
        progressIndicator = findViewById(R.id.progress_bar);
        getNews();
    }
//recyclerview method
    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }
//progressbar method
    void changeInProgress(boolean show){
        if(show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }



    void getNews(){
        changeInProgress( true);
        NewsApiClient newsApiClient = new NewsApiClient("dfc5aea1187c482781abda2425e4b7b1");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder().language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback(){
                    @Override
                    public void onSuccess(ArticleResponse response) {
//                        response.getArticles().forEach((a) -> {
//                            Log.i("Article",a.getTitle());
//                        });
                        runOnUiThread(() ->{
                            changeInProgress(false);
                            articleList = response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();

                        });



                        //USEing to get log
                        //Log.i("GOT RESPONSE",response.toString());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT FAILURE",throwable.getMessage());


                    }
                }
        );
    }
}