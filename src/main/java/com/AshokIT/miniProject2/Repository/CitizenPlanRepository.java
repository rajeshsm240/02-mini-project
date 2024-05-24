package com.AshokIT.miniProject2.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.AshokIT.miniProject2.Entities.CitizenPlan;
import com.AshokIT.miniProject2.binding.SearchCriteria;

@Repository
public interface CitizenPlanRepository extends JpaRepository<CitizenPlan, Long> {

	@Query("select distinct(planName) from CitizenPlan") 
	public List<String>
	getUniquePlanNames();
	
	@Query("select distinct(planStatus) from CitizenPlan") 
	public List<String> getUniquePlanStatus();
	
	
}
