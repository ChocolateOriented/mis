package com.mo9.risk.modules.dunning.enums;

public enum WorkPayday {

	pd0("N/A", 0),
	pd1("1日-5日", 1),
	pd2("6日-10日", 2),
	pd3("11日-15日", 3),
	pd4("16日-20日", 4),
	pd5("21日-25日", 5),
	pd6("25日-31日", 6);
	
	private String desc;
	private int value;
	
	private WorkPayday(String desc, int value) {
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
	public static WorkPayday valueOf(int value) {
		for (WorkPayday pd : WorkPayday.values()) {
			if (pd.value == value) {
				return pd;
			}
		}
		throw new IllegalArgumentException(
				"No enum constant value " + WorkPayday.class.getCanonicalName() + ":" + value);
	}
}
