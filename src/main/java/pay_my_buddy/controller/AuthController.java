package pay_my_buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


    @PostMapping("/register")
    public String handleRegister(@RequestParam("username") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 Model model) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            userService.saveUser(user);

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            return "register";
        }
    }
}
