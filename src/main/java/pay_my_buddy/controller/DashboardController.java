package pay_my_buddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pay_my_buddy.exception.InsufficientBalanceException;
import pay_my_buddy.model.User;
import pay_my_buddy.service.TransactionService;
import pay_my_buddy.service.UserService;

import java.util.Optional;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

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
    public String handlePayment(@RequestParam("receiverId") Long receiverId,
                                @RequestParam("description") String description,
                                @RequestParam("amount") double amount,
                                RedirectAttributes redirectAttributes) {

        User sender = userService.getConnectedUser();
        if (sender == null) {
            return "redirect:/login";
        }

        if (amount <= 0) {
            redirectAttributes.addFlashAttribute("error", "Le montant de la transaction doit être supérieur à zéro.");
            return "redirect:/dashboard";
        }

        if (sender.getId().equals(receiverId)) {
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas vous envoyer de l'argent !");
            return "redirect:/dashboard";
        }

        Optional<User> receiverOpt = userService.findById(receiverId);
        if (receiverOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Destinataire non trouvé !");
            return "redirect:/dashboard";
        }

        try {
            transactionService.createTransaction(sender.getId(), receiverId, description, amount);
            redirectAttributes.addFlashAttribute("success", "Paiement effectué avec succès !");
        } catch (InsufficientBalanceException e) {
            logger.error("Paiement échoué : {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur lors du paiement", e);
            redirectAttributes.addFlashAttribute("error", "Une erreur inattendue est survenue.");
        }

        return "redirect:/dashboard";
    }
}
