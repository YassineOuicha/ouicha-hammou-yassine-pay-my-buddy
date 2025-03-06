package pay_my_buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.User;
import pay_my_buddy.service.UserService;

import java.util.Optional;

@RequestMapping("/friends")
@Controller
public class FriendController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddFriendPage(Model model){
        User currentUser = userService.getConnectedUser();
        if (currentUser != null) {
            model.addAttribute("username", currentUser.getUsername());
        }
        return "friend";
    }

    @PostMapping("/add")
    public String addFriend(@RequestParam("email") String email, Model model){
        User currentUser = userService.getConnectedUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Optional<User> friendOpt = userService.findByEmail(email);

        if (friendOpt.isEmpty()) {
            model.addAttribute("error", "Pas d'utilisateur correspondant à cette adresse email : " + email);
            return reloadFriendPage(model, currentUser);
        }

        User friend = friendOpt.get();

        if (friend.getId().equals(currentUser.getId())) {
            model.addAttribute("error", "Vous ne pouvez pas vous ajouter vous-même en tant qu'ami !");
            return reloadFriendPage(model, currentUser);
        }

        if (currentUser.getFriends().contains(friend)) {
            model.addAttribute("error", "Cet utilisateur est déjà dans votre liste d'amis !");
            return reloadFriendPage(model, currentUser);
        }

        userService.addFriend(currentUser.getId(), friend.getId());
        return "redirect:/dashboard";
    }

    private String reloadFriendPage(Model model, User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("friends", user.getFriends());
        return "friend";
    }
}
