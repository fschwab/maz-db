package de.spiritaner.maz.view.component;

import de.spiritaner.maz.view.binding.Bindable;
import de.spiritaner.maz.view.renderer.DatePickerFormatter;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class BindableDatePicker extends DatePicker implements Bindable<LocalDate> {

	public StringProperty val = new SimpleStringProperty();

	public BindableDatePicker() {
		super.setConverter(new DatePickerFormatter());
	}

	@Override
	public String getVal() {
		return val.get();
	}

	@Override
	public StringProperty valProperty() {
		return val;
	}

	@Override
	public void setVal(String val) {
		this.val.set(val);
	}

	@Override
	public void bind(Property<LocalDate> p) {
		super.valueProperty().bindBidirectional(p);
	}
}
