package net.shadowfacts.mirror;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * A mirror field
 *
 * @author shadowfacts
 */
public class MirrorField {

	private Field field;

	MirrorField(Field field) {
		this.field = field;
	}

	/**
	 * @return The name of the field
	 */
	public String name() {
		return field.getName();
	}

	/**
	 * @return The class that declares this field
	 */
	public MirrorClass<?> declaringClass() {
		return Mirror.of(field.getDeclaringClass());
	}

	/**
	 * Sets this field to be accessible
	 * @param accessible
	 * @return This field
	 */
	public MirrorField setAccessible(boolean accessible) {
		field.setAccessible(accessible);
		return this;
	}

	/**
	 * Retrieves the value of this field for the given instance
	 * @see Field#get(Object)
	 * @param instance The instance for which to retrieve the field
	 * @return The value of the field
	 */
	public Object get(Object instance) {
		try {
			return field.get(instance);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets the value of this field on the given instance
	 * @param instance The instance for which to set the field
	 * @param value The new value of the field
	 */
	public void set(Object instance, Object value) {
		try {
			field.set(instance, value);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return The Java {@link Field}
	 */
	public Field unwrap() {
		return field;
	}

	/**
	 * @return The modifiers on this field
	 */
	public int modifiers() {
		return field.getModifiers();
	}

	/**
	 * @return If this field is static
	 */
	public boolean isStatic() {
		return Modifier.isStatic(modifiers());
	}

	/**
	 * @return If this field is not static
	 */
	public boolean isNotStatic() {
		return !isStatic();
	}

	/**
	 * @return If this field is final
	 */
	public boolean isFinal() {
		return Modifier.isFinal(modifiers());
	}

	/**
	 * @return If this field is not final
	 */
	public boolean isNotFinal() {
		return !isFinal();
	}

	/**
	 * @return If this field is public
	 */
	public boolean isPublic() {
		return Modifier.isPublic(modifiers());
	}

	/**
	 * @return If this field is protected
	 */
	public boolean isProtected() {
		return Modifier.isProtected(modifiers());
	}

	/**
	 * @return If this field is private
	 */
	public boolean isPrivate() {
		return Modifier.isPrivate(modifiers());
	}

	/**
	 * @param modifier
	 * @return If this field has the given modifier
	 */
	public boolean hasModifier(int modifier) {
		return (modifiers() & modifier) != 0;
	}

	/**
	 * If this field has the given annotation
	 * @param clazz The annotation class
	 * @return If this field has the annotation
	 */
	public boolean hasAnnotation(Class<? extends Annotation> clazz) {
		return field.isAnnotationPresent(clazz);
	}

	/**
	 * Retrieves the given annotation from this field
	 * @param clazz The annotation class
	 * @param <A> The annotation type
	 * @return The instance of the annotation on this field
	 */
	public <A extends Annotation> A getAnnotation(Class<A> clazz) {
		return field.getAnnotation(clazz);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MirrorField that = (MirrorField) o;

		return field.equals(that.field);

	}

	@Override
	public int hashCode() {
		return field.hashCode();
	}

}
