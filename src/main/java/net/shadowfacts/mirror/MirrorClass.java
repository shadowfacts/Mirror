package net.shadowfacts.mirror;

import net.shadowfacts.mirror.stream.FieldStream;
import net.shadowfacts.mirror.stream.MethodStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * A mirror class
 *
 * @author shadowfacts
 */
public class MirrorClass<T> {

	protected final Class<T> clazz;

	MirrorClass(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return The Java {@link Class} corresponding to this mirror class
	 */
	public Class<T> unwrap() {
		return clazz;
	}

	/**
	 * @return The simple name of the class
	 */
	public String simpleName() {
		return clazz.getSimpleName();
	}

	/**
	 * @return The full name of the class
	 */
	public String fullName() {
		return clazz.getName();
	}

	/**
	 * Retrieves the constructor for this class that takes the given types
	 * @param types The types that the constructor accepts
	 * @return The constructor
	 */
	public MirrorConstructor<T> constructor(Class<?>... types) {
		try {
			return Mirror.of(clazz.getConstructor(types));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks if the class has the given annotation
	 * @param clazz The annotation class
	 * @return If the class has the annotation
	 */
	public boolean hasAnnotation(Class<? extends Annotation> clazz) {
		return this.clazz.isAnnotationPresent(clazz);
	}

	/**
	 * Retrieves the given annotation on the class
	 * @param clazz The annotation class
	 * @param <A> The type of the annotation
	 * @return The annotation on the class
	 */
	public <A extends Annotation> A getAnnotation(Class<A> clazz) {
		return this.clazz.getAnnotation(clazz);
	}

	/**
	 * @return If this class is an inner class
	 */
	public boolean isInner() {
		return clazz.getEnclosingClass() != null;
	}

	/**
	 * @return If this class is not an inner class (a top level class)
	 */
	public boolean isNotInner() {
		return !isInner();
	}

	/**
	 * Tests if this class is a sub-class of the given class
	 * @param clazz The class
	 * @return If this class is a sub-class of the given class
	 */
	public boolean isSubClassOf(Class<?> clazz) {
		return clazz.isAssignableFrom(this.clazz);
	}

	/**
	 * Tests if this class is a sub-class of the given class
	 * @param clazz The class
	 * @return If this class is a sub-class of the given class
	 */
	public boolean isSubClassOf(MirrorClass<?> clazz) {
		return isSubClassOf(clazz.unwrap());
	}

	/**
	 * Tests if this class is a super-class of the given class
	 * @param clazz The class
	 * @return If this class is a super-class of the given class
	 */
	public boolean isSuperClassOf(Class<?> clazz) {
		return this.clazz.isAssignableFrom(clazz);
	}

	/**
	 * Tests if this class is a super-class of the given class
	 * @param clazz The class
	 * @return If this class is a super-class of the given class
	 */
	public boolean isSuperClassOf(MirrorClass<?> clazz) {
		return isSuperClassOf(clazz.unwrap());
	}

	/**
	 * @return If this class is an interface
	 */
	public boolean isInterface() {
		return clazz.isInterface();
	}

	/**
	 * @return If this class is not an interface
	 */
	public boolean isNotInterface() {
		return !isInterface();
	}

	/**
	 * @return The direct super-class of this class
	 */
	public MirrorClass<? super T> getSuperClass() {
		return Mirror.of(clazz.getSuperclass());
	}

	/**
	 * @return An array of the interfaces that this class implements
	 */
	public MirrorClass<?>[] getInterfaces() {
		Class<?>[] interfaces = clazz.getInterfaces();
		MirrorClass<?>[] mirrors = new MirrorClass<?>[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			mirrors[i] = Mirror.of(interfaces[i]);
		}
		return mirrors;
	}

	/**
	 * @return A {@link FieldStream} of all the fields in this class
	 */
	public FieldStream fields() {
		return new FieldStream(Arrays.stream(clazz.getFields()).map(MirrorField::new));
	}

	/**
	 * @return A {@link FieldStream} of all the declared fields in this class
	 */
	public FieldStream declaredFields() {
		return new FieldStream(Arrays.stream(clazz.getDeclaredFields()).map(MirrorField::new));
	}

	/**
	 * Finds the first field with one of the given names
	 * @param names The names of the fields to search for
	 * @return The field
	 */
	public Optional<MirrorField> field(String... names) {
		for (String s : names) {
			try {
				Field f = clazz.getField(s);
				return Optional.of(Mirror.of(f));
			} catch (NoSuchFieldException ignored) {}
		}
		return Optional.empty();
	}

	/**
	 * Finds the first declared field with one of the given names
	 * @param names The names of the fields to search for
	 * @return The field
	 */
	public Optional<MirrorField> declaredField(String... names) {
		for (String s : names) {
			try {
				Field f = clazz.getDeclaredField(s);
				return Optional.of(Mirror.of(f));
			} catch (NoSuchFieldException ignored) {}
		}
		return Optional.empty();
	}

	/**
	 * @return A stream of all methods in the class
	 */
	public MethodStream methods() {
		return new MethodStream(Arrays.stream(clazz.getMethods()).map(MirrorMethod::new));
	}

	/**
	 * @return A stream of all the declared methods in the class
	 */
	public MethodStream declaredMethods() {
		return new MethodStream(Arrays.stream(clazz.getDeclaredMethods()).map(MirrorMethod::new));
	}

	/**
	 * Finds the first method with one of the given names that accepts the given arguments
	 * @param names The names of the method
	 * @param args The arguments the method accepts
	 * @return The method
	 */
	public Optional<MirrorMethod> method(String[] names, Class<?>... args) {
		for (String s : names) {
			try {
				Method m = clazz.getMethod(s, args);
				return Optional.of(Mirror.of(m));
			} catch (NoSuchMethodException ignored) {}
		}
		return Optional.empty();
	}

	/**
	 * Finds the first method with one of the given names that accepts the given arguments
	 * @param name The names of the method
	 * @param args The arguments the method accepts
	 * @return The method
	 */
	public Optional<MirrorMethod> method(String name, Class<?>... args) {
		return method(new String[]{name}, args);
	}

	/**
	 * Finds the first declared method with one of the given names that accepts the given arguments
	 * @param names The names of the method
	 * @param args The arguments the method accepts
	 * @return The method
	 */
	public Optional<MirrorMethod> declaredMethod(String[] names, Class<?>... args) {
		return declaredMethods()
				.filter(m -> Arrays.equals(args, m.parameterTypes()))
				.filter(f -> {
					for (String s : names) {
						if (f.name().equals(s)) {
							return true;
						}
					}
					return false;
				})
				.findFirst();
	}

	/**
	 * Finds the first declared method with one of the given names that accepts the given arguments
	 * @param name The names of the method
	 * @param args The arguments the method accepts
	 * @return The method
	 */
	public Optional<MirrorMethod> declaredMethod(String name, Class<?>... args) {
		return declaredMethod(new String[]{name}, args);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MirrorClass<?> that = (MirrorClass<?>) o;

		return clazz.equals(that.clazz);

	}

	@Override
	public int hashCode() {
		return clazz.hashCode();
	}

}
