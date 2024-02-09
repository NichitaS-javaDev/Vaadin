package org.example.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Style;

public class DashboardCard extends Div {
    private final String color;
    private final VaadinIcon icon;
    private final String description;
    private final ComponentEventListener<ClickEvent<Button>> listener;

    public DashboardCard(String color, VaadinIcon icon, String description, ComponentEventListener<ClickEvent<Button>> listener) {
        this.color = color;
        this.icon = icon;
        this.description = description;
        this.listener = listener;

        createCard();
    }

    private void createCard(){
        Div box = new Div();
        box.getStyle()
                .setColor("white")
                .setBorder("solid 1px " + color)
                .setBorderRadius("5px");

        Div topBoxLevel = new Div();
        topBoxLevel.getStyle()
                .setPadding("3.5%")
                .setBackgroundColor(color)
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.CENTER);

        Div bottomBoxLevel = new Div();
        bottomBoxLevel.getStyle()
                .setDisplay(Style.Display.FLEX)
                .setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);

        Icon plusIcon = icon.create();
        plusIcon.setSize("5.5rem");
        Span topLeftIcon = new Span(plusIcon);
        topLeftIcon.getStyle()
                .setDisplay(Style.Display.INLINE_BLOCK);

        Span topRightText = new Span();
        topRightText.getStyle()
                .setColor("white")
                .setDisplay(Style.Display.INLINE_BLOCK)
                .setMarginLeft("41%");

        H1 topRightTextMainText = new H1("0");
        topRightTextMainText.getStyle()
                .setColor("white");

        Div topRightTextMain = new Div();
        topRightTextMain.add(topRightTextMainText);
        topRightTextMain.getStyle()
                .setTextAlign(Style.TextAlign.RIGHT);

        H5 topRightTextAdditionalText = new H5(description);
        topRightTextAdditionalText.getStyle()
                .setColor("white");

        Div topRightTextAdditional = new Div();
        topRightTextAdditional.add(topRightTextAdditionalText);
        topRightText.add(topRightTextMain, topRightTextAdditional);
        topBoxLevel.add(topLeftIcon, topRightText);

        Span bottomLeftText = new Span("View Details");
        bottomLeftText.getStyle()
                .setColor(color)
                .setAlignSelf(Style.AlignSelf.CENTER)
                .setPaddingLeft("3.5%");

        Icon addIcon = VaadinIcon.ARROW_CIRCLE_RIGHT_O.create();
        addIcon.getStyle()
                .setColor(color);
        addIcon.setSize("2rem");

        Button detailsButton = new Button("", addIcon);
        detailsButton.addClickListener(listener);
        detailsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Span bottomRightButton = new Span(detailsButton);
        bottomBoxLevel.add(bottomLeftText, bottomRightButton);
        box.add(topBoxLevel, bottomBoxLevel);
        add(box);
    }
}
