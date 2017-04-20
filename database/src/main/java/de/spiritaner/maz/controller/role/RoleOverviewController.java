package de.spiritaner.maz.controller.role;

import de.spiritaner.maz.controller.OverviewController;
import de.spiritaner.maz.dialog.ExceptionDialog;
import de.spiritaner.maz.model.Person;
import de.spiritaner.maz.model.Role;
import de.spiritaner.maz.model.meta.RoleType;
import de.spiritaner.maz.util.factory.MetaClassTableCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.util.Collection;

public class RoleOverviewController extends OverviewController<Role> {

	@FXML
	private TableColumn<Role, RoleType> roleTypeColumn;
	@FXML
	private TableColumn<Role, Long> idColumn;

	private Person person;

	public RoleOverviewController() {
		super(Role.class, true);
	}

	@Override
	protected void preCreate(Role object) {
		object.setPerson(person);
	}

	@Override
	protected Collection<Role> preLoad(EntityManager em) {
		if(person != null) {
			Hibernate.initialize(person.getRoles());
			return FXCollections.observableArrayList(person.getRoles());
		}
		return null;
	}

	@Override
	protected String getLoadingText() {
		return "Lade Funktionen ...";
	}

	@Override
	protected void handleException(RollbackException e) {
		ExceptionDialog.show(e);
	}

	@Override
	protected void postInit() {
		roleTypeColumn.setCellValueFactory(cellData -> cellData.getValue().roleTypeProperty());
		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

		roleTypeColumn.setCellFactory(column -> new MetaClassTableCell<>());
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
