package converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import entity.User;

@Component
public class UserToStringIdConverter implements Converter<User, String> {

    @Override
    public String convert(User user) {

        return String.valueOf(user.getId());
    }
}