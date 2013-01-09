package at.fhooe.restaurantfinder.shared.bo;

import java.io.Serializable;

public class Time implements Serializable {

	private static final long serialVersionUID = 5412377939339739744L;

	private Long id;

	private int hour;

	private int minute;

	public Time() {
	}

	public Time(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hour;
		result = prime * result + minute;
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
		Time other = (Time) obj;
		if (hour != other.hour)
			return false;
		if (minute != other.minute)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return hour + ":" + (minute < 10 ? "0" : "") + minute;
	}

	public static Time fromString(String string) {
		String[] parts = string.split(":");
		int hour = Integer.parseInt(parts[0]);
		int minute = Integer.parseInt(parts[1]);
		return new Time(hour, minute);
	}
}
