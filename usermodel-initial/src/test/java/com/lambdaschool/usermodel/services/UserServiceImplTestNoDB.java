package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
        properties = {
                "command.line.runner.enabled=false"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTestNoDB {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private HelperFunctions helperFunctions;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {userList = new ArrayList<>();

        Role role1 = new Role("admin");
        Role role2 = new Role("user");
        Role role3 = new Role("data");

        role1.setRoleid(1);;
        role2.setRoleid(2);
        role3.setRoleid(3);

        User u1 = new User("admin","password",
                "admin@lambdaschool.com");
        u1.setUserid(10);
        u1.getRoles()
                .add(new UserRoles(u1,
                        role1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        role2));
        u1.getRoles()
                .add(new UserRoles(u1,
                        role3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        userList.add(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(20);
        u2.getRoles()
                .add(new UserRoles(u2,
                        role2));
        u2.getRoles()
                .add(new UserRoles(u2,
                        role3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        userList.add(u2);

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(30);
        u3.getRoles()
                .add(new UserRoles(u3,
                        role2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        userList.add(u3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(40);
        u4.getRoles()
                .add(new UserRoles(u4,
                        role2));
        userList.add(u4);


        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
        u5.setUserid(50);
        u5.getRoles()
                .add(new UserRoles(u5,
                        role2));
        userList.add(u5);
        MockitoAnnotations.initMocks(this);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {
        Mockito.when(userRepository.findById(20L))
                .thenReturn(Optional.of(userList.get(1)));

        assertEquals("cinnamon",
                userService.findUserById(20)
                        .getUsername());


    }

    @Test
    public void findByNameContaining () {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("barnbarn"))
                .thenReturn(userList);

        assertEquals(5, userService.findByNameContaining("barnbarn").size());

    }
    @Test
    public void findAll() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findByName() {
    }

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
    }
}