package com.lambdaschool.usermodel.services;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.RoleRepository;
import com.lambdaschool.usermodel.repository.UserRepository;
import com.lambdaschool.usermodel.repository.UseremailRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
    properties = {"command.line.runner.enabled=false"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTestNoDB
{
   @Autowired
   private UserService userService;

   @MockBean
   private UserRepository userRepository;

   @MockBean
   private RoleRepository roleRepository;

   @MockBean
   private RoleService roleService;

   @MockBean
   HelperFunctions helperFunctions;

   private List<User> userList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();

        userService.deleteAll();
        roleService.deleteAll();
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin Test",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(10);
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
        u1.getUseremails().get(0).setUseremailid(11);

        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(12);


        userList.add(u1);

        // data, user
        User u2 = new User("cinnamon Test",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.setUserid(20);
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.getRoles()
            .add(new UserRoles(u2,
                r3));
        u2.getUseremails()
            .add(new Useremail(u2,
                "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(21);
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(22);

        userList.add(u2);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void a_findUserById()
    {
        Mockito.when(userRepository.findById(20L))
            .thenReturn(Optional.of(userList.get(1)));

        assertEquals("cinnamon Test".toLowerCase(),
            userService.findUserById(20)
        .getUsername());
    }

    @Test(expected =  ResourceNotFoundException.class)
    public void aa_notfindUserById()
    {
        Mockito.when(userRepository.findById(300L))
            .thenThrow(ResourceNotFoundException.class);

        assertEquals("cinnamon Test".toLowerCase(),
            userService.findUserById(300)
                .getUsername());
    }


    @Test
    public void b_findByNameContaining()
    {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("admin"))
            .thenReturn(userList);

        assertEquals(2, userService.findByNameContaining("admin").size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void bb_findByNameContainingFail()
    {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("asdfwsadf"))
            .thenThrow(ResourceNotFoundException.class);

        assertEquals("asdfwsadf",
            userService.findByNameContaining("admin Test"));
    }

    @Test
    public void findAll()
    {
        Mockito.when(userRepository.findAll())
            .thenReturn(userList);

        assertEquals(2,
            userService.findAll().size());
    }

    @Test
    public void delete()
    {
        Mockito.when(userRepository.findById(4L))
            .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
            .when(userRepository)
            .deleteById(4L);

        userService.delete(4);
        assertEquals(2,
            userList.size());
    }

    @Test
    public void findByName()
    {
        Mockito.when(userRepository.findByUsername("admin Test".toLowerCase()))
            .thenReturn(userList.get(0));

      assertEquals("admin Test".toLowerCase(),
          userService.findByName("admin Test")
              .getUsername());
    }

    @Test
    public void save()
    {
        String user3Name = "Indy Test";
        User u3 = new User(user3Name,
            "password",
            "dog@gmail.com"
            );
        Role roleType4 = new Role("dog");
        roleType4.setRoleid(4);

//        u1.getUseremails()
//            .add(new Useremail(u1,
//                "admin@mymail.local"));
//        u1.getUseremails().get(1).setUseremailid(12);

//        u1.getRoles()
//            .add(new UserRoles(u1,
//                r2));

        u3.getRoles()
            .add(new UserRoles(u3, roleType4));

        u3.getUseremails()
            .add(new Useremail(u3, "dog@gmail.com"));
        u3.getUseremails().get(1).setUseremailid(41);

        Mockito.when(userRepository.save(any(User.class)))
            .thenReturn(u3);
        Mockito.when(roleService.findRoleById(4))
            .thenReturn(roleType4);

        User addUser = userService.save(u3);
        assertNotNull(addUser);
        assertEquals(user3Name.toLowerCase(),
            addUser.getUsername());

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