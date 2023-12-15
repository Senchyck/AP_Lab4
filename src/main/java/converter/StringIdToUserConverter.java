package converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import entity.User;
import repository.UserRepository;


public class StringIdToUserConverter implements Converter<String, User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User convert(String userId) {

        int userIdAsInt = Integer.parseInt(userId);

        return userRepository.findById(userIdAsInt).get();
    }
}
