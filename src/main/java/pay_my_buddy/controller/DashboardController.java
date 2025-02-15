package pay_my_buddy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pay_my_buddy.model.Transaction;
import pay_my_buddy.model.User;
import pay_my_buddy.service.TransactionService;
import pay_my_buddy.service.UserService;

import java.util.Optional;

@Controller
public class DashboardController {


    private final TransactionService transactionService;
    private final UserService userService;

    public DashboardController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User connectedUser = userService.getConnectedUser();
        if (connectedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", connectedUser);
        model.addAttribute("SentTransactions", transactionService.getSentTransactions(connectedUser.getEmail()));
        model.addAttribute("ReceivedTransactions", transactionService.getReceivedTransactions(connectedUser.getEmail()));
        model.addAttribute("friends", connectedUser.getFriends());
        model.addAttribute("username", connectedUser.getUsername());
        model.addAttribute("balance", connectedUser.getBalance());
        return "dashboard";
    }

    @PostMapping("/dashboard/payment")
    public String handlePayment(@RequestParam("receiverEmail") String receiverEmail,
                                @RequestParam("description") String description,
                                @RequestParam("amount") double amount,
                                Model model){
        User sender = userService.getConnectedUser();

        if(sender == null){
            return "redirect:/login";
        }
        Optional<User> receiverOpt = userService.findByEmail(receiverEmail);
        if(receiverOpt.isEmpty()){
            model.addAttribute("error", "Destinataire non trouvé dans la base de données!");
            return "dashboard";
        }

        User receiver = receiverOpt.get();
        Transaction transaction = transactionService.createTransaction(sender.getId(), receiver.getId(), description, amount);

        if (transaction == null) {
            model.addAttribute("error", "Votre solde est insuffisant! Veuillez recharger votre compte.");
            return dashboard(model);
        }

        return "redirect:/dashboard";
    }
}
