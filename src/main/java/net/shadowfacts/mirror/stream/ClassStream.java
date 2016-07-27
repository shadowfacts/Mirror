package net.shadowfacts.mirror.stream;

import net.shadowfacts.mirror.MirrorClass;
import net.shadowfacts.mirror.scanner.*;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A wrapper for a stream of {@link MirrorClass}es that provides helper operations
 *
 * @author shadowfacts
 *
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrapped(Stream)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrapped(Class[])
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrapped(Collection)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrapped(net.shadowfacts.mirror.scanner.Scanner, Object)
 */
public class ClassStream implements Stream<MirrorClass<?>> {

	private Stream<MirrorClass<?>> underlying;

	/**
	 * Creates a new class stream based on the given underlying stream
	 * @param underlying The underlying stream to use for all stream operations
	 */
	public ClassStream(Stream<MirrorClass<?>> underlying) {
		this.underlying = underlying;
	}

	/**
	 * Maps this stream of {@link MirrorClass}es to a stream of unwrapped {@link Class}es
	 * @return The unwrapped stream
	 */
	public Stream<Class<?>> unwrap() {
		return map(MirrorClass::unwrap);
	}

	/**
	 * Filters this stream by if the class is an inner class
	 * @return The filtered stream
	 */
	public ClassStream isInner() {
		return filter(MirrorClass::isInner);
	}

	/**
	 * Filters this stream by if the class is not an inner class
	 * @return The filtered stream
	 */
	public ClassStream isNotInner() {
		return filter(MirrorClass::isNotInner);
	}

	/**
	 * Filters this stream by if the class is a sub-class of the given class
	 * @param clazz The class to filter by
	 * @return The filtered stream
	 */
	public ClassStream isSubClassOf(Class<?> clazz) {
		return filter(c -> c.isSubClassOf(clazz));
	}

	/**
	 * Filters this stream by if the class is a sub-class of the given class
	 * @param clazz The class to filter by
	 * @return The filtered stream
	 */
	public ClassStream isSubClassOf(MirrorClass<?> clazz) {
		return isSubClassOf(clazz.unwrap());
	}

	/**
	 * Filters this stream by if the class is a super-class of the given class
	 * @param clazz The class to filter by
	 * @return The filtered stream
	 */
	public ClassStream isSuperClassOf(Class<?> clazz) {
		return filter(c -> c.isSuperClassOf(clazz));
	}

	/**
	 * Filters this stream by if the class is a super-class of the given class
	 * @param clazz The class to filter by
	 * @return The filtered stream
	 */
	public ClassStream isSuperClassOf(MirrorClass<?> clazz) {
		return isSuperClassOf(clazz.unwrap());
	}

	/**
	 * Filters this stream by if the class is an interface
	 * @return The filtered stream
	 */
	public ClassStream isInterface() {
		return filter(MirrorClass::isInterface);
	}

	/**
	 * Filters this stream by if the class is not an interface
	 * @return The filtered stream
	 */
	public ClassStream isNotInterface() {
		return filter(MirrorClass::isNotInterface);
	}

	/**
	 * Maps this stream to each class' super-class
	 * @return The stream of super-classes
	 */
	public ClassStream mapToSuperClass() {
		return new ClassStream(map(MirrorClass::getSuperClass));
	}

	/**
	 * Flat maps this stream to the interfaces implemented by each class
	 * @return The flat-mapped stream
	 */
	public ClassStream flatMapToInterfaces() {
		return new ClassStream(map(MirrorClass::getInterfaces).flatMap(Arrays::stream));
	}

	/**
	 * Filters this stream of classes by if the class has the given annotation
	 * @param clazz The annotation class
	 * @return The filtered stream
	 */
	public ClassStream hasAnnotation(Class<? extends Annotation> clazz) {
		return filter(c -> c.hasAnnotation(clazz));
	}

	/**
	 * Maps this stream of classes to the given annotation on each class, excluding classes that don't have the annotation
	 * @param clazz The annotation class
	 * @param <A> The annotation type
	 * @return The mapped stream of annotations
	 */
	public <A extends Annotation> Stream<A> getAnnotation(Class<A> clazz) {
		return hasAnnotation(clazz)
				.map(c -> c.getAnnotation(clazz));
	}

	/**
	 * Maps this stream of classes to the field with given name
	 * @see MirrorClass#field(String...)
	 * @param names The names of the field to search for
	 * @return The mapped stream of fields
	 */
	public FieldStream mapToField(String... names) {
		return new FieldStream(map(c -> c.field(names)).filter(Optional::isPresent).map(Optional::get));
	}

	/**
	 * Maps this stream of classes to the declared field with the given name
	 * @see MirrorClass#declaredField(String...)
	 * @param names The names of the field to search for
	 * @return The mapped stream of fields
	 */
	public FieldStream mapToDeclaredField(String... names) {
		return new FieldStream(map(c -> c.declaredField(names)).filter(Optional::isPresent).map(Optional::get));
	}

	/**
	 * Flat maps this stream of classes to all the fields in each class
	 * @return The flat-mapped stream
	 */
	public FieldStream flatMapToFields() {
		return new FieldStream(flatMap(MirrorClass::fields));
	}

	/**
	 * Flat maps this stream of classes to all the declared fields in each class
	 * @return The flat-mapped stream
	 */
	public FieldStream flatMapToDeclaredFields() {
		return new FieldStream(flatMap(MirrorClass::declaredFields));
	}

	/**
	 * Maps this stream of classes to the method with the given name and the given arguments
	 * @see MirrorClass#method(String[], Class[])
	 * @param names The names of the methods to search for
	 * @param args The types of the arguments that the method takes
	 * @return The mapped stream of methods
	 */
	public MethodStream mapToMethod(String[] names, Class<?>... args) {
		return new MethodStream(map(c -> c.method(names, args)).filter(Optional::isPresent).map(Optional::get));
	}

	/**
	 * Maps this stream of classes to the method with the given name and the given arguments
	 * @param name The name of the methods
	 * @param args The types of the arguments that the method takes
	 * @return The mapped stream of methods
	 */
	public MethodStream mapToMethod(String name, Class<?>... args) {
		return mapToMethod(new String[]{name}, args);
	}

	/**
	 * Maps this stream of classes to the declared method with the given name and the given arguments
	 * @param names The names of the methods to search for
	 * @param args The types of the arguments that the method takes
	 * @return The mapped stream of methods
	 */
	public MethodStream mapToDeclaredMethod(String[] names, Class<?>... args) {
		return new MethodStream(map(c -> c.declaredMethod(names, args)).filter(Optional::isPresent).map(Optional::get));
	}

	/**
	 * Maps this stream of classes to the declared method with the given name and the given arguments
	 * @param name The name of the methods
	 * @param args The types of the arguments that the method takes
	 * @return The mapped stream of methods
	 */
	public MethodStream mapToDeclaredMethod(String name, Class<?>... args) {
		return mapToDeclaredMethod(new String[]{name}, args);
	}

	/**
	 * Flat maps this stream to the methods of each class
	 * @return The flat-mapped stream
	 */
	public MethodStream flatMapToMethods() {
		return new MethodStream(flatMap(MirrorClass::methods));
	}

	/**
	 * Flat maps this stream to the declared methods of each class
	 * @return The flat-mapped stream
	 */
	public MethodStream flatMapToDeclaredMethods() {
		return new MethodStream(flatMap(MirrorClass::declaredMethods));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream filter(Predicate<? super MirrorClass<?>> predicate) {
		return new ClassStream(underlying.filter(predicate));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> Stream<R> map(Function<? super MirrorClass<?>, ? extends R> mapper) {
		return underlying.map(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntStream mapToInt(ToIntFunction<? super MirrorClass<?>> mapper) {
		return underlying.mapToInt(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongStream mapToLong(ToLongFunction<? super MirrorClass<?>> mapper) {
		return underlying.mapToLong(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super MirrorClass<?>> mapper) {
		return underlying.mapToDouble(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> Stream<R> flatMap(Function<? super MirrorClass<?>, ? extends Stream<? extends R>> mapper) {
		return underlying.flatMap(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntStream flatMapToInt(Function<? super MirrorClass<?>, ? extends IntStream> mapper) {
		return underlying.flatMapToInt(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongStream flatMapToLong(Function<? super MirrorClass<?>, ? extends LongStream> mapper) {
		return underlying.flatMapToLong(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleStream flatMapToDouble(Function<? super MirrorClass<?>, ? extends DoubleStream> mapper) {
		return underlying.flatMapToDouble(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream distinct() {
		return new ClassStream(underlying.distinct());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream sorted() {
		return new ClassStream(underlying.sorted());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream sorted(Comparator<? super MirrorClass<?>> comparator) {
		return new ClassStream(underlying.sorted(comparator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream peek(Consumer<? super MirrorClass<?>> action) {
		return new ClassStream(underlying.peek(action));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream limit(long maxSize) {
		return new ClassStream(underlying.limit(maxSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream skip(long n) {
		return new ClassStream(underlying.skip(n));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Consumer<? super MirrorClass<?>> action) {
		underlying.forEach(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEachOrdered(Consumer<? super MirrorClass<?>> action) {
		underlying.forEachOrdered(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return underlying.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return underlying.toArray(generator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MirrorClass<?> reduce(MirrorClass<?> identity, BinaryOperator<MirrorClass<?>> accumulator) {
		return underlying.reduce(identity, accumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorClass<?>> reduce(BinaryOperator<MirrorClass<?>> accumulator) {
		return underlying.reduce(accumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super MirrorClass<?>, U> accumulator, BinaryOperator<U> combiner) {
		return underlying.reduce(identity, accumulator, combiner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super MirrorClass<?>> accumulator, BiConsumer<R, R> combiner) {
		return underlying.collect(supplier, accumulator, combiner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R, A> R collect(Collector<? super MirrorClass<?>, A, R> collector) {
		return underlying.collect(collector);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorClass<?>> min(Comparator<? super MirrorClass<?>> comparator) {
		return underlying.min(comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorClass<?>> max(Comparator<? super MirrorClass<?>> comparator) {
		return underlying.max(comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count() {
		return underlying.count();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean anyMatch(Predicate<? super MirrorClass<?>> predicate) {
		return underlying.anyMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean allMatch(Predicate<? super MirrorClass<?>> predicate) {
		return underlying.allMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean noneMatch(Predicate<? super MirrorClass<?>> predicate) {
		return underlying.noneMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorClass<?>> findFirst() {
		return underlying.findFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorClass<?>> findAny() {
		return underlying.findAny();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<MirrorClass<?>> iterator() {
		return underlying.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Spliterator<MirrorClass<?>> spliterator() {
		return underlying.spliterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isParallel() {
		return underlying.isParallel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream sequential() {
		return new ClassStream(underlying.sequential());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream parallel() {
		return new ClassStream(underlying.parallel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream unordered() {
		return new ClassStream(underlying.unordered());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassStream onClose(Runnable closeHandler) {
		return new ClassStream(underlying.onClose(closeHandler));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		underlying.close();
	}

}
