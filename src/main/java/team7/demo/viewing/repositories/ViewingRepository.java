package team7.demo.viewing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.demo.login.models.UserGroup;
import team7.demo.viewing.models.Viewing;

import java.time.LocalDate;
import java.util.List;

public interface ViewingRepository extends JpaRepository<Viewing, Long> {

    @Query("select v from Viewing v where v.equipmentId.equipmentId = ?1")
    public List<Viewing> getAllByEquipmentId(Long id);

    @Query("select v from  Viewing v where v.date = ?1")
    public List<Viewing> getAllByDate(LocalDate date);

    @Query("select  v from Viewing v where v.userGroup.username = ?1")
    public  List<Viewing> getAllByUserGroup(UserGroup userGroup);

}
