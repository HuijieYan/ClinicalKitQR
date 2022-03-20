package ClinicalKitQR.viewing.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ClinicalKitQR.login.models.UserGroup;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EquipmentViewing {
    private UserGroup userGroup;
    private Long viewCount;


    public EquipmentViewing(UserGroup userGroup, Long viewCount) {
        this.userGroup = userGroup;
        this.viewCount = viewCount;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
