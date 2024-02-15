package org.example.view.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.BeanValidator;
import jakarta.annotation.Nullable;
import org.example.callback.DeleteCallback;
import org.example.callback.SaveCallback;
import org.example.constants.Operation;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.service.UserRoleService;
import org.example.service.UserService;
import org.example.view.ErrorNotification;

import java.util.Objects;

public class UserDialog extends Div {
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final Dialog dialog;
    private final Binder<User> binder;
    private final User user;

    public UserDialog(UserService userService, UserRoleService userRoleService, SaveCallback saveCallback,
                      DeleteCallback deleteCallback, Operation operation, @Nullable User user
    ){
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.user = Objects.nonNull(user) ? user : new User();

        dialog = new Dialog();
        binder = new Binder<>(User.class);
        Div dialogLayout = createDialogLayout();

        Button operationButton = operation.equals(Operation.CREATE) ?
                getOperationButton("Add", saveCallback, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS) :
                getOperationButton("Update", saveCallback, ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.add(dialogLayout);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(operationButton);

        if (operation.equals(Operation.CREATE)) {
            dialog.setHeaderTitle("New User");
        } else {
            dialog.setHeaderTitle("Update User");
            dialog.getFooter().add(getDeleteButton(deleteCallback));
        }

        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }

    private Div createDialogLayout() {
        binder.setBean(user);

        TextField fullName = new TextField("Full name");
        fullName.setWidth("30%");

        TextField email = new TextField("Email");
        email.setWidth("40%");
        email.getStyle().setMarginLeft("5%");

        TextField username = new TextField("Username");
        username.setWidth("30%");

        PasswordField password = new PasswordField("Password");
        password.setRevealButtonVisible(true);
        password.setWidth("30%");
        password.getStyle().setMarginLeft("5%");

        ComboBox<UserRole> role = new ComboBox<>("Role");
        role.setItems(userRoleService.findAll());
        role.setItemLabelGenerator(UserRole::getName);
        role.setWidth("30%");
        role.getStyle().setMarginLeft("5%");

        binder.forField(fullName).withValidator(new BeanValidator(User.class, "name")).bind(User::getName, User::setName);
        binder.forField(email).withValidator(new BeanValidator(User.class, "email")).bind(User::getEmail, User::setEmail);
        binder.forField(username).withValidator(new BeanValidator(User.class, "username")).bind(User::getUsername, User::setUsername);
        binder.forField(password).withValidator(new BeanValidator(User.class, "password")).bind(User::getPassword, User::setPassword);
        binder.forField(role).withValidator(Objects::nonNull, "Please select user role").bind(User::getRole, User::setRole);

        Div dialogLayout = new Div(fullName, email, username, password, role);

        dialogLayout.setWidth("55vw");

        return dialogLayout;
    }

    private Button getOperationButton(String text, SaveCallback saveCallback, ButtonVariant... variant) {
        Button operationButton = new Button(text, e -> {
            try {
                userService.save(binder.getBean());
                dialog.close();
                saveCallback.onSave();
            } catch (Exception ex){
                ErrorNotification.show("An Error occurred when saving. Please check provided data");
                ex.printStackTrace();
            }
        });
        operationButton.addThemeVariants(variant);

        return operationButton;
    }

    private Button getDeleteButton(DeleteCallback deleteCallback){
        Button deleteButton = new Button("Delete", buttonClickEvent -> {
            try {
                userService.delete(binder.getBean());
                dialog.close();
                deleteCallback.onDelete();
            } catch (Exception e){
                ErrorNotification.show("An Error occurred while trying to delete the entity");
                e.printStackTrace();
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        return deleteButton;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
