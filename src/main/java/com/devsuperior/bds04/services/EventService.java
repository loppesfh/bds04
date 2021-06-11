package com.devsuperior.bds04.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;
	
	@Transactional	
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = repository.getOne(id);
						
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(dto.getUrl());
			
			entity.setCity(new City(dto.getCityId(), null));
	
			entity = repository.save(entity);		
			return new EventDTO(entity);		
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found \" + id");
		} 	
	}

	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> result = repository.findAll(pageable);
		return result.map(x -> new EventDTO(x));
	}

	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setName(dto.getName());
		entity.setUrl(dto.getUrl());
		entity.setDate(dto.getDate());
		entity.setCity(new City(dto.getCityId(), null));
		
		entity = repository.save(entity);
		return new EventDTO(entity);
	}
}
