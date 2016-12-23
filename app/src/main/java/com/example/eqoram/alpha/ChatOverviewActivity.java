package com.example.eqoram.alpha;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ChatOverviewActivity extends AppCompatActivity {
    private String ident = "";
    private String[] items = new String[2];
    private DrawerLayout DrawerLayout;
    private ActionBarDrawerToggle DrawerToggle;
    private ListView DrawerList;

    private String [] email_friend;
    private String friend = "";

    // Array in dem alle emailadressen stehen
    private String[] email;

    //LinkedList in der jeweils die letzte Nachricht zwischen zei usern gespeichert wird
    private LinkedList<Message> letzteMessageLinkedList;

    //Comperator um die LinkedList nach dem TimeStamp zu sortieren
    Comparator<Message> compTimeStamp = new TimeStampComparatorAbwaerts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getKey();

        setTitle("MegaBet");

        setContentView(R.layout.activity_chat_overview);

        checkLogin();

        generateNavigationDrawer();

        listenerListViewND();

        getProfile();

        generateNavigationDrawerButton();


        //createDatabasedata();
    }


    //Björns PART
    //Ueberpruefen ob der Nutzer wirklich eingeloggt ist (Sicherheitsmechanismus)
    private void checkLogin(){
        if(ident == ""){
            Intent myIntent = new Intent(ChatOverviewActivity.this, LoginActivity.class);
            myIntent.putExtra("key", ident);
            ChatOverviewActivity.this.startActivity(myIntent);
        }
    }

    //Navigation Drawer in der Acitivity anzeigen lassen
    private void generateNavigationDrawer(){
        items[0] = "Neuer Chat";
        items[1] = "Abmelden";

        DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        DrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items));
    }

    private void listenerListViewND(){
        ListView DrawerList;
        DrawerList = (ListView) findViewById(R.id.left_drawer);

        //OnClickListener für die ListView
        DrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                if(position == 0) {
                    Intent myIntent = new Intent(ChatOverviewActivity.this, FriendListActivity.class);
                    myIntent.putExtra("key", ident); //Optional parameters
                    ChatOverviewActivity.this.startActivity(myIntent);
                }
                if(position == 1){
                    Intent myIntent = new Intent(ChatOverviewActivity.this, LoginActivity.class);
                    myIntent.putExtra("key", ident); //Optional parameters
                    ChatOverviewActivity.this.startActivity(myIntent);
                    finish();
                }
            }
        });
    }

    private void generateNavigationDrawerButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerToggle = new ActionBarDrawerToggle(this, DrawerLayout,
                R.string.app_name, R.string.app_name) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();


            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        DrawerToggle.setDrawerIndicatorEnabled(true);
        DrawerLayout.setDrawerListener(DrawerToggle);

    }

    //braucht man um den Button anzeigen zu lassen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        if (DrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //braucht man um den Button zu synchronisieren
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DrawerToggle.syncState();
    }

    //braucht man um den Button zu synchronisieren
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DrawerToggle.onConfigurationChanged(newConfig);
    }

    //Nutzer aus der Datenbank holen
    private void getProfile(){
        User usr = ChatApp.db.getUser(ident);
        String name = usr.getName();
        String email = usr.getEmail();

        TextView tv_name = (TextView) findViewById(R.id.userName);
        tv_name.setText(name);

        TextView tv_email = (TextView) findViewById(R.id.userEmail);
        tv_email.setText(email);
    }

    //IDENTIFICATION aus der vorherigen Activity holen
    private void getKey(){
        Intent intent = getIntent();
        ident = intent.getStringExtra("key");
    }




//STEFANS PART

    @Override
    protected void onResume() {
        super.onResume();

        generateListView();
    }


    //Datensätze sollen für Testzwecke erzeugt werden
    private void createDatabasedata(){
        DatabaseHandler db = new DatabaseHandler(this);
        db.addMessage(new Message("max.mustermann@gmail.com","peter.schroeder@gmail.com","Hallo wie geht es dir?",ConversionHelper.now(),false));
        db.addMessage(new Message("linus.torvalds@gmail.com","max.mustermann@gmail.com","Hallo was tippst du?",ConversionHelper.now(),false));
        db.addMessage(new Message("max.mustermann@gmail.com","lisa.mueller@gmail.com","Gewinnt Bielefeld?",ConversionHelper.now(),false));
        db.addMessage(new Message("tim.koch@gmail.com","heinrich.völler@gmail.com","Gewinnt Dortmund?",ConversionHelper.now(),false));

        db.close();

    }

    //Email Liste mit allen Emails wird erstellt
    private void erstellenUserListe(){
        LinkedList<User> userLinkedList = new LinkedList<>(ChatApp.db.getAllUsers().values());
        email = new String[userLinkedList.size()];
        int i = 0;
        for(User usr: userLinkedList) {
            email[i] = usr.getEmail();
            i++;
        }

    }
    /*Eine LinkedList wird erstellt in der jeweils die letzte Nachricht zwischen zwei Usern gespeichert wird */
    private void erstellenNachrichtenListe(){
        letzteMessageLinkedList = new LinkedList<>();
        for (int i = 0; i < email.length; i++) {
            LinkedList<Message> messageLinkedList = new LinkedList<>(ChatApp.db.getMessageList(ident, email[i]).values());
            if (!messageLinkedList.isEmpty()) {
                Collections.sort(messageLinkedList, compTimeStamp);
                letzteMessageLinkedList.add(messageLinkedList.get(0));
            }
        }
        Collections.sort(letzteMessageLinkedList, compTimeStamp);
    }


    private void generateListView(){
        User chatFriend;

        this.erstellenUserListe();
        this.erstellenNachrichtenListe();

        final int n = letzteMessageLinkedList.size();

        String[] name = new String[n];
        String[] nachricht = new String[n];
        String[] time = new String[n];
        email_friend = new String[n];

        int i = 0;
        for(Message msg: letzteMessageLinkedList){
            if(msg.getFrom().equalsIgnoreCase(ident)){
                chatFriend = ChatApp.db.getUser(msg.getTo());
            }
            else {
                chatFriend = ChatApp.db.getUser(msg.getFrom());
            }
            name[i] = chatFriend.getName();
            email_friend[i] = chatFriend.getEmail();
            if(msg.getMessage().length()>20){
                nachricht[i] = msg.getMessage().substring(0,20)+"...";
            }else
            {
                nachricht[i] = msg.getMessage();
            }
            time[i]=ConversionHelper.timeStampToString(msg.getTime());

            i++;
        }

        //Listview wird erstellt
        ListView listviewChat;
        listviewChat = (ListView) findViewById(R.id.chatListview);

        //Adapter wird erstellt
        ArrayAdapter adapter = new MySimpleArrayAdapter(this, name, nachricht, time);
        listviewChat.setAdapter(adapter);

        //OnClickListener
        listviewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                friend = email_friend[position];

                Intent myIntent = new Intent(ChatOverviewActivity.this, ChatActivity.class);
                myIntent.putExtra("friend", friend+";"+ident);
                ChatOverviewActivity.this.startActivity(myIntent);
            }
        });
    }
}
