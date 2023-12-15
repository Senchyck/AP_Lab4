package converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import entity.Role;

@Component
public class RoleToStringIdConverter implements Converter<Role, String> {

    @Override
    public String convert(Role role) {

        return String.valueOf(role.getId());
    }
}
