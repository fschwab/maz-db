package de.spiritaner.maz.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Map;

/**
 * @author Florian Schwab
 * @version 0.0.1
 */
@Entity
@Audited
public class Site implements Identifiable  {

	private LongProperty id;
	private StringProperty name;
	private Map<String, String> epNumbers;

	private StringProperty organization;
	private Address address;

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

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name="id")
	@Column(name="value")
	@CollectionTable(name="SITE_EP_NUMBERS", joinColumns=@JoinColumn(name="epNumber"))
	public Map<String, String> getEpNumbers() { return epNumbers; }
	public void setEpNumbers(Map<String, String> epNumbers) { this.epNumbers = epNumbers; }

	@ManyToOne(fetch = FetchType.EAGER)
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
}
