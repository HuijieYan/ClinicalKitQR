package ClinicalKitQR.login.services;

import ClinicalKitQR.login.models.UserGroup;
import ClinicalKitQR.login.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupService {
    private final UserGroupRepository repository;

    @Autowired
    //automatically assign repo
    public UserGroupService(UserGroupRepository repository){
        this.repository = repository;
    }

    public UserGroup save(UserGroup group){
        if (repository.findByHospitalIdAndUsername(group.getHospitalId().getHospitalId(), group.getUsername())!=null){
            return null;
        }
        return repository.save(group);
    }

    public List<UserGroup> getAll(){
        return repository.findAll();
    }

    public List<UserGroup> getAllByHospital(long id){
        return repository.findAllByHospitalId(id);
    }

    public List<UserGroup> getAllByTrust(long id){
        return repository.findAllByTrustId(id);
    }

    public void delete(long hospitalId,String username){
        repository.deleteByPK(hospitalId,username);
    }

    public UserGroup findByPK(long id,String username){
        if (username == null||username.length()==0){
            return null;
        }
        return repository.findByHospitalIdAndUsername(id,username);
    }

    public void update(long id, String username,String name,String pwd,String email,String specialty,boolean isAdmin){
        repository.updateUserGroup(id,username,name,pwd,email,specialty,isAdmin);
    }

    public boolean login(long id,String username,String pwd){
        UserGroup group = this.findByPK(id,username);
        if (group == null){
            return false;
        }
        if (group.getPassword().equals(pwd)){
            return true;
        }
        return false;
    }

    public List<UserGroup> getAllAdmins(){
        return repository.findAllAdmins();
    }

}
