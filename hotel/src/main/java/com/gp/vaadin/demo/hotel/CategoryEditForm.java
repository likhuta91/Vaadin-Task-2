package com.gp.vaadin.demo.hotel;

import com.gp.vaadin.demo.hotel.view.CategoryView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class CategoryEditForm extends FormLayout{

	private static final long serialVersionUID = -3271156570356300813L;
	private CategoryView categoryView;
	private CategoryService categoryService = CategoryService.getInstance();
	private Category category;
	private Binder<Category> binder = new Binder<>(Category.class);
	private TextField name = new TextField("Name");
	
	private Button save = new Button("Save");
	private Button close = new Button("Close");
	 
	public CategoryEditForm(CategoryView categoryView) {
		
		this.categoryView = categoryView;
		HorizontalLayout buttons = new HorizontalLayout();
		
		buttons.addComponents(save, close);
		addComponents(name,buttons);
		
		save.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		save.setIcon(VaadinIcons.CHECK);
		close.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		close.setIcon(VaadinIcons.CLOSE);
		
		name.setWidth(100, Sizeable.Unit.PERCENTAGE);
		save.setWidth(100, Sizeable.Unit.PERCENTAGE);
		close.setWidth(100, Sizeable.Unit.PERCENTAGE);
		buttons.setWidth(100, Sizeable.Unit.PERCENTAGE);		
		
		name.setDescription("Category name");
		binder.forField(name).asRequired("Please enter a name").bind(Category::getName, Category::setName);
		
		save.addClickListener(e -> save());
		close.addClickListener(e -> setVisible(false));
	}
	
	private void save() {
		if(binder.isValid()) {
			try {
				binder.writeBean(category);
			} catch (ValidationException e) {
				Notification.show("Unable to save!" + e.getMessage(), Type.HUMANIZED_MESSAGE);
			}
			categoryService.save(category);
			exit();
		} else {
			Notification.show("Unable to save! Please review errors and fix them.", Type.ERROR_MESSAGE);
		}		
		
	}
	
	private void exit() {
		categoryView.updateList();
		setVisible(false);
		categoryView.deleteCategory.setEnabled(false);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
		binder.readBean(this.category);
		setVisible(true);

	}
	
}
