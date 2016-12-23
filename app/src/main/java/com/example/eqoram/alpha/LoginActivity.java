package com.example.eqoram.alpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LoginActivity extends AppCompatActivity {
    private String ident = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Name des Headers ändern
        setTitle("Anmelden");

        ident = "";

        //Nutzer eventuell hinzufügen
        createDatabasedata();

        //ListView wird erstellt
        generateListView();

        //interagieren mit den einzelnen Elementen der Listview
        listenerListView();




        /*//zu Testzwecken nicht weiter beachten
        //db.addUser("a@b.de","Diester");
        if(db.getAllUsers().isEmpty()){
            Log.d("----------ACHTUNG: ","KEINE user");
        }
        else{
            Log.d("----------ACHTUNG: ", "mind 1 user");
            //User usr = db.getUser("a@b.de");
            //Log.d("Name: ",usr.getName());
        }*/
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private void createDatabasedata(){
        if(ChatApp.db.getAllUsers().isEmpty()){
            ChatApp.db.addUser(new User("max.mustermann@gmail.com","Max Mustermann"));
            ChatApp.db.addUser(new User("linus.torvalds@gmail.com","Linus Torvalds"));
            ChatApp.db.addUser(new User("elon.musk@gmail.com","Elon Musk"));
            ChatApp.db.addUser(new User("peter.schroeder@gmail.com","Peter Schröder"));
            ChatApp.db.addUser(new User("paul.meier@gmail.com","Paul Meier"));
            ChatApp.db.addUser(new User("daniel.kapitanowski@gmail.com","Daniel Kapitanowski"));
            ChatApp.db.addUser(new User("lisa.mueller@gmail.com","Lisa Müller"));
            ChatApp.db.addUser(new User("lukas.schwimmer@gmail.com","Lukas Schwimmer"));
            ChatApp.db.addUser(new User("donald.trump@gmail.com","Donald Trump"));
            ChatApp.db.addUser(new User("heinrich.völler@gmail.com","Heinrich Völler"));
            ChatApp.db.addUser(new User("tim.koch@gmail.com","Tim Koch"));
        }
    }

    private void generateListView(){
        //Datenbankobjekt wird erstellt und in Array konvertiert
        LinkedList<User> ll =  new LinkedList<>(ChatApp.db.getAllUsers().values());
        String[] email = new String[ll.size()];
        String[] name = new String[ll.size()];
        int i = 0;
        for(User usr: ll){
            email[i] = usr.getEmail();
            name[i] = usr.getName(); // nur zu testzwecken
            i++;
        }

        //Listview erstellen
        ListView listviewlogin;
        listviewlogin = (ListView) findViewById(R.id.listviewlogin);

        //Adapter erstellen
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, email);
        listviewlogin.setAdapter(adapter);
    }

    private void listenerListView(){
        ListView listviewlogin;
        listviewlogin = (ListView) findViewById(R.id.listviewlogin);

        //OnClickListener
        listviewlogin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                ident = textView.getText().toString();

                //Ausgabe als Toast
                String toastmsg = "Sie haben sich erfolgreich angemeldet: "+ ident;
                Toast.makeText(LoginActivity.this, toastmsg, Toast.LENGTH_LONG).show();


                //ChatOverViewActivity öffnen und IDENTIFICATION übergeben
                Intent myIntent = new Intent(LoginActivity.this, ChatOverviewActivity.class);
                myIntent.putExtra("key", ident);
                LoginActivity.this.startActivity(myIntent);
            }
        });
    }

}