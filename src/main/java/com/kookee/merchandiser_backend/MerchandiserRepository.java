package com.kookee.merchandiser_backend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchandiserRepository extends JpaRepository<Merchandiser, String> {
    Merchandiser findByUsernameAndPassword(String username, String password);
}
