package pay_my_buddy.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pay_my_buddy.exception.InsufficientBalanceException;
import pay_my_buddy.model.Transaction;
import pay_my_buddy.model.User;
import pay_my_buddy.repository.TransactionRepository;
import pay_my_buddy.repository.UserRepository;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Transaction createTransaction(Long senderId, Long receiverId, String description, double amount){

        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant de la transaction doit être positif");
        }

        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("L'id de l'expéditeur et du destinataire ne peut pas être null");
        }

        if (senderId.equals(receiverId)) {
            throw new IllegalStateException("Un utilisateur ne peut pas s'envoyer de l'argent à lui-même !");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(()-> new EntityNotFoundException("Aucun utilisateur trouvé avec l'id : " + senderId));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(()-> new EntityNotFoundException("Le destinataire n'est pas trouvé avec l'id : " + receiverId));

        if (sender.getBalance() < amount){
            throw new InsufficientBalanceException("Solde insuffisant pour effectuer la transaction");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDescription(description);
        transaction.setAmount(amount);

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        userRepository.save(sender);
        userRepository.save(receiver);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getSentTransactions(String email){
        return transactionRepository.findTransactionsSentByUser(email);
    }

    public List<Transaction> getReceivedTransactions(String email) {
        return transactionRepository.findTransactionsReceivedByUser(email);
    }
}
