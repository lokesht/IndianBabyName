package in.sel.model;

import java.util.Collection;
import java.util.Comparator;

public class M_Name implements Comparator<M_Name> {

	String name_ma;
	String name_en;
	int frequency;
	int id;
	String gender_cast;

	public M_Name(int id, String gender_cast) {
		super();
		this.id = id;
		this.gender_cast = gender_cast;
	}

	public M_Name(String name_en, String name_ma, int frequency) {
		super();
		this.name_en = name_en;
		this.name_ma = name_ma;
		this.frequency = frequency;
	}

	public M_Name(String name_ma, String name_en, int frequency, String description) {
		super();
		this.name_ma = name_ma;
		this.name_en = name_en;
		this.frequency = frequency;
		this.gender_cast = description;
	}

	public M_Name(String name_ma, String name_en, int frequency, int id, String description) {
		super();
		this.name_ma = name_ma;
		this.name_en = name_en;
		this.frequency = frequency;
		this.id = id;
		this.gender_cast = description;
	}

	public String getDescription() {
		return gender_cast;
	}

	public void setDescription(String description) {
		this.gender_cast = description;
	}

	public M_Name(String name_ma, String name_en, int frequency, int id) {
		super();
		this.name_ma = name_ma;
		this.name_en = name_en;
		this.frequency = frequency;
		this.id = id;
	}

	public String getName_ma() {
		return name_ma;
	}

	public void setName_ma(String name_ma) {
		this.name_ma = name_ma;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGender_cast() {
		return gender_cast;
	}

	public void setGender_cast(String gender_cast) {
		this.gender_cast = gender_cast;
	}

	@Override
	public int compare(M_Name lhs, M_Name rhs) {
		return lhs.getFrequency() - rhs.getFrequency();
	}
}
