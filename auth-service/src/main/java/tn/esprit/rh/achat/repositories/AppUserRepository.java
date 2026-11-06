package tn.esprit.rh.achat.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.rh.achat.entities.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUsername(String username);

	boolean existsByUsername(String username);
}
