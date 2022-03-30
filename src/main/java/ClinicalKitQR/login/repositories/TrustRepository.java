package ClinicalKitQR.login.repositories;

import ClinicalKitQR.login.models.Trust;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TrustRepository extends JpaRepository<Trust,Long> {
    Trust findByTrustId(long id);

    @Transactional
    @Modifying
    @Query("delete from Trust t where t.trustId = ?1")
    void deleteByPK(long id);
}
