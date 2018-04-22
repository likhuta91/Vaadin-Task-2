package com.gp.vaadin.demo.hotel.view;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gp.vaadin.demo.hotel.Category;
import com.gp.vaadin.demo.hotel.CategoryEditForm;
import com.gp.vaadin.demo.hotel.CategoryService;
import com.gp.vaadin.demo.hotel.HotelUI;
import com.gp.vaadin.demo.hotel.MenuNavigator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

public class CategoryView extends VerticalLayout implements View {

	private static final long serialVersionUID = 4968100371210880557L;
	
	final HotelUI ui;
	final MenuNavigator menuNavigator;
	
	final CategoryService categoryService = CategoryService.getInstance();
	
	final Grid<Category> categoryGrid = new Grid<>();
	final CategoryEditForm editForm = new CategoryEditForm(this);
	
	final Button addCategory = new Button("Add category");
	public final Button deleteCategory = new Button("Delete category");
	final Button editCategory = new Button("Edit category");
	
	final VerticalLayout layout = new VerticalLayout();

	public CategoryView(HotelUI ui) {
		
		this.ui = ui;
		this.menuNavigator = new MenuNavigator(ui);
		
		deleteCategory.setEnabled(false);
		editCategory.setEnabled(false);
		editForm.setVisible(false);
		
		addCategory.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		addCategory.setIcon(VaadinIcons.PLUS);
		deleteCategory.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		deleteCategory.setIcon(VaadinIcons.TRASH);
		editCategory.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		editCategory.setIcon(VaadinIcons.EDIT);
		
		//grid		
		categoryGrid.addColumn(Category::getName).setCaption("Name");
		
		categoryGrid.setSelectionMode(SelectionMode.MULTI);
		
		categoryGrid.setWidth(500,Unit.PIXELS);
		categoryGrid.setHeightMode(HeightMode.UNDEFINED);
		
		categoryGrid.asMultiSelect().addValueChangeListener(e -> {
			if (e.getValue().size() == 1) {
			
				deleteCategory.setEnabled(true);
				editCategory.setEnabled(true);

				editCategory.addClickListener(c -> {
					Iterator<Category> iterator = e.getValue().iterator();
					if (iterator.hasNext()) {
						editForm.setCategory(iterator.next());
					}
				});

			} else if (e.getValue().size() > 1) {
				deleteCategory.setEnabled(true);
				editCategory.setEnabled(false);
			} else {
			
				deleteCategory.setEnabled(false);
				editCategory.setEnabled(false);
			}
		});
		
		//controls
		HorizontalLayout controls = new HorizontalLayout();
		controls.addComponents(addCategory, deleteCategory, editCategory);
		
		//content
		HorizontalLayout content = new HorizontalLayout();
		content.addComponents(categoryGrid, editForm);
		content.setComponentAlignment(editForm, Alignment.TOP_CENTER);
		
		layout.addComponents(menuNavigator.getMenuBar(),controls, content);
		
		//dimensions
		editForm.setWidth(400,Unit.PIXELS);
		
		//listeners
		deleteCategory.addClickListener(e -> {
			Set<Category> delCandidate= categoryGrid.getSelectedItems();			
			categoryService.delete(delCandidate);
			deleteCategory.setEnabled(false);
			updateList();
			editForm.setVisible(false);
		});

		addCategory.addClickListener(e -> editForm.setCategory(new Category()));
		updateList();
		
	}

	public void updateList() {
		List<Category> hotelList = categoryService.findAll();
		categoryGrid.setItems(hotelList);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		updateList();
		addComponent(layout);
		Notification.show("Welcome in category menu!");
	}

}