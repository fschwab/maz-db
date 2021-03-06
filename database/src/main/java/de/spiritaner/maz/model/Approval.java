package de.spiritaner.maz.model;

import de.spiritaner.maz.controller.approval.ApprovalEditorDialogController;
import de.spiritaner.maz.model.meta.ApprovalType;
import javafx.beans.property.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author Florian Schwab
 * @version 0.0.1
 */
@Entity
@Audited
@Identifiable.Annotation(editorDialogClass = ApprovalEditorDialogController.class, identifiableName = "Einwilligung")
@NamedQueries({
		  @NamedQuery(name = "Approval.findStdApprovalsForPerson", query = "SELECT a FROM Approval a WHERE a.approvalType<=100 AND a.person=:person")
})
public class Approval implements Identifiable {

	private LongProperty id;

	private ObjectProperty<Person> person;
	private ObjectProperty<ApprovalType> approvalType;
	private BooleanProperty approved;

	public Approval() {
		approved = new SimpleBooleanProperty();
		id = new SimpleLongProperty();
		person = new SimpleObjectProperty<>();
		approvalType = new SimpleObjectProperty<>();
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id.get();
	}
	public void setId(Long id) {
		this.id.set(id);
	}
	public LongProperty idProperty() { return id; }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="personId", nullable = false)
	public Person getPerson() {
		return person.get();
	}
	public void setPerson(Person person) {
		this.person.set(person);
	}

	public ObjectProperty<Person> personProperty() {
		return person;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "approvalTypeId", nullable = false)
	public ApprovalType getApprovalType() {
		return approvalType.get();
	}
	public void setApprovalType(ApprovalType approvalType) {
		this.approvalType.set(approvalType);
	}

	public ObjectProperty<ApprovalType> approvalTypeProperty() {
		return approvalType;
	}

	@Column(nullable = false)
	public boolean isApproved() {
		return approved.get();
	}
	public BooleanProperty approvedProperty() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved.set(approved);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Approval) && (((Approval) obj).getId().equals(this.getId()));
	}
}
