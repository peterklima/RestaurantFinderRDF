package at.fhooe.restaurantfinder.shared.bo;

import java.io.Serializable;

public class BusinessHours implements Serializable {

	private static final long serialVersionUID = 3629206811086403924L;

	private Long id;

	private Weekday weekday;

	private Time start;

	private Time end;

	public BusinessHours() {
		weekday = Weekday.MONDAY;
		start = new Time(7, 0);
		end = new Time(12, 0);
	}

	public BusinessHours(Weekday weekday, Time start, Time end) {
		this.weekday = weekday;
		this.start = start;
		this.end = end;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Weekday getWeekday() {
		return weekday;
	}

	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((weekday == null) ? 0 : weekday.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessHours other = (BusinessHours) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (weekday != other.weekday)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return weekday.getLabel() + ": " + start.toString() + "-" + end.toString();
	}

	public static BusinessHours fromString(String string) {
		String[] parts1 = string.split(": ");
		String[] parts2 = parts1[1].split("-");
		BusinessHours hours = new BusinessHours();
		hours.setWeekday(Weekday.fromString(parts1[0]));
		hours.setStart(Time.fromString(parts2[0]));
		hours.setEnd(Time.fromString(parts2[1]));
		return hours;
	}
}
