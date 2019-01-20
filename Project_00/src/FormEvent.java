import java.util.EventObject;

public class FormEvent extends EventObject {
    private String name, occupation, empCat;
    private int ageCategoryId;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public FormEvent(Object source) {
        super(source);
    }

    public FormEvent(Object source, String name, String occupation, int ageCategoryId, String empCat) {
        super(source);
        this.name = name;
        this.occupation = occupation;
        this.setAgeCategoryId(ageCategoryId);
        this.setEmpCat(empCat);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

	public int getAgeCategoryId() {
		return ageCategoryId;
	}

	public void setAgeCategoryId(int ageCategoryId) {
		this.ageCategoryId = ageCategoryId;
	}

	public String getEmpCat() {
		return empCat;
	}

	public void setEmpCat(String empCat) {
		this.empCat = empCat;
	}

}
