package com.gp.vaadin.demo.hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
	private static CategoryService instance;

	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	private final HashMap<Long, Category> categories = new HashMap<>();
	private long nextId = 0;

	private CategoryService() {
	}

	public static CategoryService getInstance() {
		if (instance == null) {
			instance = new CategoryService();
			instance.ensureTestData();
		}
		return instance;
	}

	public synchronized List<Category> findAll() {
		ArrayList<Category> arrayList = new ArrayList<>();
		for (Category category : categories.values()) {
			arrayList.add(category);
		}
		Collections.sort(arrayList, new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long count() {
		return categories.size();
	}

	public synchronized void delete(Set<Category> value) {

		for(Category category:value) {
			category.setName("No category");
			categories.remove(category.getId());
		}
	}

	public synchronized void save(Category entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		categories.put(entry.getId(), entry);
	}

	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] categoryData = new String[] { "Hotel", "Hostel", "GuestHouse", "Appartments"};

			for (String category : categoryData) {

				Category c = new Category(category);
				save(c);
			}
		}
	}

}
