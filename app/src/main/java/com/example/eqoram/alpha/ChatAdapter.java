package com.example.eqoram.alpha;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

/**
 * Created by Matthias on 22.11.2016.
 */

public class ChatAdapter extends ArrayAdapter<Message>{

    private  String identification;

    //ViewHolder für bessere Performance
    private static class ViewHolder {
        TextView messageText;
        LinearLayout layout;
        LinearLayout parent_layout;
        ImageView isRead;
    }


    public ChatAdapter(Context context, ArrayList<Message> messageList) {
        super(context, 0, messageList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);

        //identification aus der aufrufendenen ChatActivity-Instanz übernehmen
        ChatActivity ident = ((ChatActivity)getContext());
        identification = ident.getIdentification();

        //für bessere Perfomance viewHolder nutzen
        ViewHolder viewHolder;

        // Falls möglich Views recyclen, ansonsten erweitern
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.message_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.messageText = (TextView)
                    convertView.findViewById(R.id.message_text);
            viewHolder.layout = (LinearLayout)
                    convertView.findViewById(R.id.bubble_layout);
            viewHolder.parent_layout = (LinearLayout)
                    convertView.findViewById(R.id.bubble_layout_parent);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //überprüfen, von wem die Nachricht kommt und auf die richtige Seite bringen
        viewHolder.isRead = (ImageView) convertView.findViewById(R.id.is_read);

        if (message.getFrom().equals(identification)) {
            viewHolder.layout.setBackgroundResource(R.mipmap.bubble3);
            viewHolder.parent_layout.setGravity(Gravity.RIGHT);
            viewHolder.isRead.setVisibility(convertView.VISIBLE);


            //Wenn Nachricht gelesen wurde, isRead-Symbol anpassen
            if (message.getIs_read()) {
                viewHolder.isRead.setImageResource(R.mipmap.isread);
            } else {
                viewHolder.isRead.setImageResource(R.mipmap.notread);
            }

        } else {
            viewHolder.layout.setBackgroundResource(R.mipmap.bubble1);
            viewHolder.parent_layout.setGravity(Gravity.LEFT);
            viewHolder.isRead.setVisibility(convertView.GONE);

            //Empfangene Nachrichten als gelesen markieren
            if (!message.getIs_read()) {
                message.setIs_read(true);
                ChatApp.db.updateMessage(message);
            }
        }


        viewHolder.messageText.setText(message.getMessage());
        return convertView;
    }



}