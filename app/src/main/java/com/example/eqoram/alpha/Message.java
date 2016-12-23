package com.example.eqoram.alpha;

import java.sql.Timestamp;

/**
 * Created by Sven on 11/8/2016.
 */

public class Message {
    private int id;
    private String from;
    private String to;
    private String message;
    private Timestamp time;
    private boolean is_read;

    public Message(int id, String from, String to, String message, Timestamp time, boolean is_read)
    {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.time = time;
        this.is_read = is_read;
    }



    public Message(String from, String to, String message, Timestamp time, boolean is_read) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.time = time;
        this.is_read = is_read;
    }




    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Timestamp getTime() {
        return time;
    }

    public boolean getIs_read() {
        return is_read;
    }
}
