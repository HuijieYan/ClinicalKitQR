package team7.demo.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.demo.login.models.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
}
