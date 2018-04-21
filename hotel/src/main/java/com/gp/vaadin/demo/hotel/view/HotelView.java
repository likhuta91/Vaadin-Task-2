package com.gp.vaadin.demo.hotel.view;

import java.util.List;
import java.util.Set;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;

import com.gp.vaadin.demo.hotel.Category;
import com.gp.vaadin.demo.hotel.CategoryService;
import com.gp.vaadin.demo.hotel.Hotel;
import com.gp.vaadin.demo.hotel.HotelEditForm;
import com.gp.vaadin.demo.hotel.HotelService;
import com.gp.vaadin.demo.hotel.HotelUI;
import com.gp.vaadin.demo.hotel.MenuNavigator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class HotelView extends VerticalLayout implements View {

	private static final long serialVersionUID = -530637194012821083L;
	
	final HotelUI ui;
	final MenuNavigator menuNavigator;
	
	final HotelService hotelService = HotelService.getInstance();
	final CategoryService categoryService = CategoryService.getInstance();
	
	final Grid<Hotel> hotelGrid = new Grid<>();
	final HotelEditForm editForm = new HotelEditForm(this);
	
	final TextField nameFilter = new TextField();
	final TextField addressFilter = new TextField();
	
	final Button addHotel = new Button("Add hotel");
	public final Button deleteHotel = new Button("Delete hotel");
	final Button editHotel = new Button("Edit hotel");
	
	final VerticalLayout layout = new VerticalLayout();

	public HotelView(HotelUI ui) {
		
		this.ui = ui;
		this.menuNavigator = new MenuNavigator(ui);
		
		deleteHotel.setEnabled(false);
		editHotel.setEnabled(false);
		
		addHotel.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		addHotel.setIcon(VaadinIcons.PLUS);
		deleteHotel.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		deleteHotel.setIcon(VaadinIcons.TRASH);
		editHotel.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		editHotel.setIcon(VaadinIcons.EDIT);
		
		//grid
		hotelGrid.addColumn(Hotel::getName).setCaption("Name");
		hotelGrid.addColumn(Hotel::getAddress).setCaption("Address");
		hotelGrid.addColumn(Hotel::getRating).setCaption("Rating");
		hotelGrid.addColumn(hotel -> Instant.ofEpochMilli(new Date(hotel.getOperatesFrom()).getTime())
				.atZone(ZoneId.systemDefault()).toLocalDate()).setCaption("Operates From");
		hotelGrid.addColumn(Hotel::getCategory).setCaption("Category");		
		hotelGrid.addColumn(Hotel::getDescription).setCaption("Description");
		Grid.Column<Hotel, String> htmlColumn = hotelGrid.addColumn(
				hotel -> "<a href='" + hotel.getUrl() + "' target='_blank'>hotel info</a>", new HtmlRenderer());
		htmlColumn.setCaption("Url");
		
		hotelGrid.setWidth(100, Unit.PERCENTAGE);
		hotelGrid.setHeight("100%");
		
		hotelGrid.setSelectionMode(SelectionMode.MULTI);
		
		//controls
		HorizontalLayout controls = new HorizontalLayout();
		controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel, editHotel);

		//content
		HorizontalLayout content = new HorizontalLayout();
		content.addComponents(hotelGrid, editForm);
		content.setWidth("100%");
		content.setHeight(500, Unit.PIXELS);
		content.setExpandRatio(hotelGrid, 0.7f);
		content.setExpandRatio(editForm, 0.3f);
		content.setComponentAlignment(editForm, Alignment.MIDDLE_CENTER);
		
		layout.addComponents(menuNavigator.getMenuBar(), controls, content);
		
		//dimensions		
		editForm.setWidth("80%");
		editForm.setHeight("100%");
		editForm.setVisible(false);

		//listeners
		deleteHotel.addClickListener(e -> {
			Set<Hotel> delCandidate= hotelGrid.getSelectedItems();			
			hotelService.delete(delCandidate);
			deleteHotel.setEnabled(false);
			editForm.setVisible(false);
			updateList();
			
		});

		addHotel.addClickListener(e -> editForm.setHotel(new Hotel()));
		
		hotelGrid.asMultiSelect().addValueChangeListener(e -> {
			if (e.getValue().size() == 1) {
				deleteHotel.setEnabled(true);
				editHotel.setEnabled(true);

				editHotel.addClickListener(c -> {
					
					Iterator<Hotel> iterator = e.getValue().iterator();
						editForm.setHotel(iterator.next());
				});

			} else if (e.getValue().size() > 1) {
				deleteHotel.setEnabled(true);
				editHotel.setEnabled(false);
			} else {
				deleteHotel.setEnabled(false);
				editHotel.setEnabled(false);
			}
		});

		//filters
		nameFilter.setPlaceholder("Enter name");
		nameFilter.addValueChangeListener(e -> updateList());
		nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
		updateList();

		addressFilter.setPlaceholder("Enter address");
		addressFilter.addValueChangeListener(e -> updateList());
		addressFilter.setValueChangeMode(ValueChangeMode.LAZY);
		updateList();		
		
	}

	public void updateList() {
		List<Hotel> hotelList = hotelService.findAll(nameFilter.getValue(), addressFilter.getValue());
		hotelGrid.setItems(hotelList);

		NativeSelect<Category> category = editForm.getCategory();
		category.setItems(categoryService.findAll());
		editForm.setCategory(category);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		updateList();
		addComponent(layout);
		Notification.show("Welcome in hotel menu!");
	}

}
