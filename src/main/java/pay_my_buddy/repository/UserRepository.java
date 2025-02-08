package pay_my_buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pay_my_buddy.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
