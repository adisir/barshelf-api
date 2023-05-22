package com.barshelf.user.repository;

import com.barshelf.user.entities.BarshelfUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarshelfUserRepository  extends CrudRepository<BarshelfUser, Long> {
    List<BarshelfUser> findByEmail(String username);
}
