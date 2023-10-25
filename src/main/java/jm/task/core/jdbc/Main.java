package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        UserService userService = new UserServiceImpl();
//
        userService.createUsersTable();
        userService.saveUser("Иван1","Иванов1", (byte) 21);
        userService.saveUser("Иван2","Иванов2", (byte) 22);
        userService.saveUser("Иван3","Иванов3", (byte) 23);
        userService.saveUser("Иван4","Иванов4", (byte) 24);
        userService.removeUserById(3);
        List <User> userslist = userService.getAllUsers();
        System.out.println(userslist);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
