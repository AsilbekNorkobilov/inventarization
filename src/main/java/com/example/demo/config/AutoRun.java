package com.example.demo.config;

import com.example.demo.entity.Category;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.RoleName;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AutoRun implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepo categoryRepo;
    private final RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        //generate();
    }
    private void generate(){
        Role role1=new Role(RoleName.ROLE_ADMIN);
        Role role2=new Role(RoleName.ROLE_MANAGER);
        roleRepo.save(role1);
        roleRepo.save(role2);

        User user1=new User("998911111111",passwordEncoder.encode("1"), List.of(role1));
        User user2=new User("998922222222",passwordEncoder.encode("1"), List.of(role2));
        userRepo.save(user1);
        userRepo.save(user2);

        Category category1=new Category("Yeguliklar");
        Category category2=new Category("Ichguliklar");
        Category category3=new Category("Kiyguliklar");
        categoryRepo.save(category1);
        categoryRepo.save(category2);
        categoryRepo.save(category3);
    }
}
