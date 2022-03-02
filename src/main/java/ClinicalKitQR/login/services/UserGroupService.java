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
        group.getHospitalId().addGroup(group);
        //we need to add group for usergroup to establish connection between hospital and usergroup
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
        if (username.length()==0||username == null){
            return null;
        }
        return repository.findByHospitalIdAndUsername(id,username);
    }

    public void update(long id, String username,String name,String pwd,String email,String specialty){
        repository.updateUserGroup(id,username,name,pwd,email,specialty);
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
        return repository.findALlAdmins();
    }

}
