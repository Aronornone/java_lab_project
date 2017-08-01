package pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;

@Data
public class User {

    private long userId;
    private String name;
    private String email;
    @Getter @Setter(AccessLevel.PRIVATE)
    private String passwordHash;
    private LocalDateTime registrationDate;

    public User(long userId, String name, String email, String hashPassword, LocalDateTime registrationDate) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = hashPassword;
        this.registrationDate = registrationDate;
    }

    public User(String name, String email, String hashPassword, LocalDateTime registrationDate) {
        this.name = name;
        this.email = email;
        this.passwordHash = hashPassword;
        this.registrationDate = registrationDate;
    }
    public void setPassword(String nonHashedPassword) {
        this.passwordHash = DigestUtils.md5Hex(nonHashedPassword);
    }

}
