package com.example.eqoram.alpha;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ChatActivity extends AppCompatActivity {
    private String identification;
    private String receiverMail;
    private String receiverName;

    private EditText text;
    private Button sendButton;

    private ChatAdapter chatAdapter;
    private ArrayList<Message> messageList;

    Comparator<Message> compTimeStamp = new TimeStampComparator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //UpButton in der Actionbar aktivieren und Null-check verhindern
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Um ausschließlich die ChatActivity zu testen:
         ladeBeispieldaten();
         */

        getKey();

        getReceiverName();
        setTitle("Chat mit " + receiverName);

        text = (EditText) findViewById(R.id.messageBox);
        setTextChangedListener();
        text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2000)});

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setEnabled(false);
        setButtonClickListener();

        generateBubbles();
    }

    //Wird für den UpButton benötigt
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }


    //Name des Empfängers aus DB holen
    private void getReceiverName(){

        User user = ChatApp.db.getUser(receiverMail);
        receiverName = user.getName();
    }

    //Überprüft, ob Textfeld gefüllt ist und aktiviert Sendebutton
    public void setTextChangedListener() {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(text.getText().toString())) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
                if (s.length() > 0 && s.subSequence(0, 1).toString().equalsIgnoreCase(" ")) {
                    text.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    // Übergibt beim klicken des Buttons die eingegebene Nachricht an die Datenbank
    private void setButtonClickListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatApp.db.addMessage(new Message(identification, receiverMail,
                        text.getText().toString().trim(), ConversionHelper.now(), false));

                //Textfeld leeren
                text.setText("");

                sendButton.setEnabled(false);
                generateBubbles();
            }
        });
    }

    //einzelne Nachrichten vom ChatAdapter darstellen lassen
    private void generateBubbles(){
        this.generateMessageList(ChatApp.db);

        chatAdapter = new ChatAdapter(this, messageList);
        ListView listView = (ListView) findViewById(R.id.bubblesListView);

        //Automatisch nach unten scrollen
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);

        listView.setAdapter(chatAdapter);

    }

    //Betreffende Nachrichten aus DB laden
    private void generateMessageList(DatabaseHandler db){
        messageList = new ArrayList<>
                (db.getMessageList(identification, receiverMail).values());
        Collections.sort(messageList, compTimeStamp);
    }


    //Identification für ChatAdapter bereitstellen
    public String getIdentification(){
        return identification;
    }




    //Erstelle Beispieldaten, um diese Activity zu testen
    public void ladeBeispieldaten(){
        identification = "matthis.neufeld@gmail.com";
        receiverMail = "max.mustermann@gmail.com";

        DatabaseHandler db = new DatabaseHandler(this);
        db.addUser(new User("max.mustermann@gmail.com","Max Mustermann"));

        db.addMessage(new Message("matthis.neufeld@gmail.com", "max.mustermann@gmail.com", "ungelesene Nachricht0 - von Matthis", ConversionHelper.now(), false));
        db.addMessage(new Message("max.mustermann@gmail.com", "matthis.neufeld@gmail.com", "dies ist Nachricht 1 von oben gezählt - von Max", ConversionHelper.now(), true));
        db.addMessage(new Message("matthis.neufeld@gmail.com", "max.mustermann@gmail.com", "dies ist Nachricht 2 - von Matthis", ConversionHelper.now(), true));
        db.addMessage(new Message("max.mustermann@gmail.com", "matthis.neufeld@gmail.com", "dies ist Nachricht 3 - von Max", ConversionHelper.now(), true));
        db.addMessage(new Message("matthis.neufeld@gmail.com", "max.mustermann@gmail.com", "ungelesene Nachricht - von Matthis", ConversionHelper.now(), false));

        db.close();

    }


    //Björns Code
    //identification aus der vorherigen Activity holen
    private void getKey(){
        Intent intent = getIntent();
        String string = intent.getStringExtra("friend");
        String[] split = string.split(";");
        receiverMail = split[0];
        identification = split[1];
    }

}

