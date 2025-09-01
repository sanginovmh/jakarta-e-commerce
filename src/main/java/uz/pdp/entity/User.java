package uz.pdp.entity;

import lombok.Data;
import uz.pdp.entity.enums.Role;

@Data
public class User {
    private Integer id;
    private String fullName;
    private String email;
    private String password;
    private Boolean enabled;
    private Role role;
}
