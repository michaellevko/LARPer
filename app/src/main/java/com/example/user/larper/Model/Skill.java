package com.example.user.larper.Model;

/**
 * Created by User on 4/1/2017.
 */

public class Skill {

    private String name;
    private int level;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getLevel() { return level; }

    public void setLevel(int level) {
        if (level >= 0) { this.level = level; }
    }
}
