package com.example.demo_searchviewrecyclerview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<LanguageData> mList = new ArrayList<>();
    private LanguageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addDataToList();
        adapter = new LanguageAdapter(mList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String query) {
        if (query != null) {
            List<LanguageData> filteredList = new ArrayList<>();
            for (LanguageData item : mList) {
                if (item.getTitle().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    filteredList.add(item);
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(filteredList);
            }
        }
    }

    private void addDataToList() {
        mList.add(new LanguageData("Java", R.drawable.java));
        mList.add(new LanguageData("Kotlin", R.drawable.kotlin));
        mList.add(new LanguageData("C++", R.drawable.cplusplus));
        mList.add(new LanguageData("Python", R.drawable.python));
        mList.add(new LanguageData("HTML", R.drawable.html));
        mList.add(new LanguageData("Swift", R.drawable.swift));
        mList.add(new LanguageData("C#", R.drawable.csharp));
        mList.add(new LanguageData("JavaScript", R.drawable.javascript));
    }
}
