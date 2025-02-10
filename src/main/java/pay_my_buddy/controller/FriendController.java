package pay_my_buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;

import java.util.Optional;

@Controller
public class FriendController {

    @Autowired
    private UserService userService;

    @PostMapping("/friends/add")
    public String addFriend(@RequestParam("email") String email, Model model){
        User currentUser = userService.getConnectedUser();
        if(currentUser == null){
            model.addAttribute("error", "User not connected, please login first!");
            return "login";
        }
        Optional<User> friendOpt = userService.findByEmail(email);
        if(friendOpt.isPresent()){
            userService.addFriend(currentUser.getId(), friendOpt.get().getId());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "No corresponding user for this email, please enter a valid email!");
            return "connection";
        }
    }
}
