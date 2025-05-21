package com.example.lab3;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class LoadNotesTask extends AsyncTask<Void, Void, List<String>> {
    private Context context;
    private ListView listView;
    private DatabaseHelper dbHelper;

    public LoadNotesTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        return dbHelper.getAllNotes();
    }

    @Override
    protected void onPostExecute(List<String> notes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);
    }
}
