package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //dont call real userService, we will tell ya what to do later in the code
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {

        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1); //have to manually set id's because repo is not actually being called
        Role r2 = new Role("user");
        r2.setRoleid(2); //have to manually set id's because repo is not actually being called
        Role r3 = new Role("data");
        r3.setRoleid(3); //have to manually set id's because repo is not actually being called

        // admin, data, user
        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.setUserid(11);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .get(0)
                .setUseremailid(20);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails()
                .get(1)
                .setUseremailid(21);

        userList.add(u1);

        // data, user
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.setUserid(12); // manually setting id
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .get(0)
                .setUseremailid(30); // manually setting id

        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .get(1)
                .setUseremailid(31); // manually setting id
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails()
                .get(2)
                .setUseremailid(32); // manually setting id
        userList.add(u2); // add to list

        // user
        User u3 = new User("barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.setUserid(13); // manually setting id
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails()
                .get(0)
                .setUseremailid(40); // manually setting id
        userList.add(u3); // add to list

        User u4 = new User("batman2",
                "password",
                "batman2@school.lambda");
        u4.getRoles().add(new UserRoles(u4, r2));
        u4.setUserid(14); // manually setting id
        userList.add(u4); // add to list

        User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
        u5.getRoles().add(new UserRoles(u5, r2));
        u5.setUserid(15); // manually setting id
        userList.add(u5);  // add to list
    }

    @After
    public void tearDown() throws Exception { }

    @Test
    public void listAllUsers() throws Exception {

        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);
        // when you see "userService.findAll()" dont call it use userlist

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        // create request (GET request to apiUrl and accept json data)

        // perform request
        MvcResult r = mockMvc.perform(rb).andReturn();
        // capture request as string
        String tr = r.getResponse().getContentAsString();

        // Object mapper does conversions
        ObjectMapper mapper = new ObjectMapper();
        // take string and write it as a sring value in JSON
        String er = mapper.writeValueAsString(userList);

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void getUserById() throws Exception {

        String apiUrl = "/users/user/13";
        Mockito.when(userService.findUserById(13)).thenReturn(userList.get(2));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(2));

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        String apiUrl = "/users/user/5000";
        Mockito.when(userService.findUserById(5000)).thenReturn(null); // EXPECTING NULL HERE

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = "";

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void getUserByName() throws Exception {

        String apiUrl = "/users/user/name/barnbarn";
        Mockito.when(userService.findByName("barnbarn")).thenReturn(userList.get(2));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(2));

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void getUserLikeName() throws Exception {

        String apiUrl = "/users/user/name/like/a";

        List<User> filteredList = userList.stream()
                .filter(u -> u.getUsername().contains("a"))
                .collect(Collectors.toList());

        Mockito.when(userService.findByNameContaining("a")).thenReturn(filteredList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(filteredList);

        //printing results
        System.out.println("Expected Result: " + er);
        System.out.println("Test Result: " + tr);

        // make sure expected result = test result
        assertEquals("Success", er, tr);
    }

    @Test
    public void addNewUser() throws Exception {

        String apiUrl = "/users/user";

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);

        User user = new User("batman",
                "password",
                "batman@example.com");
        user.getRoles().add(new UserRoles(user, r1));
        user.getRoles().add(new UserRoles(user, r2));
        user.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);

        Mockito.when(userService.save(any(User.class))).thenReturn(user);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateFullUser() throws Exception {
        String apiUrl = "/users/user/{userid}";

        Role r2 = new Role("user");
        r2.setRoleid(2);

        // create a user
        String newUsername = "batman2";
        User user = new User(newUsername,
                "password",
                "batman2@example.com");
        user.getRoles().add(new UserRoles(user, r2));
        user.setUserid(14);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);

        Mockito.when(userService.save(any(User.class))).thenReturn(user);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 14)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception {
        String apiUrl = "/users/user/{userid}";

        User user = userList.get(1);
        user.setPrimaryemail("cinnamon@example.com");

        Mockito.when(userService.update(user, 12)).thenReturn(user);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(user);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, user.getUserid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}