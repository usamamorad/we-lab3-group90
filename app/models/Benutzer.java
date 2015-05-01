package models;

import at.ac.tuwien.big.we15.lab2.api.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class Benutzer implements at.ac.tuwien.big.we15.lab2.api.User{

    @Id
    private String name;
    private String password;

    private Avatar avatar;
    private String firstName;
    private String lastName;
    private Date birthday;
    private boolean man;
    private boolean women;

    public Benutzer(){}

    public Benutzer(String password, String username) {
        this.password = password;
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

    @Override
    public Avatar getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }

    public boolean isWomen() {
        return women;
    }

    public void setWomen(boolean women) {
        this.women = women;
    }
}
