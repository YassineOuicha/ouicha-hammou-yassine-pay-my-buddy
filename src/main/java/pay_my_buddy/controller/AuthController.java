package pay_my_buddy.controller;


import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              Model model){
        User user = userService.findByEmail(email).orElse(null);
        if(user!=null){
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Incorrect email or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam("username") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 Model model){
        try {
            userService.registerUser(username, email, password);
            return "redirect:/login";
        } catch (Exception e){
            model.addAttribute("error", "Error while registering " + e.getMessage());
            return "register";
        }
    }
}
