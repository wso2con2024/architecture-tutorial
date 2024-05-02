package org.choreo.demo.luxury.hotels.repo;

import org.choreo.demo.luxury.hotels.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
