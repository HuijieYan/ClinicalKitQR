package team7.demo.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team7.demo.login.models.UserGroup;
import team7.demo.login.repositories.UserGroupRepository;

import java.util.ArrayList;
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
        if (repository.findByHospitalIdAndUsername(group.getHospital().getId(), group.getUsername())!=null){
            return null;
        }
        return repository.saveAndFlush(group);
    }

    public List<UserGroup> getAll(){
        return repository.findAll();
    }

    public UserGroup findByPK(long id,String username){
        if (username.length()==0||username == null){
            return null;
        }
        return repository.findByHospitalIdAndUsername(id,username);
    }

    public void update(long id, String username,String pwd,String name){
        repository.updateUserGroup(id,username,pwd,name);
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

}
