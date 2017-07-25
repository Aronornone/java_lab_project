package pojo;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;

public class User {

    private long userId;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime registrationDate;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setPasswordHash(String nonHashedPassword) {
        this.passwordHash = DigestUtils.md5Hex(nonHashedPassword);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

}
