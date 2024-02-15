package org.example.view.user;

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
import jakarta.annotation.security.RolesAllowed;
import org.example.constants.Operation;
import org.example.entity.User;
import org.example.service.UserRoleService;
import org.example.service.UserService;
import org.example.view.MainLayout;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN"})
public class Users extends VerticalLayout {
    private Grid<User> grid;
    private UserDialog dialog;
    private final UserService userService;
    private final UserRoleService userRoleService;

    public Users(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;

        Button createUserBtn = new Button("Create User", buttonClickEvent -> {
            dialog = new UserDialog(
                    userService, userRoleService,
                    () -> grid.getDataProvider().refreshAll(),
                    () -> grid.getDataProvider().refreshAll(),
                    Operation.CREATE, null
            );
            dialog.getDialog().open();
        });

        HorizontalLayout topGridLayout = new HorizontalLayout(getSearchField(), createUserBtn);
        topGridLayout.getStyle().setDisplay(Style.Display.FLEX).setJustifyContent(Style.JustifyContent.SPACE_BETWEEN).setWidth("100%");

        add(topGridLayout, createGrid());
    }

    private Grid<User> createGrid() {
        grid = new Grid<>(User.class, false);
        grid.setHeight("75vh");

        grid.addColumn(User::getName).setHeader("Full Name").setSortable(true);
        grid.addColumn(User::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(User::getUsername).setHeader("Username").setSortable(true);
        grid.addColumn(user -> user.getRole().getName()).setHeader("Role").setSortable(true);

        grid.setItems(query -> userService.fetchPage(query.getPage(), query.getLimit()).stream());

        grid.addItemDoubleClickListener(
                itemDoubleClickEvent ->{
                    User user = itemDoubleClickEvent.getItem();
                    user.setPassword("");
                    dialog = new UserDialog(
                            userService, userRoleService,
                            () -> grid.getDataProvider().refreshAll(),
                            () -> grid.getDataProvider().refreshAll(),
                            Operation.MODIFY, user
                    );
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

        return searchField;

    }
}
