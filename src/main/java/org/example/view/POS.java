package org.example.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.example.service.CityService;
import org.example.service.ConnectionTypeService;
import org.example.service.PosService;

@PageTitle("POS")
@Route(value = "pos", layout = MainLayout.class)
@PermitAll
public class POS extends VerticalLayout {
    private final PosService posService;
    private final ConnectionTypeService connectionTypeService;
    private final CityService cityService;
    private Grid<org.example.entity.POS> grid;
    private PosDialog dialog;

    public POS(PosService posService, ConnectionTypeService connectionTypeService, CityService cityService) {
        this.posService = posService;
        this.connectionTypeService = connectionTypeService;
        this.cityService = cityService;

        Button createPosBtn = new Button("Create POS", buttonClickEvent -> {
            dialog = new PosDialog(
                    connectionTypeService, cityService, posService,
                    () -> grid.getDataProvider().refreshAll(),
                    () -> grid.getDataProvider().refreshAll(),
                    Operation.CREATE
            );
            dialog.getDialog().open();
        });

        HorizontalLayout topGridLayout = new HorizontalLayout(getSearchField(), createPosBtn);
        topGridLayout.getStyle().setDisplay(Style.Display.FLEX).setJustifyContent(Style.JustifyContent.SPACE_BETWEEN).setWidth("100%");

        add(topGridLayout, createGrid());
    }

    private Grid<org.example.entity.POS> createGrid() {
        grid = new Grid<>(org.example.entity.POS.class, false);
        grid.setHeight("75vh");

        grid.addColumn(org.example.entity.POS::getName).setHeader("Name").setSortable(true);
        grid.addColumn(org.example.entity.POS::getBrand).setHeader("Brand").setSortable(true);
        grid.addColumn(org.example.entity.POS::getModel).setHeader("Model").setSortable(true);
        grid.addColumn(pos -> pos.getCity().getName()).setHeader("City").setSortable(true);
        grid.addColumn(org.example.entity.POS::getAddress).setHeader("Address").setSortable(true);
        grid.addColumn(org.example.entity.POS::getTelephone).setHeader("Telephone").setSortable(true);
        grid.addColumn(org.example.entity.POS::getOpening).setHeader("Opening hours").setSortable(true);
        grid.addColumn(org.example.entity.POS::getClosing).setHeader("Closing hours").setSortable(true);
        grid.addColumn(pos -> pos.getConnType().getName()).setHeader("Connection Type").setSortable(true);

        grid.setItems(query -> posService.fetchPage(query.getPage(), query.getLimit()).stream());

        grid.addItemDoubleClickListener(
                posItemDoubleClickEvent ->{
                    dialog = new PosDialog(
                            connectionTypeService, cityService, posService,
                            () -> grid.getDataProvider().refreshAll(),
                            () -> grid.getDataProvider().refreshAll(),
                            Operation.MODIFY
                    );
                    dialog.setPosEntity(posItemDoubleClickEvent.getItem());
                    dialog.getDialog().open();
                }
        );

        return grid;
    }

    private Component getSearchField(){
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
//        searchField.addValueChangeListener(e -> dataView.refreshAll());

        return searchField;

//        dataView.addFilter(person -> {
//            String searchTerm = searchField.getValue().trim();
//
//            if (searchTerm.isEmpty())
//                return true;
//
//            boolean matchesFullName = matchesTerm(person.getFullName(),
//                    searchTerm);
//            boolean matchesEmail = matchesTerm(person.getEmail(), searchTerm);
//            boolean matchesProfession = matchesTerm(person.getProfession(),
//                    searchTerm);
//
//            return matchesFullName || matchesEmail || matchesProfession;
//        });
    }
}
