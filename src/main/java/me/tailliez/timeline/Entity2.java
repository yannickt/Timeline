package me.tailliez.timeline;

public class Entity2 {

	private Integer id;
	private Range<Integer> range;

	private int serieId;

	private Integer data;
	private static final  RangeMetadata<Entity2, Range<Integer>, Integer> rangeMetadata = new RangeMetadata<>(
            Entity2::getRange, Entity2::setRange);

    public Entity2(int id, int serieId, Integer start, Integer end, Integer data) {
		super();
		this.id = id;
		this.serieId = serieId;
		this.range = new Range<Integer>(start,end);
		this.data = data;
	}

    public RangeMetadata<Entity2, Range<Integer>, Integer> getRangeMetadata() {
        return rangeMetadata;
    }

	/*public static <V extends Comparable<V>> RangeMetadata<Entity2, Range<V>, V> getRangeMetadata() {
		RangeMetadata<Entity2, Range<Integer>, Integer> metadata = new RangeMetadata<Entity2, Range<Integer>, Integer>(
				Entity2::getRange, Entity2::setRange);
		return metadata;
	}*/

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
