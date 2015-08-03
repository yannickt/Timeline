package me.tailliez.timeline;

import java.util.Objects;

public class Entity {

	private Integer id;
	private Range<Integer> range;

	private int serieId;

	private Integer data;
	private static final RangeMetadata<Entity, Range<Integer>, Integer> rangeMetadata = new RangeMetadata<>(
            Entity::getRange, Entity::setRange, (Entity e1, Entity e2) -> e1 != null && e2 != null && Objects.equals(e1.getSerieId(), e2.getSerieId()));

    public Entity(Integer id, int serieId, Integer start, Integer end, Integer data) {
        super();
        this.id = id;
        this.serieId = serieId;
        this.range = new RangeIE<Integer>(start,end);
        this.data = data;
    }

    public Entity(Entity obj) {
        super();
        this.id = obj.id;
        this.serieId = obj.serieId;
        this.range = new Range<Integer>(obj.range);
        this.data = obj.data;
    }

    public RangeMetadata<Entity, Range<Integer>, Integer> getRangeMetadata() {
		return rangeMetadata;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Range<Integer> getRange() {
		return range;
	}

	public void setRange(Range<Integer> range) {
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
