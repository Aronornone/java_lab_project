package pojo;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void testUserHashPassword() {
        User user = new User(1,"Name","@mail.ru","123452Afr3", LocalDateTime.now());
        assertEquals("c664e0608e2ff50d07ebc4e3127deafd",user.getPasswordHash());
        System.out.println(user);
    }
}