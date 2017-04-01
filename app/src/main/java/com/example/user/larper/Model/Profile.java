package com.example.user.larper.Model;

import java.util.ArrayList;

/**
 * Created by User on 4/1/2017.
 */

public class Profile {

    private String nickName;
    private int age;
    private String gender;
    private String scenarioClass;
    private String race;
    private int hitPoints;
    private ArrayList<Skill> skills;
    private String biography;

    public Profile(String nickname, int age, String gender, String scenarioClass,
                   String race, int hitpoints, ArrayList<Skill> skills, String biography) {
        this.nickName = nickname;
        this.age = age;
        this.gender = gender;
        this.scenarioClass = scenarioClass;
        this.race = race;
        this.hitPoints = hitpoints;
        this.skills = new ArrayList<>(skills);
        this.biography = biography;
    }

    public String getNickName() { return this.nickName; }

    public void setNickName(String nickName) { this.nickName = nickName; }

    public int getAge() { return this.age; }

    public void setAge(int age) { this.age = age; }

    public String getGender() { return this.gender; }

    public void setSex(String gender) { this.gender = gender; }

    public String getScenarioClass() { return this.scenarioClass; }

    public void setScenarioClass(String scenarioClass) {
        this.scenarioClass = scenarioClass;
    }

    public String getRace() { return this.race; }

    public void setRace(String race) { this.race = race; }

    public int getHitPoints() { return this.hitPoints; }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public ArrayList<Skill> getSkills() { return this.skills; }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public String getBiography() { return this.biography; }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public boolean equals(Object obj) {
        return this.nickName.equals(((Profile)obj).getNickName());
    }
}
