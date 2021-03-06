package de.spiritaner.maz.model;

import de.spiritaner.maz.controller.yearabroad.SiteEditorDialogController;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

/**
 * @author Florian Schwab
 * @version 0.0.1
 */
@Entity
@Audited
@Identifiable.Annotation(editorDialogClass = SiteEditorDialogController.class, identifiableName = "Einsatzstelle")
@NamedQueries({
		  @NamedQuery(name = "Site.findAll", query = "SELECT s FROM Site s"),
})
public class Site implements Identifiable  {

	private LongProperty id;
	private StringProperty name;
	private StringProperty organization;
	private Address address;

	private List<EPNumber> epNumbers;
	private List<Responsible> responsibles;
	private List<YearAbroad> yearsAbroad;

	public Site() {
		id = new SimpleLongProperty();
		name = new SimpleStringProperty();
		organization = new SimpleStringProperty();
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id.get();
	}
	public LongProperty idProperty() {
		return id;
	}
	public void setId(long id) {
		this.id.set(id);
	}

	@Column(nullable = false)
	public String getName() { return name.get(); }
	public StringProperty nameProperty() { return name; }
	public void setName(String name) { this.name.set(name); }

	@OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
	public List<EPNumber> getEpNumbers() { return epNumbers; }
	public void setEpNumbers(List<EPNumber> epNumbers) { this.epNumbers = epNumbers; }

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="addressId", nullable = false)
	public Address getAddress() { return address; }
	public void setAddress(Address address) { this.address = address; }

	/**
	 * The organization is used together with the address for correspondence
	 *
	 * @return
	 */
	@Column(nullable = false)
	public String getOrganization() {
		return organization.get();
	}
	public StringProperty organizationProperty() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization.set(organization);
	}

	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
	public List<Responsible> getResponsibles() {
		return responsibles;
	}

	public void setResponsibles(List<Responsible> responsibles) {
		this.responsibles = responsibles;
	}

	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
	public List<YearAbroad> getYearsAbroad() {
		return yearsAbroad;
	}

	public void setYearsAbroad(List<YearAbroad> yearsAbroad) {
		this.yearsAbroad = yearsAbroad;
	}

	@Transient
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Site) && ((Site) obj).getId().equals(this.getId());
	}
}
