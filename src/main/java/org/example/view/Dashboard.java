package org.example.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.example.entity.Issue;
import org.example.service.IssueService;

import java.util.List;

@PageTitle("Dashboard")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class Dashboard extends VerticalLayout {
    private final IssueService issueService;
    private Component currentGrid = new Grid<>();

    public Dashboard(IssueService issueService){
        this.issueService = issueService;

        Span cardsSpan = new Span();
        cardsSpan.setWidth("100%");
        DashboardCard newIssuesCard = new DashboardCard("#fe4d4d", VaadinIcon.PLUS_CIRCLE_O, "New Issues",
                buttonClickEvent -> replaceCurrentGrid("Opened"));
        newIssuesCard.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        newIssuesCard.setWidth("20%");
        newIssuesCard.getStyle().setMarginLeft("1%");

        DashboardCard pendingIssuesCard = new DashboardCard("#fce634", VaadinIcon.REFRESH, "Pending Issue",
                buttonClickEvent -> replaceCurrentGrid("Pending"));
        pendingIssuesCard.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        pendingIssuesCard.setWidth("22%");
        pendingIssuesCard.getStyle().setMarginLeft("2.5%");

        DashboardCard assignedIssuesCard = new DashboardCard("#0a83fd", VaadinIcon.ARROW_FORWARD, "Assigned Issue",
                buttonClickEvent -> replaceCurrentGrid("Assigned"));
        assignedIssuesCard.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        assignedIssuesCard.setWidth("23%");
        assignedIssuesCard.getStyle().setMarginLeft("2.5%");

        DashboardCard inProgressIssuesCard = new DashboardCard("#218b21", VaadinIcon.AUTOMATION, "In Progress Issue",
                buttonClickEvent -> replaceCurrentGrid("In Progress"));
        inProgressIssuesCard.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        inProgressIssuesCard.setWidth("25%");
        inProgressIssuesCard.getStyle().setMarginLeft("2.5%");

        cardsSpan.add(newIssuesCard, pendingIssuesCard, assignedIssuesCard, inProgressIssuesCard);
        add(cardsSpan);

    }

    private void replaceCurrentGrid(String status){
        remove(currentGrid);

        Grid<Issue> grid = createGrid(status);
        grid.getStyle().setMargin("1%");
        add(grid);

        currentGrid = grid;
    }

    private Grid<Issue> createGrid(String status){
        Grid<Issue> grid = new Grid<>(Issue.class, false);

        grid.addColumn(Issue::getId).setHeader("Issue #").setSortable(true);
        grid.addColumn(issue -> issue.getPos().getId()).setHeader("POS ID").setSortable(true);
        grid.addColumn(issue -> issue.getPos().getName()).setHeader("POS Name").setSortable(true);
        grid.addColumn(issue -> issue.getCreatedBy().getName()).setHeader("Created By").setSortable(true);
        grid.addColumn(Issue::getCreationDate).setHeader("Date").setSortable(true);
        grid.addColumn(issue -> issue.getMainType().getName()).setHeader("Issue Type").setSortable(true);
        grid.addColumn(issue -> issue.getStatus().getName()).setHeader("Issue Status").setSortable(true);
        grid.addColumn(issue -> issue.getAssignedTo().getName()).setHeader("Assigned To").setSortable(true);
        grid.addColumn(Issue::getDescription).setHeader("Description").setSortable(true);

        List<Issue> issues = issueService.findAllByUsernameAndStatus(status);
        grid.setItems(issues);

        return grid;
    }
}
