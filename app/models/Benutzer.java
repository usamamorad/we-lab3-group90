package models;

import at.ac.tuwien.big.we15.lab2.api.*;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.validation.Constraint;
import java.time.LocalDate;

@Entity
public class Benutzer implements at.ac.tuwien.big.we15.lab2.api.User{

    @Id
    @Constraints.Required
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    private String name;

    @Constraints.Required
    @Constraints.MinLength(4)
    @Constraints.MaxLength(8)
    private String password;

    private Avatar avatar;
    private String firstName;
    private String lastName;


    private LocalDate birthday;
    private String gender;  //"m" or "f"

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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
