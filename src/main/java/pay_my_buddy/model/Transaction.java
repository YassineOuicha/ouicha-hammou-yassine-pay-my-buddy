package pay_my_buddy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity @Data @Getter @Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn( name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn (name = "receiver_id", nullable = false)
    private User receiver;

    @Column(length = 512)
    private String description;

    @Column(nullable = false)
    private double amount;

}
