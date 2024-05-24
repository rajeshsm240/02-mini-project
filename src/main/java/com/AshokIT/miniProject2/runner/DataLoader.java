package com.AshokIT.miniProject2.runner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.AshokIT.miniProject2.Entities.CitizenPlan;
import com.AshokIT.miniProject2.Repository.CitizenPlanRepository;

@Component	
public class DataLoader implements ApplicationRunner{

	@Autowired
	private CitizenPlanRepository citizenPlanRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		citizenPlanRepository.deleteAll();
		
		CitizenPlan citizen1 = new CitizenPlan("ABC","ABC@gmail.com",208782,97301874,"Male","Cash","Approved",LocalDate.now(),LocalDate.now().plusMonths(6));
		CitizenPlan citizen2 = new CitizenPlan("DEF","DEF@gmail.com",208778,97301809,"Female","Food","Denied",null,null);
		CitizenPlan citizen3 = new CitizenPlan("GHI","GHI@gmail.com",203432,92301874,"Male","Medical","Approved",LocalDate.now(),LocalDate.now().plusMonths(3));
		
		List<CitizenPlan> records = Arrays.asList(citizen1,citizen2,citizen3);
		
		citizenPlanRepository.saveAll(records);
		
	}

}
