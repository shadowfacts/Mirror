package net.shadowfacts.mirror;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A mirror method
 *
 * @author shadowfacts
 */
public class MirrorMethod {

	private Method method;

	MirrorMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return The Java {@link Method}
	 */
	public Method unwrap() {
		return method;
	}

	/**
	 * @return The class that declared this method
	 */
	public MirrorClass<?> declaringClass() {
		return Mirror.of(method.getDeclaringClass());
	}

	/**
	 * @return The name of this method
	 */
	public String name() {
		return unwrap().getName();
	}

	/**
	 * Invokes this method on the given object with the given parameters
	 * @see Method#invoke(Object, Object...)
	 * @param instance The instance to invoke on
	 * @param args The arguments to pass to the method
	 * @return The return value of the method
	 */
	public Object invoke(Object instance, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets this method to be accessible
	 * @param accessible
	 * @return This method
	 */
	public MirrorMethod setAccessible(boolean accessible) {
		method.setAccessible(accessible);
		return this;
	}

	/**
	 * @return The types of the parameters that this method accepts
	 */
	public MirrorClass<?>[] parameterTypes() {
		return Mirror.ofAllUnwrapped(method.getParameterTypes()).toArray(MirrorClass<?>[]::new);
	}

	/**
	 * @return The return type of the method
	 */
	public MirrorClass<?> returnType() {
		return Mirror.of(method.getReturnType());
	}

	/**
	 * @return The modifiers of this method
	 */
	public int modifiers() {
		return method.getModifiers();
	}

	/**
	 * @return If this method is static
	 */
	public boolean isStatic() {
		return Modifier.isStatic(modifiers());
	}

	/**
	 * @return If this method is not static
	 */
	public boolean isNotStatic() {
		return !isStatic();
	}

	/**
	 * @return If this method is public
	 */
	public boolean isPublic() {
		return Modifier.isPublic(modifiers());
	}

	/**
	 * @return If this method is protected
	 */
	public boolean isProtected() {
		return Modifier.isProtected(modifiers());
	}

	/**
	 * @return If this method is private
	 */
	public boolean isPrivate() {
		return Modifier.isPrivate(modifiers());
	}

	/**
	 * @return If this method is abstract
	 */
	public boolean isAbstract() {
		return Modifier.isAbstract(modifiers());
	}

	/**
	 * @return If this method is not abstract
	 */
	public boolean isNotAbstract() {
		return !isAbstract();
	}

	/**
	 * Checks if this method has the given modifier
	 * @param modifier The modifier
	 * @return If this method has the modifier
	 */
	public boolean hasModifier(int modifier) {
		return (method.getModifiers() & modifier) != 0;
	}

	/**
	 * Checks if this method has the given annotation
	 * @param clazz The annotation class
	 * @return If this method has the annotation
	 */
	public boolean hasAnnotation(Class<? extends Annotation> clazz) {
		return method.isAnnotationPresent(clazz);
	}

	/**
	 * Retrieves the annotation on the class
	 * @param clazz The annotation class
	 * @param <A> The type of the annotation
	 * @return The annotation
	 */
	public <A extends Annotation> A getAnnotation(Class<A> clazz) {
		return method.getAnnotation(clazz);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MirrorMethod that = (MirrorMethod) o;

		return method.equals(that.method);

	}

	@Override
	public int hashCode() {
		return method.hashCode();
	}

}
