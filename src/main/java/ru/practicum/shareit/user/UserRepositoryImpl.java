package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.RepositoryMain;

import java.util.*;

@Repository
public class UserRepositoryImpl implements RepositoryMain<User> {

    private Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User save(User user) {
        if (!users.containsValue(user) && checkEmail(user)) {
            user.setId(getId());
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    public User findId(long id) {
        return users.get(id);
    }

    @Override

    public User update(User user, User userNewData) {
        if (users.containsKey(user.getId())) {
            User userUpdate = users.get(user.getId());
            if (userNewData.getEmail() != null) {
                userUpdate.setEmail(userNewData.getEmail());
            }
            if (userNewData.getName() != null) {
                userUpdate.setName(userNewData.getName());
            }
            return userUpdate;
        }
        return null;
    }

    private long getId() {
        return ++id;
    }

    @Override
    public Optional<User> find(String email) {
        List<User> userList = new ArrayList<User>(users.values());
        return userList.stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void delete(long id) {
        if (findId(id) != null) {
            users.remove(id);
        }
    }

    private boolean checkEmail(User user) {
        if (users.containsValue(user)) {
            return false;
        }
        return true;
    }


}
