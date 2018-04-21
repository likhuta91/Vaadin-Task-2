package com.gp.vaadin.demo.hotel;

import javax.servlet.annotation.WebServlet;

import com.gp.vaadin.demo.hotel.view.CategoryView;
import com.gp.vaadin.demo.hotel.view.HotelView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("mytheme")
public class HotelUI extends UI {	

	
	final HotelService hotelService = HotelService.getInstance();
	private Navigator navigator;
	
	final Grid<Hotel> hotelGrid = new Grid<>();
	
	final TextField nameFilter = new TextField();
	final TextField addressFilter = new TextField();
	
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	final Button edit = new Button();
	
	final VerticalLayout layout = new VerticalLayout();
	
    public static final String HOTEL = "Hotel";
    public static final String CATEGORY = "Category";

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		navigator = new Navigator(this, this);
		
		HotelView hotelView = new HotelView(this);
		CategoryView categoryView = new CategoryView(this);
		
		navigator.addView("", hotelView);
		navigator.addView(CATEGORY, categoryView);
		navigator.addView(HOTEL, hotelView);  

	}

	@WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
	public static class HotelUIServlet extends VaadinServlet {

	}
	 
}


