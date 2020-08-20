package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

// Service Impl using DB
@RunWith(SpringRunner.class) // tell junit we're doing a spring app
@SpringBootTest(classes = UserModelApplication.class)// where is our public static void main
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest
{
    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this); // initialize mockito (systems will be using) to work with this class

//        List<User> myList = userService.findAll();
//        for (User u : myList)
//        {
//            System.out.println(u.getUserid() + "" + u.getUsername());
//        }
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void A_findUserById()
    {
        assertEquals("test cinnamon", userService.findUserById(7).getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void AB_findUserByIdNotFound()
    {
        assertEquals("test cinnamon", userService.findUserById(100).getUsername());
    }

    @Test
    public void B_findByNameContaining()
    {
        assertEquals(1, userService.findByNameContaining("cin").size());
    }

    @Test
    public void C_findAll()
    {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void E_delete()
    {
        userService.delete(7);
        assertEquals(4, userService.findAll().size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void EA_deleteNotFound()
    {
        userService.delete(100);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void D_findByName()
    {
        assertEquals("test cinnamon", userService.findByName("test cinnamon").getUsername());
        assertEquals("test barnbarn", userService.findByName("test barnbarn").getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void DA_findByNameNotFound()
    {
        assertEquals("test cinnamon", userService.findByName("test frank").getUsername());
        assertEquals("test barnbarn", userService.findByName("test elle").getUsername());
    }

    @Test
    public void F_saveadd()
    {
        // create user obj
        String user2username = "test lauren";
        User u2 = new User(user2username,
            "password",
            "lauren@school.lambda");

        // create user
        Role r1 = new Role("banana");
        r1.setRoleid(1); // has to match id that's already in our system
        Role r2 = new Role("apple");
        r2.setRoleid(2);
        u2.getRoles().add(new UserRoles(u2, r1)); // add to user
        u2.getRoles().add(new UserRoles(u2, r2));

        u2.getUseremails().add(new Useremail(u2, "lauren2@school.lambda"));

        // call save to get it to work
        User addUser = userService.save(u2);
        assertNotNull(addUser); // test there's something there
        assertEquals(user2username, addUser.getUsername()); // and returns something that' correct
    }

    @Test
    public void FA_saveput()
    {
        // create user obj
        String user2username = "Test Lauren";
        User u2 = new User(user2username,
            "password",
            "lauren@school.lambda");
        u2.setUserid(15);

        // create user
        Role r1 = new Role("banana");
        r1.setRoleid(1); // has to match id that's already in our system
        Role r2 = new Role("apple");
        r2.setRoleid(2);
        u2.getRoles().add(new UserRoles(u2, r1)); // add to user
        u2.getRoles().add(new UserRoles(u2, r2));

        u2.getUseremails().add(new Useremail(u2, "lauren2@school.lambda"));
    }

    @Test
    public void G_update()
    {
    }

    @Test
    public void H_deleteAll()
    {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}