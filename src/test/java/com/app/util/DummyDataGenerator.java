package com.app.util;

import com.app.entity.Role;
import com.app.entity.enums.RoleTypeEnum;
import com.app.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DummyDataGenerator {

    public static List<User> getUsers(int amount, int idStarsFrom) {
        List<User> users = new ArrayList<>();
        for (int i = idStarsFrom; i < amount + idStarsFrom; i++) {
            users.add(new User("random-user-name" + i,
                    "random-first-name" + i,
                    "random-last-name" + i,
                    String.format("random%d@email.com", i),
                    "password" + i,
                    new Role(0L, RoleTypeEnum.ADMIN),
                    "private-key" + i,
                    "public-key" + i));
            users.get(i - idStarsFrom).setId((long) i);
        }

        return users;
    }

    public static List<User> getUsers(int amount) {
        return getUsers(amount, 1);
    }

}