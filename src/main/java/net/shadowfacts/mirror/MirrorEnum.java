package net.shadowfacts.mirror;

/**
 * A mirror enum
 *
 * @author shadowfacts
 *
 * @see Mirror#ofEnum(Class)
 */
public class MirrorEnum<E extends Enum<E>> extends MirrorClass<E> {

	MirrorEnum(Class<E> clazz) {
		super(clazz);
	}

	/**
	 * @return All the enum values of the class
	 */
	public E[] values() {
		return clazz.getEnumConstants();
	}

	/**
	 * Retrieves the element directly succeeding the given one
	 * @param current The current element
	 * @return The element directly succeeding the current one
	 */
	public E next(E current) {
		int currentId = current.ordinal();
		E[] values = values();
		int next = currentId + 1;
		if (next >= values.length) next = 0;
		return values[next];
	}

	/**
	 * Retrieves the element directly preceding the given on
	 * @param current The current element
	 * @return The element directly preceding the current one
	 */
	public E previous(E current) {
		int currentId = current.ordinal();
		E[] values = values();
		int prev = currentId - 1;
		if (prev < 0) prev = values.length - 1;
		return values[prev];
	}

}
