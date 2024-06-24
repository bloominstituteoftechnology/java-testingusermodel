package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class) //tell junit we are using spring
@SpringBootTest(classes = UserModelApplication.class) //tell where public static void main is (where the app starts)
@Transactional
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Before
    public void setUp() throws Exception {
        // set up mock data
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception { }

    @Test
    public void findUserById() {
        assertEquals("cinnamon", userService.findUserById(7).getUsername());
    }
    // userService.findUserById(7).getUsername() expect ( cinnamon )

    @Test (expected = EntityNotFoundException.class)
    public void findUserByIdFails() { assertEquals(1337, userService.findUserById(357).getUserid()); }
    // userService.findUserById(357).getUserid() ( expect EntityNotFoundException.class  )

    @Test
    public void findByNameContaining() { assertEquals(2, userService.findByNameContaining("tt").size()); }
    // userService.findByNameContaining("tt").size() expect  ( 2 )

    @Test
    public void findAll() { assertEquals(5, userService.findAll().size()); }
    // userService.findAll().size()) ( expect 5 )

    @Test
    public void delete() {
        // do this
        userService.delete(13);
        // expect 4 from userService.findAll().size()
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void findByName() { assertEquals(14, userService.findByName("misskitty").getUserid()); }
    // userService.findByName("misskitty").getUserid() ( expect 14 )

    @Transactional
    @Test
    public void save() {
        String username = "batman";
        Role r1 = roleService.findByName("admin");
        Role r2 = roleService.findByName("user");

        User user = new User("batman",
                "password",
                "batman@batman.com");
        user.getRoles().add(new UserRoles(user, r1));
        user.getRoles().add(new UserRoles(user, r2));

        User addUser = userService.save(user);

        // expect that value is not null
        assertNotNull(addUser);
        // addUser.getUsername()  expect username
        assertEquals(username, addUser.getUsername());
    }

    @Transactional
    @Test
    public void update() {

        String username = "batman2";
        Role r2 = roleService.findByName("user");

        User user = new User("batman2",
                "password",
                "batman@batman.com");
        user.getRoles().add(new UserRoles(user, r2));
        user.setUserid(13);

        User addUser = userService.update(user, 13);

        // expect that value is not null
        assertNotNull(addUser);
        // addUser.getUsername()  expect username
        assertEquals(username, addUser.getUsername());
    }

    @Transactional
    @Test
    public void deleteAll() {
        // do this
        userService.deleteAll();
        // userService.findAll().size() expect 0
        assertEquals(0, userService.findAll().size());
    }
}

