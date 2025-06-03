package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CreateUserDatabaseRepositoryAdapter implements CreateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    private final DietaryNeedRepositoryAdapter dietaryNeedRepositoryAdapter;


    public CreateUserDatabaseRepositoryAdapter(CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter,
                                               DietaryNeedRepositoryAdapter dietaryNeedRepositoryAdapter) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
        this.dietaryNeedRepositoryAdapter = dietaryNeedRepositoryAdapter;

    }

    @Override
    public User execute(User user) {

        UserHibernateModel userResponse = createUserHibernateRepositoryAdapter.save(buildHibernateUser(user));

        return buildUser(userResponse);
    }

    private UserHibernateModel buildHibernateUser(User user) {
        List<DietaryNeedHibernateModel> dietaryNeedEntities = user.getDietaryNeeds()
                .stream()
                .map(needName -> dietaryNeedRepositoryAdapter.findByName(needName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new UserHibernateModel(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRegisterDate(),
                user.getPlan(),
                user.getIsValid(),
                user.getCookLevel(),
                user.getDiet(),
                dietaryNeedEntities
        );
    }


    private User buildUser(UserHibernateModel userResponse) {
        List<String> dietaryNeedNames = userResponse.getDietaryNeeds()
                .stream()
                .map(DietaryNeedHibernateModel::getName)
                .collect(Collectors.toList());

        return new User(
                userResponse.getId(),
                userResponse.getName(),
                userResponse.getEmail(),
                userResponse.getPassword(),
                userResponse.getRegisterDate(),
                userResponse.getPlan(),
                userResponse.getIsValid(),
                userResponse.getCookLevel(),
                userResponse.getDiet(),
                dietaryNeedNames);
    }
}