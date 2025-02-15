package pay_my_buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pay_my_buddy.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sender.email = :email")
    List<Transaction> findTransactionsReceivedByUser(String email);

    @Query("SELECT t FROM Transaction t WHERE t.receiver.email = :email")
    List<Transaction> findTransactionsSentByUser(String email);
}
