package models;

import at.ac.tuwien.big.we15.lab2.api.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class User implements at.ac.tuwien.big.we15.lab2.api.User{

    @Id
    private int id;

    private String name;
    private Avatar avatar;

    public User(int id, Avatar avatar, String name) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Avatar getAvatar() {
        return this.avatar;
    }

    @Override
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }
}
