package com.beastshop.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTest {
	@Autowired
	private StateRepository repo;
	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId=3;
		Country country = entityManager.find(Country.class, countryId);
		
//		State state = repo.save(new State("West Bengal", country));
		State state = repo.save(new State("Maharastra", country));
		//State state = repo.save(new State("Karnataka", country));
		//State state = repo.save(new State("UP", country));
		//State state = repo.save(new State("Bihar", country));
		//State state = repo.save(new State("Gujarat", country));
	
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void listStatesByCountry() {
		Integer countryId = 3;
		Country country = entityManager.find(Country.class, countryId);
		
		List<State> listStates = repo.findByCountryOrderByNameAsc(country);
		
		listStates.forEach(System.out::println);
		
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	
	@Test
	public void testUpdateState() {
		Integer stateId=3;
		String newName = "Ultimatre Gujarat";
		
		State state = repo.findById(stateId).get();
		state.setName(newName);
		
		State savedState = repo.save(state);
		assertThat(savedState.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testGetState() {
		Integer stateId=3;
		 Optional<State> stateById = repo.findById(stateId);
	assertThat(stateById.isPresent());
	}
	
	@Test
	public void deleteState() {
		Integer id=2;
		repo.deleteById(id);
		 Optional<State> stateById = repo.findById(id);
		 assertThat(stateById.isEmpty());
		
	}
	
}
