package com.barshelf.repository;

import com.barshelf.entities.BarshelfUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarshelfUserRepository extends CrudRepository<BarshelfUser, Long> {
    Optional<BarshelfUser> findByEmail(String email);
}
