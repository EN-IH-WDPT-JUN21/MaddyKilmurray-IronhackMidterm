package com.ironhack.midterm.repository.users;

import com.ironhack.midterm.dao.users.usersubclasses.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
