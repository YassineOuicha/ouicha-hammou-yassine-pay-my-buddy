package pay_my_buddy.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

        User sender = userRepository.findById(senderId)
                .orElseThrow(()-> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(()-> new RuntimeException("Receiver not found"));

        if (sender.getBalance() < amount){
            throw new RuntimeException("Insufficient balance");
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

    public List<Transaction> getTransactionsForUser(Long userId){
        return transactionRepository.findBySenderIdOrReceiverId(userId, userId);
    }
}
