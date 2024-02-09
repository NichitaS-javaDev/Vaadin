package org.example.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.BeanValidator;
import org.example.entity.City;
import org.example.entity.ConnectionType;
import org.example.entity.POS;
import org.example.service.CityService;
import org.example.service.ConnectionTypeService;
import org.example.service.PosService;

import java.util.Objects;

public class PosDialog extends Div {
    @FunctionalInterface
    interface SaveCallback { void onSave(); }
    @FunctionalInterface
    interface DeleteCallback { void onDelete(); }
    private final ConnectionTypeService connectionTypeService;
    private final CityService cityService;
    private final PosService posService;
    private final Dialog dialog;
    private final Binder<POS> binder;
    private final VerticalLayout dialogLayout;
    private POS pos = new POS();

    public PosDialog(
            ConnectionTypeService connectionTypeService, CityService cityService, PosService posService, SaveCallback saveCallback,
            DeleteCallback deleteCallback, Operation operation
    ) {
        this.connectionTypeService = connectionTypeService;
        this.cityService = cityService;
        this.posService = posService;

        dialog = new Dialog();
        binder = new Binder<>(POS.class);

        dialogLayout = createDialogLayout();
        Button operationButton = operation.equals(Operation.CREATE) ?
                getOperationButton("Add", saveCallback, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS) :
                getOperationButton("Update", saveCallback, ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.add(dialogLayout);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(operationButton);

        if (operation.equals(Operation.CREATE)) {
            dialog.setHeaderTitle("New POS");
        } else {
            dialog.setHeaderTitle("Update POS");
            dialog.getFooter().add(getDeleteButton(deleteCallback));
        }

        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }

    private VerticalLayout createDialogLayout() {
        binder.setBean(pos);

        TextField name = new TextField("Name");
        TextField brand = new TextField("Brand");
        TextField model = new TextField("Model");
        ComboBox<City> city = new ComboBox<>("City");
        city.setItems(cityService.findAll());
        city.setItemLabelGenerator(City::getName);
        TextField address = new TextField("Address");
        Div phoneNumber = new Div();
        TextField countryCode = new TextField("Phone");
        countryCode.setPlaceholder("+373");
        countryCode.getStyle().setMarginRight("1%").setWidth("14%");
        TextField telephone = new TextField();
        telephone.setWidth("85%");
        phoneNumber.add(countryCode, telephone);
        TimePicker opening = new TimePicker("Opening");
        TimePicker closing = new TimePicker("Closing");
        ComboBox<ConnectionType> connectionType = new ComboBox<>("ConnectionType");
        connectionType.setItems(connectionTypeService.findAll());
        connectionType.setItemLabelGenerator(ConnectionType::getName);

        binder.forField(name).withValidator(new BeanValidator(POS.class, "name")).bind(POS::getName, POS::setName);
        binder.forField(brand).withValidator(new BeanValidator(POS.class, "brand")).bind(POS::getBrand, POS::setBrand);
        binder.forField(model).withValidator(new BeanValidator(POS.class, "model")).bind(POS::getModel, POS::setModel);
        binder.forField(city).withValidator(Objects::nonNull, "This field is required").bind(POS::getCity, POS::setCity);
        binder.forField(address).withValidator(new BeanValidator(POS.class, "address")).bind(POS::getAddress, POS::setAddress);
        binder.forField(countryCode)
                .withValidator(
                        code -> code.matches("^\\+[0-9]{1,3}+$\n"), "Please provide a valid country code starting with '+' followed by max 3 digits"
                ).bind(POS::getCountryCode, POS::setCountryCode);
        binder.forField(telephone).withValidator(
                code -> code.matches("^[0-9]+$\n"), "Please provide a valid phone number containing only digits"
        ).bind(POS::getTelephone, POS::setTelephone);
        binder.forField(opening).withValidator(Objects::nonNull, "This field is required").bind(POS::getOpening, POS::setOpening);
        binder.forField(closing).withValidator(Objects::nonNull, "This field is required").bind(POS::getClosing, POS::setClosing);
        binder.forField(connectionType).withValidator(Objects::nonNull, "This field is required").bind(POS::getConnType, POS::setConnType);

        VerticalLayout dialogLayout = new VerticalLayout(
                name, brand, model, city, address, phoneNumber, opening, closing, connectionType
        );

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setWidth("30vw");
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        return dialogLayout;
    }

    private Button getOperationButton(String text, SaveCallback saveCallback, ButtonVariant... variant) {
        Button saveButton = new Button(text, e -> {
            try {
                posService.save(binder.getBean());
                dialog.close();
                saveCallback.onSave();
            } catch (Exception ex){
                ErrorNotification.show("An Error occurred when saving. Please check provided data");
            }

        });
        saveButton.addThemeVariants(variant);

        return saveButton;
    }

    private Button getDeleteButton(DeleteCallback deleteCallback){
        Button deleteButton = new Button("Delete", buttonClickEvent -> {
            try {
                posService.delete(binder.getBean());
                dialog.close();
                deleteCallback.onDelete();
            } catch (Exception e){
                ErrorNotification.show("An Error occurred while trying to delete the entity");
            }

        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        return deleteButton;
    }

    public PosDialog setPosEntity(POS pos) {
        this.pos = pos;
        dialog.remove(dialogLayout);
        dialog.add(createDialogLayout());
        return this;
    }

    public Dialog getDialog() {
        return dialog;
    }

}
