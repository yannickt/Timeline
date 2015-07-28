package me.tailliez.timeline;

public class Entity {

	private Integer id;
	private Integer start;
	private Integer end;
	
	private int serieId;
	
	private Integer data;

	public Entity(int id, int serieId, Integer start, Integer end, Integer data) {
		super();
		this.id = id;
		this.serieId = serieId;
		this.start = start;
		this.end = end;
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Entity entity = (Entity) o;
		return (id != null ? id.equals(entity.id) : entity.id != null);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
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
