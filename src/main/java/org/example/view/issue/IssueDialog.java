package org.example.view.issue;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.validator.BeanValidator;
import jakarta.annotation.Nullable;
import org.example.callback.DeleteCallback;
import org.example.callback.SaveCallback;
import org.example.constants.Operation;
import org.example.entity.*;
import org.example.service.*;
import org.example.view.ErrorNotification;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class IssueDialog extends Div {
    private final IssueStatusService issueStatusService;
    private final PosService posService;
    private final UserService userService;
    private final IssueService issueService;
    private final IssueTypeService issueTypeService;
    private final Dialog dialog;
    private final Binder<Issue> binder;
    private final Issue issue;

    public IssueDialog(IssueService issueService, UserService userService, PosService posService, IssueStatusService issueStatusService,
                       IssueTypeService issueTypeService, SaveCallback saveCallback, DeleteCallback deleteCallback, Operation operation,
                       @Nullable Issue issue){
        this.issueStatusService = issueStatusService;
        this.posService = posService;
        this.userService = userService;
        this.issueService = issueService;
        this.issueTypeService = issueTypeService;
        this.issue = Objects.nonNull(issue) ? issue : new Issue();

        dialog = new Dialog();
        binder = new Binder<>(Issue.class);
        Div dialogLayout = createDialogLayout();

        Button operationButton = null;
        if (operation.equals(Operation.CREATE)){
            operationButton = getOperationButton("Add", saveCallback, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        } else {
            if (issueService.isUserAuthorizedToModifyIssue(issue)){
                operationButton = getOperationButton("Update", saveCallback, ButtonVariant.LUMO_PRIMARY);
            }
        }
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.add(dialogLayout);
        dialog.getFooter().add(cancelButton);
        if (Objects.nonNull(operationButton)){
            dialog.getFooter().add(operationButton);
        }

        if (operation.equals(Operation.CREATE)) {
            dialog.setHeaderTitle("New Issue");
        } else {
            dialog.setHeaderTitle("Update Issue");
            if (issueService.isUserAuthorizedToModifyIssue(issue)){
                dialog.getFooter().add(getDeleteButton(deleteCallback));
            }
        }

        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");
    }

    private Div createDialogLayout() {
        binder.setBean(issue);

        TextField title = new TextField("Title");
        title.setWidth("40%");
        title.getStyle().setMarginRight("60%");

        ComboBox<POS> posName = new ComboBox<>("POS");
        posName.setItems(posService.findAll());
        posName.setItemLabelGenerator(POS::getName);
        posName.setWidth("31%");
        posName.getStyle().setMarginRight("3%");

        ComboBox<IssueType> mainType = new ComboBox<>("Main Type");
        mainType.setItems(issueTypeService.findAll());
        mainType.setItemLabelGenerator(IssueType::getName);
        mainType.setWidth("31%");
        mainType.getStyle().setMarginRight("3%");

        ComboBox<IssueType> subType = new ComboBox<>("Sub Type");
        subType.setItems(issueTypeService.findAll());
        subType.setItemLabelGenerator(IssueType::getName);
        subType.setWidth("31%");

        ComboBox<Integer> priority = new ComboBox<>("Priority");
        priority.setItems(List.of(1,2,3,4,5));
        priority.setWidth("31%");
        priority.getStyle().setMarginRight("3%");

        ComboBox<User> assignedTo = new ComboBox<>("Assigned to");
        assignedTo.setItems(userService.findAll());
        assignedTo.setItemLabelGenerator(User::getName);
        assignedTo.getStyle().setMarginRight("3%").setWidth("31%");

        ComboBox<Status> status = new ComboBox<>("Status");
        status.setItems(issueStatusService.findAll());
        status.setItemLabelGenerator(Status::getName);
        status.setWidth("31%");

        ComboBox<User> createdBy = new ComboBox<>("Created by");
        createdBy.setItems(userService.findAll());
        createdBy.setItemLabelGenerator(User::getName);
        createdBy.getStyle().setMarginRight("3%").setWidth("31%");

        DatePicker creationDate = new DatePicker("Creation date");
        creationDate.getStyle().setMarginRight("3%").setWidth("31%");

        DatePicker assignedDate = new DatePicker("Assigned date");
        assignedDate.setWidth("31%");

        TextArea description = new TextArea("Description");
        description.getStyle().setMarginRight("5%").setWidth("40%").setMinHeight("10rem");

        TextArea solution = new TextArea("Solution");
        solution.getStyle().setWidth("40%").setHeight("10rem");

        binder.forField(title).withValidator(new BeanValidator(Issue.class, "title")).bind(Issue::getTitle, Issue::setTitle);
        binder.forField(posName).withValidator(Objects::nonNull, "Please select the POS").bind(Issue::getPos, Issue::setPos);
        binder.forField(mainType).withValidator(Objects::nonNull, "Please select the main type").bind(Issue::getMainType, Issue::setMainType);
        binder.forField(subType).withValidator(Objects::nonNull, "Please select the sub type").bind(Issue::getSubType, Issue::setSubType);
        binder.forField(priority).withValidator(Objects::nonNull, "Please select priority").bind(Issue::getPriority, Issue::setPriority);
        binder.forField(assignedTo).bind(Issue::getAssignedTo, Issue::setAssignedTo);
        binder.forField(status).withValidator(Objects::nonNull, "Please select the status").bind(Issue::getStatus, Issue::setStatus);
        binder.forField(description).withValidator(new BeanValidator(Issue.class, "description")).bind(Issue::getDescription, Issue::setDescription);
        binder.forField(solution).withValidator(new BeanValidator(Issue.class, "solution")).bind(Issue::getSolution, Issue::setSolution);
        binder.forField(createdBy).bindReadOnly(Issue::getCreatedBy);
        binder.forField(creationDate).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault())).bindReadOnly(Issue::getCreationDate);
        binder.forField(assignedDate).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault())).bindReadOnly(Issue::getAssignedDate);

        Div dialogLayout = new Div(
                setAllDisabledIfNotAllowed(
                        title, posName, mainType, subType, priority, assignedTo, status, description, solution, createdBy, creationDate, assignedDate
                )
        );

        dialogLayout.setWidth("75vw");

        return dialogLayout;
    }

    private Button getOperationButton(String text, SaveCallback saveCallback, ButtonVariant... variant) {
        Button operationButton = new Button(text, buttonClickEvent -> {
            try {
                issueService.save(binder.getBean());
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
                issueService.delete(binder.getBean());
                dialog.close();
                deleteCallback.onDelete();
            } catch (Exception ex){
                ErrorNotification.show("An Error occurred while trying to delete the entity");
                ex.printStackTrace();
            }

        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        return deleteButton;
    }

    public Component[] setAllDisabledIfNotAllowed(AbstractSinglePropertyField<?,?>... components){
        if (!issueService.isUserAuthorizedToModifyIssue(issue)){
            for (AbstractSinglePropertyField<?,?> cmp : components){
                cmp.setEnabled(false);
            }
        }

        return components;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
