package com.example.user.larper.Model;

/**
 * Created by User on 4/1/2017.
 */

public class Skill {

    private String name;
    private String level;

    public Skill(){ this.level = "0"; }

    public Skill(String skillName) {
        this.name = skillName;
        this.level = "0";
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }

    public void setLevel(String level) {
        if (Integer.parseInt(level) >= 0) { this.level = level; }
    }
}
