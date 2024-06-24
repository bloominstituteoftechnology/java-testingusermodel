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


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
        properties = {"command.line.runner.enabled=false"})
public class UserServiceImplTestNoDB {

    @Autowired
    UserService userService;

    @MockBean
    private UserRepository userrepos;

    @MockBean
    private RoleService roleService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {

        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);

        User u1 = new User("admintest",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(1);
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
        userList.add(u1);

        User u2 = new User("cinnamontest",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(2);
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
        userList.add(u2);

        User u3 = new User("barnbarntest",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(3);
        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        userList.add(u3);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {
        Mockito.when(userrepos.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));
        assertEquals("admintest",
                userService.findUserById(1)
                        .getUsername());
    }

    @Test
    public void findByNameContaining() {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("admintest"))
                .thenReturn(userList);
        assertEquals(3,
                userService.findByNameContaining("admintest")
                        .size());
    }

    @Test
    public void findAll() {
        Mockito.when(userrepos.findAll())
                .thenReturn(userList);
        assertEquals(3,
                userService.findAll()
                        .size());
    }

    @Test
    public void delete() {
        Mockito.when(userrepos.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));
        Mockito.doNothing()
                .when(userrepos)
                .deleteById(1L);
        userService.delete(1);
        assertEquals(3,
                userList.size());
    }

    @Test
    public void findByName() {
        Mockito.when(userrepos.findByUsername("barnbarntest"))
                .thenReturn(userList.get(0));
        assertEquals(1,
                userService.findByName("barnbarntest")
                        .getUserid());
    }

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
        Mockito.when(userrepos.findAll())
                .thenReturn(userList);
        Mockito.doNothing()
                .when(userrepos)
                .deleteAll();
        userService.deleteAll();
        assertEquals(3, userList.size());
    }
}