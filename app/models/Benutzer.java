package models;

import at.ac.tuwien.big.we15.lab2.api.*;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import play.data.validation.Constraints.*;
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


    private String avatarId;
    private String firstName;
    private String lastName;


    private LocalDate birthday;
    private String gender;  //"m" or "f"

    public Benutzer(){}

    public Benutzer(String password, String username, String avatarId) {
        this.password = password;
        this.name = username;
        this.avatarId = avatarId;
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
        return Avatar.getAvatar(avatarId);
    }

    @Override
    public void setAvatar(Avatar avatar) {
        this.avatarId = avatar.getId();
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
    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    @Override
    public String toString() {
        return "Benutzer{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", avatarId='" + avatarId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Benutzer other = (Benutzer) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
