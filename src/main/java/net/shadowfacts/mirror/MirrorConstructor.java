package net.shadowfacts.mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A mirror constructor
 *
 * @author shadowfacts
 *
 * @see MirrorClass#constructor(Class[])
 */
public class MirrorConstructor<T> {

	private Constructor<T> constructor;

	MirrorConstructor(Constructor<T> constructor) {
		this.constructor = constructor;
	}

	/**
	 * @return The Java {@link Constructor} corresponding to this mirror constructor
	 */
	public Constructor<T> unwrap() {
		return constructor;
	}

	/**
	 * @return An array of the types this constructor accepts
	 */
	public MirrorClass<?>[] parameterTypes() {
		Class<?>[] types = constructor.getParameterTypes();
		MirrorClass<?>[] mirrors = new MirrorClass<?>[types.length];
		for (int i = 0; i < types.length; i++) {
			mirrors[i] = Mirror.of(types[i]);
		}
		return mirrors;
	}

	/**
	 * Invokes the constructor with the given arguments and returns the new instance
	 * @param args The arguments to invoke the constructor with
	 * @return The new instance
	 */
	public T invoke(Object... args) {
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		if (args.length != parameterTypes.length) {
			throw new IllegalArgumentException("Wrong number of arguments");
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
				throw new IllegalArgumentException("Argument " + i + " was of wrong type. Expected " + parameterTypes[i].getName() + " got " + args[i].getClass().getName());
			}
		}
		try {
			return constructor.newInstance(args);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MirrorConstructor<?> that = (MirrorConstructor<?>) o;

		return constructor.equals(that.constructor);

	}

	@Override
	public int hashCode() {
		return constructor.hashCode();
	}

}
