package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import entity.User;
import model.ConstraintViolation;
import model.UserPrincipal;
import repository.RoleRepository;
import repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ConstraintViolation userConstraintViolation;

    @GetMapping("/users/list")
    public String listUsers(Model model) {

        model.addAttribute("userConstraintViolation", this.userConstraintViolation);
        model.addAttribute("users", userRepository.findAllByOrderByIdAsc());

        this.userConstraintViolation = null;

        return "users/list-users";
    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "users/login-form";
    }

    @GetMapping("/register-user")
    public String showRegisterUserForm(Model model) {

        User user = new User();
        user.setRole(roleRepository.findByName("ROLE_CUSTOMER"));

        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("user", user);

        return "users/register-or-update-user-form";
    }

    @PostMapping("/register-user")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("allRoles", roleRepository.findAll());

            return "users/register-or-update-user-form";
        }

        String userPassword = user.getPassword();

        user.setId(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        // immediate login after registration if not logged in user with "ROLE_ADMIN"
        if (!request.isUserInRole("ADMIN")) {
            try {
                // we use email as username
                request.login(user.getEmail(), userPassword);
                return "redirect:/pizzas/list";
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/users/list";
    }

    @GetMapping("/users/update/{userId}")
    public String showUpdateUserForm(@PathVariable int userId, Model model) {

        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("user", userRepository.findById(userId).get());

        return "users/register-or-update-user-form";
    }

    @PostMapping("/users/update")
    public String updateUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Authentication authentication, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("allRoles", roleRepository.findAll());

            return "users/register-or-update-user-form";
        }

        userRepository.save(user);



        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


        if (userPrincipal.getUsername().equals(user.getEmail())) {
            userPrincipal.getUser().setName(user.getName());;
        }

        return "redirect:/users/list";
    }

    @GetMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable int userId) {

        try {

            if (userRepository.findById(userId) != null) {
                userRepository.deleteById(userId);
            }
        } catch (DataIntegrityViolationException e) {

            this.userConstraintViolation = new ConstraintViolation(userId, "This user cannot be deleted now, it is assigned to the order.");

        } catch (Exception e) {

            System.out.println(">>> [source: CONTROLLER] Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/users/list";
    }
}