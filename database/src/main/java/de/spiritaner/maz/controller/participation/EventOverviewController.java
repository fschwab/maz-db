package de.spiritaner.maz.controller.participation;

import de.spiritaner.maz.controller.OverviewController;
import de.spiritaner.maz.dialog.ExceptionDialog;
import de.spiritaner.maz.model.Event;
import de.spiritaner.maz.util.document.ParticipantList;
import de.spiritaner.maz.util.factory.DateAsStringListCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.util.Collection;

@OverviewController.Annotation(fxmlFile = "/fxml/participation/event_overview.fxml", objDesc = "Veranstaltung")
public class EventOverviewController extends OverviewController<Event> {

	@FXML private TableColumn<Event, String> eventTypeColumn;
	@FXML private TableColumn<Event, String> nameColumn;
	@FXML private TableColumn<Event, String> descriptionColumn;
	@FXML private TableColumn<Event, LocalDate> fromDateColumn;
	@FXML private TableColumn<Event, LocalDate> toDateColumn;
	@FXML private TableColumn<Event, Long> idColumn;

	public EventOverviewController() {
		super(Event.class, true);
	}

	@Override
	protected void handleException(RollbackException e) {
		ExceptionDialog.show(e);
	}

	@Override
	protected String getLoadingText() {
		return "Lade Veranstaltungen ...";
	}

	@Override
	protected Collection<Event> preLoad(EntityManager em) {
		return em.createNamedQuery("Event.findAll", Event.class).getResultList();
	}

	@Override
	protected void postInit() {
		eventTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getEventType().descriptionProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
		fromDateColumn.setCellValueFactory(cellData -> cellData.getValue().fromDateProperty());
		toDateColumn.setCellValueFactory(cellData -> cellData.getValue().toDateProperty());
		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

		fromDateColumn.setCellFactory(column -> DateAsStringListCell.localDateTableCell());
		toDateColumn.setCellFactory(column -> DateAsStringListCell.localDateTableCell());
	}

	public void createParticipantList(ActionEvent actionEvent) {
		Event selectedEvent = getTable().getSelectionModel().getSelectedItem();

		if (selectedEvent != null) {
			ParticipantList.forEvent(selectedEvent, getStage());
		}
	}
}