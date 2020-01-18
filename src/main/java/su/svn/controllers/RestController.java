package su.svn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import su.svn.db.front.services.UserService;

import su.svn.db.front.domain.User;
import java.util.List;

@RepositoryRestController
@Controller
public class RestController {

    private final UserService userService;

    @Autowired
    public RestController(UserService ormUserService) {
        this.userService = ormUserService;
    }

    @RequestMapping(
            value = "/api/rest",
            method = RequestMethod.GET)
    @ResponseBody
    public List<User> ormFindAllUsers() {
        return userService.queryFindAllUsersJPA();
    }

    @RequestMapping(
            value = "/api/rest",
            method = RequestMethod.PUT)
    @ResponseBody
    public List<User> ormInsertUserById(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "role") String role,
            @RequestParam(value = "password") String password) {
        return userService.insertUser(name, role, password);
    }

    @RequestMapping(
            value = "/api/rest",
            method = RequestMethod.DELETE)
    @ResponseBody
    public List<User> ormDeleteUserById(@RequestParam(value = "id") Integer id) {
        return userService.deleteUserById(id);
    }
}
