package ru.javamentor.springbootcrud.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javamentor.springbootcrud.model.Role;
import ru.javamentor.springbootcrud.model.User;
import ru.javamentor.springbootcrud.service.UserService;
import ru.javamentor.springbootcrud.util.UserValidator;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/index")
    public String index(Model model) {

        return "index";
    }


    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView saveUser(@ModelAttribute("user") User user, BindingResult result,
                                 @RequestParam(value = "userRole", required = false) String userRole,
                                 @RequestParam(value = "adminRole", required = false) String adminRole) {
        ModelAndView modelAndView = new ModelAndView();
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            modelAndView.setViewName("registration");
            return modelAndView;
        }
        setUserRoles(user, userRole, adminRole);
        userService.saveUser(user);
        modelAndView.setViewName("user");
        return modelAndView;
    }


    @GetMapping(value = "/user")
    public ModelAndView userForm(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView();//(ModelAndView modelAndView, Principal principal) {
        String email = authentication.getName();

        User user = (User) userService.loadUserByUsername(email);
        String titleRole = "USER";
        for (Role role : user.getRoles()) {
            if (role.equals(Role.ADMIN)) {
                titleRole = "ADMIN";
                break;
            }
        }

        modelAndView.addObject("titleRole", titleRole);
        modelAndView.addObject("user", user);
        modelAndView.addObject("authentication", authentication);
        modelAndView.setViewName("user");
        return modelAndView;
    }


    @GetMapping("/admin")
    public ModelAndView admin(ModelAndView modelAndView, Principal principal) {
        String email = principal.getName();
        User user = (User) userService.loadUserByUsername(email);
        String titleRole = "ADMIN";
        List<User> users = userService.findAll();
        modelAndView.addObject("user", user);
        modelAndView.addObject("titleRole", titleRole);
        modelAndView.addObject("users", users);
        modelAndView.setViewName("admin-page");
        return modelAndView;
    }


    @GetMapping("/admin/add")
    public ModelAndView addGet() {
        ModelAndView model = new ModelAndView();
        model.setViewName("add-new-user");
        model.addObject("user", new User());
        model.addObject("roles", new HashSet<Role>());
        return model;
    }


    @PostMapping({"/admin/add"})
    public ModelAndView addPost(@ModelAttribute("user") User user, BindingResult result,
                                @RequestParam(value = "userRole", required = false) String userRole,
                                @RequestParam(value = "adminRole", required = false) String adminRole) {

        userValidator.validate(user, result);
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("add-new-user");
            return modelAndView;
        }
        setUserRoles(user, userRole, adminRole);
        userService.saveUser(user);
        return new ModelAndView("redirect:/admin");
    }


    @GetMapping("/admin/update")
    public ModelAndView updateGet(@RequestParam Long id, ModelAndView model) {
        model.setViewName("edit-user");
        User user = userService.findById(id);
        model.addObject("user", user);
        model.addObject("roles", new HashSet<Role>());
        return model;
    }

    @PostMapping("/admin/update")
    public ModelAndView updatePost(@ModelAttribute("admin/user") User user,
                                   @RequestParam(value = "userRole", required = false) String userRole,
                                   @RequestParam(value = "adminRole", required = false) String adminRole) {

        setUserRoles(user, userRole, adminRole);
        userService.saveUser(user);
        return new ModelAndView("redirect:/admin");
    }

    private void setUserRoles(@ModelAttribute("admin/user") User user, @RequestParam(value = "userRole", required = false) String userRole, @RequestParam(value = "adminRole", required = false) String adminRole) {
        Set<Role> roles = new HashSet<>();
        if (userRole != null) {
            roles.add(Role.USER);
        }
        if (adminRole != null) {
            roles.add(Role.ADMIN);
        }
        user.setRoles(roles);
    }


    @GetMapping("/admin/delete{id}")
    public ModelAndView deleteGet(@RequestParam("id") Long id, ModelAndView model) {
        model.setViewName("delete-user");
        User user = userService.findById(id);
        model.addObject("user", user);
        model.addObject("roles", new HashSet<Role>());
        return model;
    }


    @PostMapping("/admin/delete{id}")
    public ModelAndView deletePost(@RequestParam("id") Long id, ModelAndView model) {
        userService.deleteById(id);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping(value = "/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }
        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }
        return "login";
    }


    @GetMapping(value = {"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }


}