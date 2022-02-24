package team7.demo.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import team7.demo.login.models.Hospital;

import javax.transaction.Transactional;
import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
    @Query("select h from Hospital h where h.hospitalId = ?1")
    public Hospital findByHospitalId(long id);

    @Query("select h from Hospital h where h.trust.trustId = ?1")
    public List<Hospital> findByTrustId(long id);

    @Transactional
    @Modifying
    @Query("delete from Hospital h where h.hospitalId = ?1")
    void deleteByPK(long id);
}
