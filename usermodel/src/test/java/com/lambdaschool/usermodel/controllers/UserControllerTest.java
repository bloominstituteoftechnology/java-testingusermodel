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

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean // don't want to call the service bc it wouldn't be a unit test - it would be implementation
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception
    {
        // build DB (list)
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("Test admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(15);
        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(11);
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(12);


        // data, user
        User u2 = new User("Test cinnamon",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.setUserid(16);
        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));
        u2.getUseremails()
            .add(new Useremail(u2,
                "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(13);
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(14);
        u2.getUseremails()
            .add(new Useremail(u2,
                "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(15);


        // user
        User u3 = new User("Test barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");
        u3.setUserid(17);
        u3.getRoles().add(new UserRoles(u3, r2));
        u3.getUseremails()
            .add(new Useremail(u3,
                "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(16);


        User u4 = new User("Test puttat",
            "password",
            "puttat@school.lambda");
        u4.setUserid(18);
        u4.getRoles().add(new UserRoles(u4, r2));

        User u5 = new User("Test misskitty",
            "password",
            "misskitty@school.lambda");
        u5.setUserid(19);
        u5.getRoles().add(new UserRoles(u5, r2));

        // add to list
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listAllUsers() throws Exception
    {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList); // when this method gets called, return userList

        // call endpoint
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON); // build the request
        MvcResult r = mockMvc.perform(rb).andReturn(); // run the request
        String tr = r.getResponse().getContentAsString(); // get response back and convert to string

        ObjectMapper mapper = new ObjectMapper(); // manually use Jackson to do our conversion
        String er = mapper.writeValueAsString(userList); // convert to string and will already be in JSON format

        // compare test results (tr) and expected results (er)
        assertEquals(er, tr);
    }

    @Test
    public void getUserById() throws Exception
    {
        String apiUrl = "/users/user/7";
        Mockito.when(userService.findUserById(7)).thenReturn(userList.get(0)); // if we're doing a 7, then return 1st user

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(er, tr);
    }

//    @Test
//    public void getUserByIdNotFound() throws Exception
//    {
//        String apiUrl = "/users/user/100";
//        Mockito.when(userService.findUserById(100)).thenReturn(null); // if we're doing a 12, then return 1st user
//
//        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
//        MvcResult r = mockMvc.perform(rb).andReturn();
//        String tr = r.getResponse().getContentAsString();
//
//        String er = "";
//
//        assertEquals(er, tr);
//    }

    @Test
    public void getUserByName() throws Exception
    {
        String apiUrl = "/users/user/name/testing";

        Mockito.when(userService.findByName("testing")).thenReturn(userList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void getUserLikeName() throws Exception
    {
        String apiUrl = "/users/user/name/like/cin";

        Mockito.when(userService.findByNameContaining(any(String.class))).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(er, tr);
    }

    @Test
    public void addNewUser() throws Exception
    {
        String apiUrl = "/users/user";

        // create user obj
        String user3username = "test alex";
        User u3 = new User(user3username,
            "password",
            "alex@school.lambda");
        u3.setUserid(40);

        // create user
        Role r1 = new Role("peach");
        r1.setRoleid(1); // has to match id that's already in our system
        Role r2 = new Role("blueberry");
        r2.setRoleid(2);
        u3.getRoles().add(new UserRoles(u3, r1)); // add to user
        u3.getRoles().add(new UserRoles(u3, r2));

        u3.getUseremails().add(new Useremail(u3, "lauren2@school.lambda"));

        // convert to json
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u3);

        // mock up saved procedure
        Mockito.when(userService.save(any(User.class))).thenReturn(u3); // save java obj (doesn't matter which) but always return u3

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON) // content sent with the request is app json
            .content(userString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() throws Exception
    {
        String apiUrl = "/users/user/{userid}";

        // build a user
        User u1 = new User();
        u1.setUserid(13);
        u1.setUsername("the new test barnbarn");
        u1.setPassword("password");
        u1.setPrimaryemail("testingbarnbarn@lambdaschool.local");

        Mockito.when(userService.update(u1, 13)).thenReturn(userList.get(0));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 13)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb)
            .andExpect(status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception
    {
        String apiUrl = "/users/user/{userid}";

        // build a user
        User u1 = new User();
        u1.setUserid(7);
        u1.setUsername("the new test cinnamon");
        u1.setPassword("password");
        u1.setPrimaryemail("testingcinnamon@lambdaschool.local");

        Mockito.when(userService.update(u1, 7)).thenReturn(userList.get(0));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 7)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb)
            .andExpect(status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception
    {
        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "3")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
            .andExpect(status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
    }
}