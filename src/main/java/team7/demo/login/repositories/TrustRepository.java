package team7.demo.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team7.demo.login.models.Trust;

import javax.transaction.Transactional;

public interface TrustRepository extends JpaRepository<Trust,Long> {
    public Trust findByTrustId(long id);

    @Transactional
    @Modifying
    @Query("delete from Trust t where t.trustId = ?1")
    void deleteByPK(long id);
}
