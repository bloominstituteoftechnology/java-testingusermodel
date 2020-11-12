package com.lambdaschool.usermodel.controllers;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerUnitTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
        HelperFunctions helperFunctions;

    List<User> userList;

    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();

        Role roleType1 = new Role("Test admin");
        Role roleType2 = new Role("Test user");
        Role roleType3 = new Role("Test data");

        roleType1.setRoleid(1);
        roleType2.setRoleid(2);
        roleType3.setRoleid(3);

        public void run(String[] args) throws
        Exception
        {
//            userService.deleteAll();
//            roleService.deleteAll();
//            Role r1 = new Role("admin");
//            Role r2 = new Role("user");
//            Role r3 = new Role("data");
//
//            r1 = roleService.save(r1);
//            r2 = roleService.save(r2);
//            r3 = roleService.save(r3);

            // admin, data, user
            User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local");

            //One to Many
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

            userService.save(u1);

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
            userService.save(u2);

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
            userService.save(u3);

            User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
            u4.getRoles()
                .add(new UserRoles(u4,
                    r2));
            userService.save(u4);

            User u5 = new User("misskitty",
                "password",
                "misskitty@school.lambda");
            u5.getRoles()
                .add(new UserRoles(u5,
                    r2));
            userService.save(u5);

            if (false)
            {
                // using JavaFaker create a bunch of regular users
                // https://www.baeldung.com/java-faker
                // https://www.baeldung.com/regular-expressions-java

                FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"),
                    new RandomService());
                Faker nameFaker = new Faker(new Locale("en-US"));

                for (int i = 0; i < 25; i++)
                {
                    new User();
                    User fakeUser;

                    fakeUser = new User(nameFaker.name()
                        .username(),
                        "password",
                        nameFaker.internet()
                            .emailAddress());
                    fakeUser.getRoles()
                        .add(new UserRoles(fakeUser,
                            r2));
                    fakeUser.getUseremails()
                        .add(new Useremail(fakeUser,
                            fakeValuesService.bothify("????##@gmail.com")));
                    userService.save(fakeUser);
                }
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listAllUsers() throws Exception
    {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String testResult = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList);

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getUserByIdNotFound() throws Exception
    {
        String apiUrl = "/users/user/300";
        Mockito.when(userService.findAll()).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String testResult = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = "";

        assertEquals(expectedResult, testResult);
    }
    }

    @Test
    public void getUserByName()
    {
    }

    @Test
    public void getUserLikeName()
    {
    }

    @Test
    public void addNewUser()
    {
        User u1 = new User("Test admin",
            "password",
            "admin@lambdaschool.local");

        Role roleType1 = new Role("barnbarn");
        roleType1.setRoleid(1);

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
        u1.setUserid(13);

        String apiUrl = "/users/user";

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        Mockito.when(userService.save(any(User.class))).thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userString);
        mockMvc.perform(rb).andExpect(status().isCreated)).andDo(MockMvcResultHandlers.print());
    }
    }

    @Test
    public void updateFullUser()
    {
    }

    @Test
    public void updateUser()
    {
    }

    @Test
    public void deleteUserById()
    {
    }
}