

package com.example.eqoram.alpha;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import java.util.LinkedList;

/**
 * Created by Sven on 11/16/2016.
 */

public class FriendListActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        LinkedList<User> ll =  new LinkedList<>(ChatApp.db.getAllUsers().values());
        String[] email = new String[ll.size() - 1];
        int i = 0;
        for(User usr: ll){
            if(usr.getEmail().equals(getIntent().getStringExtra("key")) == false){
                email[i] = usr.getEmail();
                i++;
            }
        }
        listView = (ListView) findViewById(R.id.chatList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,email);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String  itemValue    = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(FriendListActivity.this, ChatActivity.class);
                intent.putExtra("friend", itemValue+";"+getIntent().getStringExtra("key"));
                startActivity(intent);
            }

        });
    }
}
