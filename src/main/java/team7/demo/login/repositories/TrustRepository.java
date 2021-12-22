package team7.demo.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.demo.login.models.Trust;

public interface TrustRepository extends JpaRepository<Trust,Long> {
    public Trust findByTrustId(long id);
}
