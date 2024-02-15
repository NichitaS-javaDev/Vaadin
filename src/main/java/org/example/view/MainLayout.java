package org.example.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import org.example.service.SecurityService;
import org.example.view.dashboard.Dashboard;
import org.example.view.issue.Issues;
import org.example.view.pos.POS;
import org.example.view.user.Users;

import java.util.Optional;

public class MainLayout extends AppLayout {
    private final Tabs menu;
    private H3 viewTitle;
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        setPrimarySection(Section.NAVBAR);

        addToNavbar(true, createHeaderContent());

        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        Optional<String> optionalUserName = securityService.getAuthenticatedUserName();
        String name = optionalUserName.orElse("Undefined");

        layout.setId("header");
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());

        viewTitle = new H3();
        viewTitle.getStyle().set("width", "10%");
        viewTitle.getStyle().setColor("#4c4c4c");
        layout.add(viewTitle);

        Avatar avatar = new Avatar();
        avatar.getStyle().set("margin-left", "70%");
        avatar.getStyle().set("margin-right", "0.5%");
        H6 currentUsername = new H6(name);
        layout.add(avatar);
        layout.add(currentUsername);


        Button logoutButton = new Button("Logout", VaadinIcon.CHEVRON_CIRCLE_RIGHT_O.create());
        logoutButton.getStyle().set("font-weight", "bold");
        logoutButton.getStyle().setColor("#36404e");
        logoutButton.getStyle().set("margin-left", "4%");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutButton.addClickListener(e -> securityService.logout());
        layout.add(logoutButton);

        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(logoLayout, menu);

        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        if (securityService.isCurrentUserAdmin()){
            tabs.add(createAdminItems());
        }

        return tabs;
    }

    private Tab[] createMenuItems() {
        return new Tab[] { createTab("Dashboard", VaadinIcon.DASHBOARD.create(), Dashboard.class),
                createTab("POS", VaadinIcon.CREDIT_CARD.create(), POS.class),
                createTab("Issues", VaadinIcon.WRENCH.create(), Issues.class)};
    }

    private Tab[] createAdminItems(){
        return new Tab[] {createTab("Users", VaadinIcon.USER.create(), Users.class)};
    }

    private static Tab createTab(String text, Icon icon, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        icon.getStyle().set("margin-right", "5%");
        tab.add(icon);
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
