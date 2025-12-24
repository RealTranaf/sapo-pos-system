package com.sapo.mockprojectpossystem.seeder.mock;

import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UserMockFactory {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static User create(String username, String name, String phone, Role role) {
        User u = new User();
        u.setUsername(username);
        u.setName(name);
        u.setPhoneNum(phone);
        u.setPassword(encoder.encode("123456"));
        u.setRole(role);
        u.setActive(true);
        u.setExpired(false);
        u.setToken("token_" + username);
        return u;
    }

    public static List<User> all() {
        return List.of(
                create("cs_admin", "CS Admin", "0900000001", Role.CS),
                create("owner_1", "Owner One", "0900000101", Role.OWNER),
                create("sales_1", "Sales One", "0900000201", Role.SALES),
                create("wh_1", "Warehouse One", "0900000301", Role.WAREHOUSE)
        );
    }
}
