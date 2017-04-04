package com.example.user.larper.Model;

/**
 * Created by event on 04/04/2017.
 */

public class StaticProfile {
    private String nickname;
    private String google_id;

    public StaticProfile(String nickname, String google_id)
    {
        this.nickname = nickname;
        this.google_id = google_id;
    }

    public String getNickname(){return this.nickname;}
    public String getGoogleID(){return this.google_id;}
}
