package net.shadowfacts.mirror;

import net.shadowfacts.mirror.scanner.Scanner;
import net.shadowfacts.mirror.stream.FieldStream;
import net.shadowfacts.mirror.stream.MethodStream;
import net.shadowfacts.mirror.stream.ClassStream;
import net.shadowfacts.mirror.scanner.cls.JarScanner;
import net.shadowfacts.mirror.scanner.cls.PackageScanner;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Helper methods for creating mirrors and streams of mirrors
 *
 * @author shadowfacts
 */
public class Mirror {

	/**
	 * Creates a mirror of the given class
	 * @param clazz The class
	 * @param <T> The type of the class
	 * @return The mirror
	 */
	public static <T> MirrorClass<T> of(Class<T> clazz) {
		return new MirrorClass<>(clazz);
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given stream of mirror classes
	 * @param classes The stream to use as the underlying stream for the {@link ClassStream}
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAll(Stream<MirrorClass<?>> classes) {
		return new ClassStream(classes);
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given collection of mirror classes
	 * @param classes The collection of classes to create the stream for
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAll(Collection<MirrorClass<?>> classes) {
		return ofAll(classes.stream());
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given array of mirror classes
	 * @param classes The array of classes to create the stream for
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAll(MirrorClass<?>... classes) {
		return ofAll(Arrays.stream(classes));
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given stream of Java {@link Class}es
	 * @param classes The stream of classes to create the stream for
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAllUnwrapped(Stream<Class<?>> classes) {
		return ofAll(classes.map(Mirror::of));
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given collection of Java {@link Class}es
	 * @param classes The collection of classes to create the stream for
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAllUnwrapped(Collection<Class<?>> classes) {
		return ofAllUnwrapped(classes.stream());
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given {@link Class} scanner and scanner input
	 * @param scanner The scanner to use
	 * @param input The input for the scanner
	 * @param <I> The type of scanner input
	 * @return The stream of mmirror classes
	 */
	public static <I> ClassStream ofAllUnwrapped(Scanner<Class<?>, I> scanner, I input) {
		return ofAllUnwrapped(scanner.scan(input));
	}

	/**
	 * Creates a stream of {@link MirrorClass}es from the given array of Java {@link Class}es
	 * @param classes The array of classes to create the stream for
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAllUnwrapped(Class<?>... classes) {
		return ofAllUnwrapped(Arrays.stream(classes));
	}

	/**
	 * Creates a stream of all the {@link MirrorClass}es in the given package
	 * @param thePackage The package to search in
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAllInPackage(String thePackage) {
		return ofAllUnwrapped(PackageScanner.instance, thePackage);
	}

	/**
	 * Creates a stream of all the {@link MirrorClass}es in the given jar to be loaded using the given class loader
	 * @param jar The jar to search in
	 * @param classLoader The class loader to use to load the classes
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAllInJar(File jar, ClassLoader classLoader) {
		return ofAllUnwrapped(JarScanner.instance, new JarScanner.JarScannerOptions(jar, classLoader));
	}

	/**
	 * Creates a stream of all the {@link MirrorClass}es in the given jar using the context class loader
	 * @param jar THe jar to search in
	 * @return The stream of mirror classes
	 */
	public static ClassStream ofAllInJar(File jar) {
		return ofAllInJar(jar, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Creates a mirror of the given {@code enum} class
	 * @param clazz The enum class
	 * @param <E> The type of the enum
	 * @return The mirror of the enum class
	 */
	public static <E extends Enum<E>> MirrorEnum<E> ofEnum(Class<E> clazz) {
		return new MirrorEnum<>(clazz);
	}

	/**
	 * Creates a mirror of the given field
	 * @param f The field
	 * @return The mirror field
	 */
	public static MirrorField of(Field f) {
		return new MirrorField(f);
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given stream of mirror fields
	 * @param fields The stream to use as the underlying stream for the {@link FieldStream}
	 * @return The stream of mirror fields
	 */
	public static FieldStream ofAllFields(Stream<MirrorField> fields) {
		return new FieldStream(fields);
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given collection of mirror fields
	 * @param fields The collection of fields to create the stream from
	 * @return The stream of mirror fields
	 */
	public static FieldStream ofAllFields(Collection<MirrorField> fields) {
		return ofAllFields(fields.stream());
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given array of mirror fields
	 * @param fields The array of fields to create the stream from
	 * @return The stream of mirror fields
	 */
	public static FieldStream ofAllFields(MirrorField... fields) {
		return ofAllFields(Arrays.stream(fields));
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given stream of Java {@link Field}s
	 * @param fields The stream of fields to use
	 * @return The stream of mirror fields
	 */
	public static FieldStream ofAllUnwrappedFields(Stream<Field> fields) {
		return ofAllFields(fields.map(MirrorField::new));
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given collection of Java {@link Field}s
	 * @param fields The collection of fields to create the stream from
	 * @return The stream of mirror fields
	 */
	public static FieldStream ofAllUnwrappedFields(Collection<Field> fields) {
		return ofAllUnwrappedFields(fields.stream());
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given {@link Field} scanner and input
	 * @param scanner The scanner to use
	 * @param input The input for the scanner
	 * @param <I> The type of the input
	 * @return The stream of mirror fields
	 */
	public static <I> FieldStream ofAllUnwrappedFields(Scanner<Field, I> scanner, I input) {
		return ofAllUnwrappedFields(scanner.scan(input));
	}

	/**
	 * Creates a stream of {@link MirrorField}s from the given array of Java {@link Field}s
	 * @param fields The array of fields to create the stream from
	 * @return The stream of mirror fields
	 */
	public static FieldStream ofAllUnwrappedFields(Field... fields) {
		return ofAllUnwrappedFields(Arrays.stream(fields));
	}

	/**
	 * Creates a {@link MirrorMethod} from the given Java {@link Method}
	 * @param m The method
	 * @return The mirror method
	 */
	public static MirrorMethod of(Method m) {
		return new MirrorMethod(m);
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given stream of mirror methods
	 * @param methods The stream to use as the underlying stream for the {@link MethodStream}
	 * @return The stream of mirror methods
	 */
	public static MethodStream ofAllMethods(Stream<MirrorMethod> methods) {
		return new MethodStream(methods);
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given collection of mirror methods
	 * @param methods The collection of methods to create the stream from
	 * @return The stream of mirror methods
	 */
	public static MethodStream ofAllMethods(Collection<MirrorMethod> methods) {
		return ofAllMethods(methods.stream());
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given array of mirror methods
	 * @param methods The array of methods to create the stream from
	 * @return The stream of mirror methods
	 */
	public static MethodStream ofAllMethods(MirrorMethod... methods) {
		return ofAllMethods(Arrays.stream(methods));
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given stream of Java {@link Method}s
	 * @param methods The stream to use as the underlying stream for the {@link MethodStream}
	 * @return The stream of mirror methods
	 */
	public static MethodStream ofAllUnwrappedMethods(Stream<Method> methods) {
		return ofAllMethods(methods.map(MirrorMethod::new));
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given collection of Java {@link Method}s
	 * @param methods The collection of methods to create the stream from
	 * @return The stream of mirror methods
	 */
	public static MethodStream ofAllUnwrappedMethods(Collection<Method> methods) {
		return ofAllUnwrappedMethods(methods.stream());
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given {@link Method} scanner and input
	 * @param scanner The scanner to use
	 * @param input The input for the scanner
	 * @param <I> The type of the input
	 * @return The stream of mirror methods
	 */
	public static <I> MethodStream ofAllUnwrappedMethods(Scanner<Method, I> scanner, I input) {
		return ofAllUnwrappedMethods(scanner.scan(input));
	}

	/**
	 * Creates a stream of {@link MirrorMethod}s from the given array of Java {@link Method}s
	 * @param methods The array of methods to create the stream from
	 * @return The stream of mirror methods
	 */
	public static MethodStream ofAllUnwrappedMethods(Method... methods) {
		return ofAllUnwrappedMethods(Arrays.stream(methods));
	}

	/**
	 * Creates a mirror of the given constructor
	 * @param constructor The constructor
	 * @param <T> The type of the constructor
	 * @return The mirror constructor
	 */
	public static <T> MirrorConstructor<T> of(Constructor<T> constructor) {
		return new MirrorConstructor<>(constructor);
	}

}
