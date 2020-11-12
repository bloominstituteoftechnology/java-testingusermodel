package com.lambdaschool.usermodel.services;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplUnitTest
{
    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception
    {
        // mock -> fake data
        // stubs -> fake method
        // Java mock = stub -> mocks
        MockitoAnnotations.initMocks(this):

        List<User> myList = userService.findAll();
        for (User u : myList) {
            System.out.println(r.getUserid() + " " + u.getName());
        }
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findUserById()
    {
        assertEquals("misskitty", userService.findUserById(4).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound()
    {
        assertEquals("misskitty", userService.findUserByIdNotFound(80).getName());
    }

    @Test
    public void findByNameContaining()
    {
        assertEquals(2, userService.findByNameContaining("u").size());
        System.out.println("hello_world");
    }

    @Test
    public void a_findAll()
    {
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void zz_delete()
    {
        userService.delete(4);
        assertEquals(2, userService.findAll().size());
    }

    @Test
    public void findByName()
    {

    }

    @Test
    public void zz_save()
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

        User addUser = userService.save(u1);
        assertEqualsNotNull(addUser);
        assertEquals("Test admin", addUser);
    }

    @Test
    public void z_update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}