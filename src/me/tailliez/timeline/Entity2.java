package me.tailliez.timeline;

public class Entity2 {

	private Integer id;
	private RangeInteger range;

	private int serieId;

	private Integer data;

	public Entity2(int id, int serieId, Integer start, Integer end, Integer data) {
		super();
		this.id = id;
		this.serieId = serieId;
		this.range = new RangeInteger(start,end);
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Entity2 entity = (Entity2) o;
		return (id != null ? id.equals(entity.id) : entity.id != null);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RangeInteger getRange() {
		return range;
	}

	public void setRange(RangeInteger range) {
		this.range = range;
	}

	public int getSerieId() {
		return serieId;
	}

	public void setSerieId(int serieId) {
		this.serieId = serieId;
	}

	public Integer getData() {
		return data;
	}

	public void setData(Integer data) {
		this.data = data;
	}
}
