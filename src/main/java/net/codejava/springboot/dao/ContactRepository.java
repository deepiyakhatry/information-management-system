package net.codejava.springboot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.codejava.springboot.entitites.ContactDetails;

public interface ContactRepository extends JpaRepository<ContactDetails, Integer> 
{
	
	@Query("from ContactDetails as c where c.user.id=:userId")
	public List<ContactDetails> findContactsByUser(@Param("userId")int userId);
	
}

