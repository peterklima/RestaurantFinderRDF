package at.fhooe.restaurantfinder.shared.bo;

public enum Weekday {
	MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

	private final String label;

	private Weekday() {
		label = name().toLowerCase();
	}

	public String getLabel() {
		return label;
	}
}
