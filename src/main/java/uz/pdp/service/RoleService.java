package uz.pdp.service;

import uz.pdp.entity.enums.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleService {
    private RoleService() {}

    public static List<String> getRoleList() {
        List<String> roleList = new ArrayList<>();
        for (Role role : Role.values()) {
            roleList.add(role.name());
        }
        return roleList;
    }
}
