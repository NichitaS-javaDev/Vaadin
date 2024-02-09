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
import org.example.entity.Issue;
import org.example.service.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@PageTitle("Issues")
@Route(value = "issues", layout = MainLayout.class)
@PermitAll
public class Issues extends VerticalLayout {
    private Grid<Issue> grid;
    private IssueDialog dialog;
    private final IssueService issueService;
    private final IssueStatusService issueStatusService;
    private final PosService posService;
    private final UserService userService;
    private final IssueTypeService issueTypeService;

    public Issues(IssueService issueService, IssueStatusService issueStatusService, PosService posService, UserService userService, IssueTypeService issueTypeService) {
        this.issueService = issueService;
        this.issueStatusService = issueStatusService;
        this.posService = posService;
        this.userService = userService;
        this.issueTypeService = issueTypeService;

        Button createPosBtn = new Button("Create Issue", buttonClickEvent -> {
            dialog = new IssueDialog(
                    issueService, userService, posService, issueStatusService, issueTypeService,
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

    private Grid<Issue> createGrid() {
        grid = new Grid<>(Issue.class, false);
        grid.setHeight("75vh");

        grid.addColumn(Issue::getTitle).setHeader("Title").setSortable(true);
        grid.addColumn(issue -> issue.getPos().getName()).setHeader("Pos name").setSortable(true);
        grid.addColumn(issue -> issue.getMainType().getName()).setHeader("Type").setSortable(true);
        grid.addColumn(issue -> {
            LocalDate localDate = issue.getCreationDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }).setHeader("Creation date").setSortable(true);
        grid.addColumn(Issue::getPriority).setHeader("Priority").setSortable(true);
        grid.addColumn(Issue::getDescription).setHeader("Description").setSortable(true);
        grid.addColumn(issue -> issue.getAssignedTo().getName()).setHeader("Assigned to").setSortable(true);
        grid.addColumn(issue -> issue.getStatus().getName()).setHeader("Status").setSortable(true);
        grid.addColumn(Issue::getSolution).setHeader("Solution").setSortable(true);

        grid.setItems(query -> issueService.fetchPage(query.getPage(), query.getLimit()).stream());

        grid.addItemDoubleClickListener(
                posItemDoubleClickEvent ->{
                    dialog = new IssueDialog(
                            issueService, userService, posService, issueStatusService, issueTypeService,
                            () -> grid.getDataProvider().refreshAll(),
                            () -> grid.getDataProvider().refreshAll(),
                            Operation.MODIFY
                    );
                    dialog.setIssueEntity(posItemDoubleClickEvent.getItem());
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
