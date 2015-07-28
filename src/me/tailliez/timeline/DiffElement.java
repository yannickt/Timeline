package me.tailliez.timeline;

public class DiffElement<E> {

	private E src;
	private E dest;
	
	public DiffElement(E src, E dest) {
		super();
		this.src = src;
		this.dest = dest;
	}

	public boolean isAllreadyExists() {
		return src != null && dest != null;
	}
	
	public boolean isAdded() {
		return src == null && dest != null;
	}
	
	public boolean isRemoved() {
		return src != null && dest == null;
	}
	
	public E getSrc() {
		return src;
	}
	public void setSrc(E src) {
		this.src = src;
	}
	public E getDest() {
		return dest;
	}
	public void setDest(E dest) {
		this.dest = dest;
	}
}