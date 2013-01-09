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

	public static Weekday fromString(String string) {
		string = string.trim().toLowerCase();
		for (Weekday day : values()) {
			if (day.getLabel().startsWith(string)) {
				return day;
			}
		}
		return null;
	}
}
