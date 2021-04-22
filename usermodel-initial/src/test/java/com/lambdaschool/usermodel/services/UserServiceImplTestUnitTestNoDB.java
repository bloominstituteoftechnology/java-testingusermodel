package com.lambdaschool.usermodel.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.*;
import com.lambdaschool.usermodel.repository.RoleRepository;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
    properties = {
        "command.line.runner.enabled=false"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTestUnitTestNoDB {

  @Autowired
  private UserService userService;

  // mocks -> fake data
  // stubs -> fake methods
  // Java -> mocks

  @MockBean
  private UserRepository userrepos;

  @MockBean
  private RoleRepository rolerepos;

  private List<User> userList;

  @Before
  public void setUp() throws Exception {
    userList = new ArrayList<>();

    Role r1 = new Role("testadmin");
    Role r2 = new Role("testuser");
    Role r3 = new Role("testdata");

    r1.setRoleid(1);
    r2.setRoleid(2);
    r3.setRoleid(3);

    // admin, data, user
    User u1 = new User("testadmin",
                       "password",
                       "admin@lambdaschool.local");
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

    // data, user
    User u2 = new User("testcinnamon",
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
    userList.add(u2);

    // user
    User u3 = new User("testbarnbarn",
                       "ILuvM4th!",
                       "barnbarn@lambdaschool.local");
    u3.getRoles()
      .add(new UserRoles(u3,
                         r2));
    u3.getUseremails()
      .add(new Useremail(u3,
                         "barnbarn@email.local"));
    userList.add(u3);

    User u4 = new User("testputtat",
                       "password",
                       "puttat@school.lambda");
    u4.getRoles()
      .add(new UserRoles(u4,
                         r2));
    userList.add(u4);

    User u5 = new User("testmisskitty",
                       "password",
                       "misskitty@school.lambda");
    u5.getRoles()
      .add(new UserRoles(u5,
                         r2));
    userList.add(u5);

    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test_findUserById() {
    Mockito.when(userrepos.findById(1L))
           .thenReturn(Optional.of(userList.get(0)));

    assertEquals("testadmin", userService.findUserById(1).getUsername());
  }
  @Test(expected = ResourceNotFoundException.class)
  public void test_findUserByIdNotFound()
  {
    Mockito.when(userrepos.findById(10000L))
           .thenThrow(ResourceNotFoundException.class);

    assertEquals("testadmin",
                 userService.findUserById(10000)
                                  .getUsername());
  }

  @Test
  public void test_findByNameContaining() {
    Mockito.when(userrepos.findByUsernameContainingIgnoreCase("testbarnbarn"))
           .thenReturn(userList);

    assertEquals(5, userService.findByNameContaining("testbarnbarn")
                .size());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void test_findUserByNameFailed()
  {
    Mockito.when(userrepos.findByUsername("Lambda"))
           .thenThrow(ResourceNotFoundException.class);

    assertEquals("Lambda",
                 userService.findByName("testmisskitty")
                                  .getUsername());
  }

  @Test
  public void test_findAll() {
    Mockito.when(userrepos.findAll()).thenReturn(userList);

    System.out.println(userService.findAll().toString());
    assertEquals(5, userService.findAll().size());
  }

  @Test
  public void test_delete() {
    Mockito.when(userrepos.findById(6L)).thenReturn(
        Optional.of(userList.get(0)));

    Mockito.doNothing()
           .when(userrepos)
           .deleteById(6L);

    userService.delete(6);
    assertEquals(5, userList.size());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void test_deletefailed()
  {
    Mockito.when(userrepos.findById(777L))
           .thenReturn(Optional.empty());

    Mockito.doNothing()
           .when(userrepos)
           .deleteById(777L);

    userService.delete(777L);
    assertEquals(5,
                 userList.size());
  }

  @Test
  public void test_findByName() {
    Mockito.when(userrepos.findByUsername("testcinnamon"))
           .thenReturn(userList.get(1));

    assertEquals("testcinnamon", userService.findByName(
        "testCinnaMon").getUsername());
  }

  @Test
  public void test_save() {
    User newUser = new User(
        "testtarah",
        "password",
        "tarah.agbokhana@gmail.com");

    Role newRole = new Role("DATA-MGR");
    newRole.setRoleid(1);

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "tagbokhana@gmail.com"));

    Mockito.when(userrepos.save(any(User.class)))
    .thenReturn(newUser);

    Mockito.when(rolerepos.findById(1L))
           .thenReturn(Optional.of(newRole));

    User addUser = userService.save(newUser);
    assertNotNull(addUser);
    assertEquals("testtarah",addUser.getUsername());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void test_saveRoleNotFound() {
    User newUser = new User(
        "testtarah",
        "password",
        "tarah.agbokhana@gmail.com");

    Role newRole = new Role("DATA-MGR");
    newRole.setRoleid(1);

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole));
    newUser.getUseremails()
           .add(new Useremail(newUser, "tagbokhana@gmail.com"));

    Mockito.when(userrepos.save(any(User.class)))
           .thenReturn(newUser);

    Mockito.when(rolerepos.findById(1L))
           .thenReturn(Optional.empty());//return no role

    User addUser = userService.save(newUser);
    assertNotNull(addUser);
    assertEquals("testtarah",addUser.getUsername());
  }

  @Test
  public void test_save_put(){
    User newUser = new User(
        "testtarah",
        "password",
        "tarah.agbokhana@gmail.com");

    newUser.setUserid(7);

    Role newRole1 = new Role("Unknown1");
    newRole1.setRoleid(1);

    Role newRole2 = new Role("Unknown2");
    newRole2.setRoleid(2);

    newUser.getRoles()
           .clear();

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole1));
    newUser.getRoles()
           .add(new UserRoles(newUser, newRole2));

    newUser.getUseremails()
           .add(new Useremail(newUser, "tagbokhana@gmail.com"));

    Mockito.when(userrepos.findById(7L))
           .thenReturn(Optional.of(newUser));

    Mockito.when(rolerepos.findById(1L))
           .thenReturn(Optional.of(newRole1));

    Mockito.when(rolerepos.findById(2L))
           .thenReturn(Optional.of(newRole2));

    Mockito.when(userrepos.save(any(User.class)))
           .thenReturn(newUser);

    assertEquals(7L,
                 userService.save(newUser).getUserid());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void test_save_put_failed() {
    User newUser = new User(
        "testtarah",
        "password",
        "tarah.agbokhana@gmail.com");

    newUser.setUserid(7);

    Role newRole1 = new Role("Unknown1");
    newRole1.setRoleid(1);

    Role newRole2 = new Role("Unknown2");
    newRole2.setRoleid(2);

    newUser.getRoles()
           .clear();

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole1));
    newUser.getRoles()
           .add(new UserRoles(newUser, newRole2));

    newUser.getUseremails()
           .add(new Useremail(newUser, "tagbokhana@gmail.com"));

    Mockito.when(userrepos.findById(77L))
           .thenThrow(ResourceNotFoundException.class);

    Mockito.when(userrepos.save(any(User.class)))
           .thenReturn(newUser);

    Mockito.when(rolerepos.findById(1L))
           .thenReturn(Optional.of(newRole1));

    Mockito.when(rolerepos.findById(2L))
           .thenReturn(Optional.of(newRole2));

    assertEquals(77L,
                 userService.save(newUser).getUserid());
  }

//  @Test
//  public void test_update() throws Exception {
//    User newUser = new User(
//        "testtarah",
//        "password",
//        "tarah.agbokhana@gmail.com");
//
//    newUser.setUserid(22);
//
//    Role newRole1 = new Role("Unknown1");
//    newRole1.setRoleid(1);
//
//    Role newRole2 = new Role("Unknown2");
//    newRole2.setRoleid(2);
//
//    newUser.getRoles()
//           .add(new UserRoles(newUser, newRole1));
//    newUser.getRoles()
//           .add(new UserRoles(newUser, newRole2));
//
//    newUser.getUseremails()
//           .add(new Useremail(newUser, "tagbokhana@gmail.com"));
//    System.out.println(newUser.getUsername());
//
//
//    // I need a copy of "newUser" to send to update so the original "newUser"
//    // is not changed. I am using Jackson to make a clone of the object
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    User clonedUser = objectMapper
//        .readValue(objectMapper.writeValueAsString(newUser), User.class);
//
//    Mockito.when(userrepos.findById(22L))
//           .thenReturn(Optional.of(clonedUser));
//
//    System.out.println(clonedUser.getUsername());
//
//    Mockito.when(rolerepos.findById(1L))
//           .thenReturn(Optional.of(newRole1));
//
//    Mockito.when(rolerepos.findById(2L))
//           .thenReturn(Optional.of(newRole2));
//
//    Mockito.when(userrepos.save(any(User.class)))
//           .thenReturn(newUser);
//
//    User addUser = userService.update(newUser, 22L);
//    System.out.println("adduser: " + addUser.getUsername());
//
//    String name = "testtarah";
//    assertNotNull(addUser);
//    assertEquals(name,
//                 addUser.getUsername());
//  }

  @Test(expected = ResourceNotFoundException.class)
  public void test_update_failed() throws Exception {
    User newUser = new User(
        "testtarah",
        "password",
        "tarah.agbokhana@gmail.com");

    newUser.setUserid(18);

    Role newRole1 = new Role("Unknown1");
    newRole1.setRoleid(1);

    Role newRole2 = new Role("Unknown2");
    newRole2.setRoleid(2);

    newUser.getRoles()
           .clear();

    newUser.getRoles()
           .add(new UserRoles(newUser, newRole1));
    newUser.getRoles()
           .add(new UserRoles(newUser, newRole2));

    newUser.getUseremails()
           .add(new Useremail(newUser, "tagbokhana@gmail.com"));

    Mockito.when(userrepos.findById(18L))
           .thenThrow(ResourceNotFoundException.class);

    Mockito.when(rolerepos.findById(1L))
           .thenReturn(Optional.of(newRole1));

    Mockito.when(rolerepos.findById(2L))
           .thenReturn(Optional.of(newRole2));

    Mockito.when(userrepos.save(any(User.class)))
           .thenReturn(newUser);

    User addUser = userService.update(newUser, 18);

    assertNotNull(addUser);
    assertEquals("testtarah",
                 addUser.getUsername());
  }

  @Test
  public void test_deleteAll() {
    Mockito.when(userrepos.findAll()).thenReturn(userList);

    Mockito.doNothing()
           .when(userrepos)
           .deleteAll();

    userService.deleteAll();

    assertEquals(5, userList.size());
  }

}