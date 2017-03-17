package de.spiritaner.maz.controller.person;

import de.spiritaner.maz.controller.Controller;
import de.spiritaner.maz.controller.approval.ApprovalOverviewController;
import de.spiritaner.maz.controller.contactmethod.ContactMethodOverviewController;
import de.spiritaner.maz.controller.participant.EventOverviewController;
import de.spiritaner.maz.controller.participant.ParticipantOverviewController;
import de.spiritaner.maz.controller.residence.ResidenceOverviewController;
import de.spiritaner.maz.model.Person;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.controlsfx.control.MaskerPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonPageController implements Initializable, Controller {

	private static final Logger logger = Logger.getLogger(PersonPageController.class);

	@FXML
	private TabPane detailTabPane;

	@FXML
	private AnchorPane personOverview;
	@FXML
	private PersonOverviewController personOverviewController;
	@FXML
	private AnchorPane detailPane;

	@FXML
	private MaskerPane detailsMasker;
	@FXML
	private SplitPane personSplitPane;

	@FXML
	private AnchorPane personResidences;
	@FXML
	private ResidenceOverviewController personResidencesController;

	@FXML
	private AnchorPane personContactMethods;
	@FXML
	private ContactMethodOverviewController personContactMethodsController;

	@FXML
	private AnchorPane personParticipations;
	@FXML
	private ParticipantOverviewController personParticipationsController;

	@FXML
	private AnchorPane personApprovals;
	@FXML
	private ApprovalOverviewController personApprovalsController;

	private Stage stage;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		personOverviewController.getTable().getSelectionModel().selectedItemProperty().addListener((observable, oldPerson, newPerson) -> {
			if (newPerson != null && personOverviewController.getPersonDetailsToggle().isSelected()) {
				loadPersonDetails(newPerson);
			} else if (newPerson == null) {
				detailsMasker.setVisible(true);
			}
		});

		personOverviewController.getTable().setRowFactory(tv -> {
			TableRow<Person> row = new TableRow<>();

			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Person selectedPerson = row.getItem();
					loadPersonDetails(selectedPerson);
				}
			});

			return row;
		});

		detailTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
			reloadSpecificTab(newTab.getId());
		});
	}

	private void reloadSpecificTab(String tabFxId) {
		switch(tabFxId) {
			case "residenceTab": personResidencesController.onReopen(); break;
			case "approvalTab": personApprovalsController.onReopen(); break;
			case "contactMethodTab": personContactMethodsController.onReopen(); break;
			case "participationTab": personParticipationsController.onReopen(); break;
		}
	}

	private void loadPersonDetails(Person person) {
		Platform.runLater(() -> {
			// TODO implement person history display
			//AuditReader reader = AuditReaderFactory.get(DataDatabase.getFactory().createEntityManager());
			//List<Number> revisions = reader.getRevisions(Person.class, person.getId());

			/*for (Number revision : revisions) {
				logger.info("Found revision " + revision + " for person with first name " + person.getFirstName());
				Person revPerson = reader.find(Person.class, person.getId(), revision);
				logger.info("First in this revision was: " + revPerson.getFirstName());
			}*/

			personResidencesController.setPerson(person);
			personContactMethodsController.setPerson(person);
			personApprovalsController.setPerson(person);
			personParticipationsController.setPerson(person);

			detailsMasker.setVisible(false);

			reloadSpecificTab(detailTabPane.getSelectionModel().getSelectedItem().getId());
		});
	}

	public void setStage(Stage stage) {
		this.stage = stage;

		personOverviewController.setStage(stage);
		personResidencesController.setStage(stage);
		//personEventsController.setStage(stage);
		personContactMethodsController.setStage(stage);
	}

	@Override
	public void onReopen() {
		personOverviewController.onReopen();
	}
}
