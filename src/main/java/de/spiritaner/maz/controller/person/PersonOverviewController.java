package de.spiritaner.maz.controller.person;

import de.spiritaner.maz.controller.OverviewController;
import de.spiritaner.maz.model.Person;
import de.spiritaner.maz.util.DataDatabase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.controlsfx.control.ToggleSwitch;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

public class PersonOverviewController extends OverviewController<Person> {

	private static final Logger logger = Logger.getLogger(PersonOverviewController.class);

	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> familyNameColumn;
	@FXML
	private TableColumn<Person, String> birthNameColumn;
	@FXML
	private TableColumn<Person, String> birthplaceColumn;
	@FXML
	private TableColumn<Person, Long> idColumn;
	@FXML
	private TableColumn<Person, LocalDate> birthdayColumn;
	@FXML
	private TableColumn<Person, LocalDate> ageColumn;
	@FXML
	private TableColumn<Person, String> genderColumn;
	@FXML
	private TableColumn<Person, String> dioceseColumn;
	@FXML
	private ToggleSwitch personDetailsToggle;
	@FXML
	private Button personSearchButton;
	@FXML
	private TextField personSearchText;

	public PersonOverviewController() {
		super(Person.class);
	}

	@Override
	protected void postInit() {
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		familyNameColumn.setCellValueFactory(cellData -> cellData.getValue().familyNameProperty());
		birthNameColumn.setCellValueFactory(cellData -> cellData.getValue().birthNameProperty());
		genderColumn.setCellValueFactory(cellData -> cellData.getValue().getGender().descriptionProperty());
		dioceseColumn.setCellValueFactory(cellData -> cellData.getValue().getDiocese(true).descriptionProperty());
		birthdayColumn.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty());
		ageColumn.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty());
		birthplaceColumn.setCellValueFactory(cellData -> cellData.getValue().birthplaceProperty());
		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

		final DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		birthdayColumn.setCellFactory(column -> {
			return new TableCell<Person, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						// Format date.
						setText(myDateFormatter.format(item));
					}
				}
			};
		});

		final LocalDate today = LocalDate.now();

		ageColumn.setCellFactory(column -> {
			return new TableCell<Person, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						// Format date.
						long years = item.until(today, ChronoUnit.YEARS);
						setText(years+" Jahre");
					}
				}
			};
		});

		personSearchButton.setDefaultButton(true);
	}

	@Override
	protected void preCreate(Person object) {

	}

	@Override
	protected void preEdit(Person object) {

	}

	@Override
	protected void preRemove(Person obsoleteEntity) {

	}

	@Override
	protected Collection preLoad(EntityManager em) {
		return em.createNamedQuery("Person.findAll", Person.class).getResultList();
	}

	@Override
	protected void postLoad(Collection loadedObjs) {

	}

	@Override
	protected String getLoadingText() {
		return "Lade Personen ...";
	}

	@Override
	protected void handleException(RollbackException e) {

	}

	@Override
	protected void preInit() {

	}

	/*public void removePerson(ActionEvent actionEvent) {
		Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
		final Optional<ButtonType> result = RemoveDialog.create(selectedPerson, stage).showAndWait();

		if (result.get() == ButtonType.OK) {
			try {
				EntityManager em = DataDatabase.getFactory().createEntityManager();
				em.getTransaction().begin();
				Person obsoletePerson = em.find(Person.class, selectedPerson.getId());
				em.remove(obsoletePerson);
				em.getTransaction().commit();

				loadAllPersons();
			} catch (RollbackException e) {
				// TODO show graphical error message in better way
				ExceptionDialog.show(e);
			}
		}
	}*/

	/*public TableView<Person> getPersonTable() {
		return personTable;
	}*/

	public ToggleSwitch getPersonDetailsToggle() {
		return personDetailsToggle;
	}

	public void searchForPersons(ActionEvent actionEvent) {
		EntityManager em = DataDatabase.getFactory().createEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Person.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("firstName", "familyName")
				  .matching(personSearchText.getText()+"*")
				  .createQuery();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query jpaQuery =
				  fullTextEntityManager.createFullTextQuery(luceneQuery, Person.class);

		// execute search
		List result = jpaQuery.getResultList();

		getTable().getItems().clear();
		getTable().getItems().addAll(FXCollections.observableArrayList(result));

		em.getTransaction().commit();
	}
}
