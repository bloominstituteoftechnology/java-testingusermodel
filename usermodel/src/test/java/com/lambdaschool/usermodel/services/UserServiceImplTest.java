package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

// Service Impl using DB
@RunWith(SpringRunner.class) // tell junit we're doing a spring app
@SpringBootTest(classes = UserModelApplication.class)// where is our public static void main
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
    public void findUserById()
    {
        assertEquals("test cinnamon", userService.findUserById(7).getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findUserByIdNotFound()
    {
        assertEquals("test cinnamon", userService.findUserById(100).getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        assertEquals(1, userService.findByNameContaining("cin").size());
    }

    @Test
    public void findAll()
    {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void delete()
    {
    }

    @Test
    public void findByName()
    {
        assertEquals("test cinnamon", userService.findByName("test cinnamon").getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByNameNotFound()
    {
        assertEquals("test frank", userService.findByName("test frank").getUsername());
    }

    @Test
    public void saveadd()
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
    public void saveput()
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
    public void update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}