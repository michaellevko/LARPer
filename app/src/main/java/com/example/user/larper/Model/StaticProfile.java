package com.example.user.larper.Model;

/**
 * Created by event on 04/04/2017.
 */

public class StaticProfile {
    private String nickname;
    private String google_id;

    public StaticProfile()
    {
    }

    public StaticProfile(String nickname, String google_id)
    {
        this.nickname = nickname;
        this.google_id = google_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getNickname(){return this.nickname;}

    @Override
    public String toString()
    {
        return getNickname();
    }
}
