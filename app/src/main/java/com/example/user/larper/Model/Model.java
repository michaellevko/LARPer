package com.example.user.larper.Model;

import java.util.ArrayList;

/**
 * Created by User on 4/1/2017.
 */

public class Model {
    final private static Model instance = new Model();
    private Model(){
    }

    public static Model instance(){
        return instance;
    }

    private Profile profile;
    /* TODO: IMPLEMENT MAP */
    private ArrayList<Profile> lore = new ArrayList<>();
    private ArrayList<Blueprint> blueprints = new ArrayList<>();
    private ArrayList<Profile> contacts = new ArrayList<>();

    public Profile getProfile() { return profile; }

    public void setProfile(Profile profile) {
        this.profile = new Profile(profile.getNickName(),
                                    profile.getAge(),
                                    profile.getGender(),
                                    profile.getScenarioClass(),
                                    profile.getRace(),
                                    profile.getHitPoints(),
                                    profile.getSkills(),
                                    profile.getBiography());
    }

    public ArrayList<Profile> getLore() { return this.lore; }

    public void addProfileToLore(Profile profile) { this.lore.add(profile); }

    public boolean removeProfileFromLore(Profile profile) { return this.lore.remove(profile);}

    public ArrayList<Blueprint> getBlueprints() { return this.blueprints; }

    public void addBlueprint(Blueprint bp) { this.blueprints.add(bp); }

    public boolean removeBlueprint(Blueprint bp) { return this.blueprints.remove(bp); }

    public ArrayList<Profile> getContacts() { return this.contacts; }

    public void addProfileToContact(Profile profile) { this.contacts.add(profile); }

    public boolean removeProfileFromContacts(Profile profile) {
        return this.contacts.remove(profile);
    }
}
