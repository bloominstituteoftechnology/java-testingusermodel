package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceFoundException;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTestUnitTestWithDB {

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  @MockBean
  private HelperFunctions helperFunctions;

  @Before
  public void setUp() throws Exception {

    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDB_findUserById() {
    System.out.println(userService.findUserById(4)
                                  .getUsername());
    assertEquals("testadmin",
                 userService.findUserById(4)
                .getUsername());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDB_findUserByIdNotFound() {
    assertEquals("testadmin",
                 userService.findUserById(10)
                            .getUsername());
  }

  @Test
  public void testDB_findByNameContaining() {

  }

  @Test
  public void testDB_findAll() {
    assertEquals(5,
                 userService.findAll().size());
  }

  @Test
  public void ztestDB_delete() {
    userService.delete(13);
    assertEquals(5,
                 userService.findAll().size());
  }

  @Test
  public void testDB_findByName() {
    assertEquals("testadmin",
                 userService.findByName("testadmin").getUsername());
  }

  @Test
  public void testDB_save() {
    Role newRole = new Role("ADMIN");
    newRole.setRoleid(1); //sets a role that already exist

    User newUser = new User("tagbokhana",
                            "password",
                            "tagbokhana@gmail.com");

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "tarah.agbokhana@gmail.com"));

    User addUser = userService.save(newUser);

    assertEquals("tagbokhana", addUser.getUsername());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDB_saveNotFound() {
    Role newRole = new Role("ADMIN");
    newRole.setRoleid(8); //sets a role that already exist

    User newUser = new User("tagbokhana",
                            "password",
                            "tagbokhana@gmail.com");

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "tarah.agbokhana@gmail.com"));

    User addUser = userService.save(newUser);

    assertEquals("tagbokhana", addUser.getUsername());
  }

  @Test
  public void testDB_savePut(){
    Role newRole = new Role("testuser");
    newRole.setRoleid(3);

    User newUser = new User("bebop",
                            "password",
                            "beep@lol.com");

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "beepbeep@lol.com"));
    newUser.setUserid(4);

    User addUser = userService.save(newUser);

    assertEquals("beepbeep@lol.com",
                 addUser.getUseremails()
                .get(0)
                .getUseremail());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDB_savePutNotFound(){
    Role newRole = new Role("testuser");
    newRole.setRoleid(3);

    User newUser = new User("bebop",
                            "password",
                            "beep@lol.com");

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "beepbeep@lol.com"));
    newUser.setUserid(40);

    User addUser = userService.save(newUser);

    assertEquals("beepbeep@lol.com",
                 addUser.getUseremails()
                        .get(0)
                        .getUseremail());
  }

  @Test
  public void testDB_update() {
    Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
           .thenReturn(true);

    Role newRole = new Role("testnewuser");
    newRole.setRoleid(1);

    User newUser = new User("tata",
                            "password",
                            "tata@agency.com");
    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "admin@agency.com"));
    newUser.getUseremails()
           .add(new Useremail(newUser, "manager@agency.com"));

    User updatedUser = userService.update(newUser, 4);

    int lastIndex = updatedUser.getUseremails()
        .size() - 1;
    assertEquals("manager@agency.com",
                 updatedUser.getUseremails()
                .get(lastIndex)
                .getUseremail());
  }


  @Test(expected = ResourceNotFoundException.class)
  public void testDB_updateNotFound() {
    Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
           .thenReturn(true);

    Role newRole = new Role("testnewuser");
    newRole.setRoleid(1);

    User newUser = new User("tata",
                            "password",
                            "tata@agency.com");
    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "admin@agency.com"));
    newUser.getUseremails()
           .add(new Useremail(newUser, "manager@agency.com"));

    User updatedUser = userService.update(newUser, 8);

    int lastIndex = updatedUser.getUseremails()
                               .size() - 1;
    assertEquals("manager@agency.com",
                 updatedUser.getUseremails()
                            .get(lastIndex)
                            .getUseremail());
  }

  @Test
  public void ztestDB_deleteAll() {
    userService.deleteAll();
    assertEquals(0,
                 userService.findAll().size());
  }

  @Test
  public void testDB_findAllRoles() {
    assertEquals(3,
                 roleService.findAll().size());
  }

  @Test
  public void testDB_findRoleByName() {
    assertEquals("testadmin".toUpperCase(),
                 roleService.findByName("testadmin".toUpperCase()).getName());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDB_findRoleByNameNotFound() {
    assertEquals("bunnyrabbit".toUpperCase(),
                 roleService.findByName("bunnyrabbit".toUpperCase()).getName());
  }

  @Test(expected = ResourceFoundException.class)
  public void testDB_saveRoleNotFound(){
    Role newRole = new Role("ADMIN");
    newRole.setRoleid(99);

    User newUser = new User("tagbokhana",
                            "password",
                            "tagbokhana@gmail.com");

    User newUser2 = new User("tagbokhana",
                            "password",
                            "tagbokhana@gmail.com");
    newRole.getUsers()
           .add(new UserRoles(newUser, newRole));
    newRole.getUsers()
           .add(new UserRoles(newUser2, newRole));

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "tarah.agbokhana@gmail.com"));

    Role addRole = roleService.save(newRole);

    assertEquals(2, addRole.getUsers());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testDB_updateRoleNotFound(){
    Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
           .thenReturn(true);

    User newUser = new User("tata",
                            "password",
                            "tata@agency.com");
    newUser.setUserid(48);

    Role newRole = new Role("testnewuser");
//    newRole.setRoleid(50);

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "admin@agency.com"));
    newUser.getUseremails()
           .add(new Useremail(newUser, "manager@agency.com"));

    Role updatedRole = roleService.update(8L, newRole);

    assertEquals(50,
                 updatedRole.getRoleid());
  }
}