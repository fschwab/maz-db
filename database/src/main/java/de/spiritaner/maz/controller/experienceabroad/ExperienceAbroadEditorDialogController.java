package de.spiritaner.maz.controller.experienceabroad;

import de.spiritaner.maz.controller.EditorDialogController;
import de.spiritaner.maz.controller.person.PersonEditorController;
import de.spiritaner.maz.model.ExperienceAbroad;
import de.spiritaner.maz.util.database.CoreDatabase;
import de.spiritaner.maz.view.dialog.EditorDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.net.URL;
import java.util.ResourceBundle;

// TODO Extract strings
@EditorDialog.Annotation(fxmlFile = "/fxml/experienceabroad/experienceabroad_editor_dialog.fxml", objDesc = "Mitlebezeit")
public class ExperienceAbroadEditorDialogController extends EditorDialogController<ExperienceAbroad> {

	final static Logger logger = Logger.getLogger(ExperienceAbroadEditorDialogController.class);

	public GridPane personEditor;
	public PersonEditorController personEditorController;
	public GridPane experienceAbroadEditor;
	public ExperienceAbroadEditorController experienceAbroadEditorController;

	@Override
	protected void bind(ExperienceAbroad experienceAbroad) {
		personEditorController.person.bindBidirectional(experienceAbroad.person);
		personEditorController.readOnly.bind(experienceAbroad.person.isNotNull());

		experienceAbroadEditorController.experienceAbroad.bindBidirectional(identifiable);
	}

	@Override
	protected boolean allValid() {
		boolean personValid = personEditorController.isValid();
		boolean experienceAbroadValid = experienceAbroadEditorController.isValid();

		return personValid && experienceAbroadValid;
	}
}
