package com.mo9.risk.modules.dunning.enums;

public enum WorkSalary {

	pd0("N/A", 0),
	pd1("1000-3000", 1),
	pd2("3000-5000", 2),
	pd3("5000-7000", 3),
	pd4("7000-9000", 4),
	pd5("9000+", 5);
	
	private String desc;
	private int value;
	
	private WorkSalary(String desc, int value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

    /**
     * Returns the enum constant of the specified value.  The name must 
     * match exactly the value of an enum constant in this type.
     *
     * @param value the field {@code value} of the constant
     * @return the enum constant of the specified enum type with the
     *      specified name
     * @throws IllegalArgumentException if the specified enum type has
     *         no constant with the specified value
     */
	public static WorkSalary valueOf(int value) {
		for (WorkSalary pd : WorkSalary.values()) {
			if (pd.value == value) {
				return pd;
			}
		}
		throw new IllegalArgumentException(
				"No enum constant value " + WorkSalary.class.getCanonicalName() + ":" + value);
	}
}
