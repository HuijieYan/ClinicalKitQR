package team7.demo.login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.login.models.Specialty;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, String> {
    @Query("select s from Specialty s where s.specialty = ?1")
    public Specialty findBySpecialty(String specialty);

    @Query("select s from Specialty s")
    public List<Specialty> findAll();
}
