package org.example.backend.models.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.example.backend.models.entities.UserDevice;

import java.io.Serializable;

@Data
public class UserDeviceInfo implements Serializable {

    private String id;

    private String details;
    private String location;

    @JsonCreator
    public UserDeviceInfo() {
    }

    public UserDeviceInfo(UserDevice userDevice) {
        this.id = userDevice.getId();

        this.details = userDevice.getDetails();
        this.location = userDevice.getLocation();
    }
}
