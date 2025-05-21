package com.example.lab1a;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<Musicians> musicians = new ArrayList<Musicians>();
    ListView groupsList;
    @Override
    protected void onCreate(Bundle savedInstanceMusicians) {
        super.onCreate(savedInstanceMusicians);
        setContentView(R.layout.activity_main);


        // начальная инициализация списка
        setInitialData();
        // получаем элемент ListView
        groupsList = findViewById(R.id.groupsList);
        // создаем адаптер
        MusiciansAdapter musiciansAdapter = new MusiciansAdapter(this, R.layout.list_item, musicians);
        // устанавливаем адаптер
        groupsList.setAdapter(musiciansAdapter);
        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // получаем выбранный пункт
                Musicians selectedMusicians = (Musicians) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedMusicians.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        groupsList.setOnItemClickListener(itemListener);
    }
    private void setInitialData(){

        musicians.add(new Musicians ("Linkin Park", "Chester Bennington", R.drawable.linkin));
        musicians.add(new Musicians ("System of a down", "Serj Tankan", R.drawable.system));
        musicians.add(new Musicians ("Animal Djaz", "Красоцкий Алксандр", R.drawable.animal));
        musicians.add(new Musicians ("Slipknot", "Corey Taylor", R.drawable.slipknot));
        musicians.add(new Musicians ("Three Days Grace", "Adam Gontier", R.drawable.tdg));
        musicians.add(new Musicians ("Korn", "Jonatan Devis", R.drawable.korn));
        musicians.add(new Musicians ("Pantera", "Fill Anselmo", R.drawable.pantera));
        musicians.add(new Musicians ("Limp Bizkit", "Fred Derst", R.drawable.limp));

        musicians.add(new Musicians ("Death", "Chak Shuldiner", R.drawable.death));
        musicians.add(new Musicians ("Ghost", "Tobias Forge", R.drawable.ghost));
        musicians.add(new Musicians ("Киш", "Михаил Горшенёв", R.drawable.kish));
        musicians.add(new Musicians ("Кукрыниксы", "Алексей Горшенёв", R.drawable.kukriniksi));
        musicians.add(new Musicians ("Megadeth", "Deiv Mastein", R.drawable.megadeth));
        musicians.add(new Musicians ("Metallica", "Rob Helford", R.drawable.metallica));
        musicians.add(new Musicians ("Nirvana", "Kurt Kobein", R.drawable.nirvana));
        musicians.add(new Musicians ("Suidakra", "Arkadius Antonik", R.drawable.suidakra));

        musicians.add(new Musicians ("Aria", "Valery Kipelov", R.drawable.aria));
        musicians.add(new Musicians ("Kino", "Victor Coi", R.drawable.kino));
        musicians.add(new Musicians ("Kipelov", "Valery Kipelov", R.drawable.aria));
    }
}