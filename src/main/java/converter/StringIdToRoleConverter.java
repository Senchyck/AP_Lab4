package converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import entity.Role;
import repository.RoleRepository;

@Component
public class StringIdToRoleConverter implements Converter<String, Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String roleId) {

        return roleRepository.findById(Integer.parseInt(roleId)).get();
    }

}