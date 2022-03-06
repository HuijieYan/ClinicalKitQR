package ClinicalKitQR.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ClinicalKitQR.login.models.Hospital;

import javax.transaction.Transactional;
import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
    @Query("select h from Hospital h where h.hospitalId = ?1")
    Hospital findByHospitalId(long id);

    @Query("select h from Hospital h where h.trust.trustId = ?1")
    List<Hospital> findByTrustId(long id);

    @Transactional
    @Modifying
    @Query("delete from Hospital h where h.hospitalId = ?1")
    void deleteByPK(long id);

    @Transactional
    @Modifying
    @Query("update Hospital h set h.hospitalName=?2 where h.hospitalId = ?1")
    void update(long id,String name);
}