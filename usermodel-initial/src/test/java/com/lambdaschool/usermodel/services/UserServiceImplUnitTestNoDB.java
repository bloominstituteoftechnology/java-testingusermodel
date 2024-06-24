package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class)
public class UserServiceImplUnitTestNoDB {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleService roleService;

    private List<User> userList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        u1.setUserid(1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.getRoles()
                .add(new UserRoles(u2,
                        r2));
        u2.getRoles()
                .add(new UserRoles(u2,
                        r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.setUserid(2);

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.setUserid(3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.getRoles()
                .add(new UserRoles(u4,
                        r2));
        u4.setUserid(4);

        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
        u5.getRoles()
                .add(new UserRoles(u5,
                        r2));
        u5.setUserid(5);

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));
        assertEquals("admin", userService.findUserById(1).getUsername());


    }

    @Test
    public void findByNameContaining() {
    }

    @Test
    public void findAll() {

        Mockito.when(userRepository.findAll())
                .thenReturn(userList);
        assertEquals(5, userService.findAll().size());

    }

    @Test
    public void delete() {
    }

    @Test
    public void findByName() {

        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(userList.get(0));
        assertEquals("admin", userService.findByName("admin").getUsername());


    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByNameNotFound() {

        Mockito.when(userRepository.findByUsername("Not found"))
                .thenReturn(null);
        assertEquals("admin", userService.findByName("admin").getUsername());


    }

    @Test
    public void save() {

        Role r1 = new Role("admin");

        r1.setRoleid(1);

        // admin, data, user
        User u1 = new User("newUser",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        u1.setUserid(0);


        Mockito.when(roleService.findRoleById(1))
                .thenReturn(r1);
        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(u1);

        assertEquals("newUser", userService.save(u1).getUsername());

    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
    }
}