package net.shadowfacts.mirror.stream;

import net.shadowfacts.mirror.MirrorField;
import net.shadowfacts.mirror.scanner.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A wrapper for a stream of {@link MirrorField}s that provides helper operations
 *
 * @author shadowfacts
 *
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedFields(Stream)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedFields(Collection)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedFields(Field...)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedMethods(net.shadowfacts.mirror.scanner.Scanner, Object)
 */
public class FieldStream implements Stream<MirrorField> {

	private Stream<MirrorField> underlying;

	/**
	 * Creates a new field stream based on the given underlying stream
	 * @param underlying The underlying stream to use for all stream operations
	 */
	public FieldStream(Stream<MirrorField> underlying) {
		this.underlying = underlying;
	}

	/**
	 * Maps this stream of {@link MirrorField}s to a stream of unwrapped {@link Field}s
	 * @return The unwrapped stream
	 */
	public Stream<Field> unwrap() {
		return map(MirrorField::unwrap);
	}

	/**
	 * Filters this stream by if the field is declared in one of the given classes
	 * @param classes The classes to filter by
	 * @return The filtered stream
	 */
	public FieldStream filterDeclaringClass(Class<?>... classes) {
		return filter(f -> {
			Class<?> declaringClass = f.declaringClass().unwrap();
			for (Class<?> c : classes) {
				if (declaringClass == c) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Maps this stream of fields to their values on the given instance
	 * @param instance The instance to retrieve the field values from. Use {@code null} if the field is static
	 * @return The mapped stream
	 */
	public Stream<Object> get(Object instance) {
		return map(f -> f.get(instance));
	}

	/**
	 * Maps this stream of fields to their values on the given instance and converts them to an array
	 * @param instance The instance to retrieve the field alvues from. Use {@code null} if the field is static
	 * @return The array of field values
	 */
	public Object[] getToArray(Object instance) {
		return get(instance).toArray();
	}

	/**
	 * Filter this stream by if the field has the given annotation
	 * @param clazz The annotation class
	 * @return The filtered stream
	 */
	public FieldStream hasAnnotation(Class<? extends Annotation> clazz) {
		return filter(f -> f.hasAnnotation(clazz));
	}

	/**
	 * Maps this stream of fields to the given annotations on the fields
	 * @param clazz The annotation class
	 * @param <A> The annotation type
	 * @return The stream of annotations
	 */
	public <A extends Annotation> Stream<A> getAnnotation(Class<A> clazz) {
		return map(f -> f.getAnnotation(clazz));
	}

	/**
	 * Filters this stream by if the field is static
	 * @return The filtered stream
	 */
	public FieldStream isStatic() {
		return filter(MirrorField::isStatic);
	}

	/**
	 * Filters this stream by if the field is not static
	 * @return The filtered stream
	 */
	public FieldStream isNotStatic() {
		return filter(MirrorField::isNotStatic);
	}

	/**
	 * Filters this stream by if the field is final
	 * @return The filtered stream
	 */
	public FieldStream isFinal() {
		return filter(MirrorField::isFinal);
	}

	/**
	 * Filters this stream by if the field is not final
	 * @return The filtered stream
	 */
	public FieldStream isNotFinal() {
		return filter(MirrorField::isNotFinal);
	}

	/**
	 * Filters this stream by if the field is public
	 * @return The filtered stream
	 */
	public FieldStream isPublic() {
		return filter(MirrorField::isPublic);
	}

	/**
	 * Filters this stream by if the field is protected
	 * @return The filtered stream
	 */
	public FieldStream isProtected() {
		return filter(MirrorField::isProtected);
	}

	/**
	 * Filters this stream by if the field is private
	 * @return The filtered stream
	 */
	public FieldStream isPrivate() {
		return filter(MirrorField::isPrivate);
	}

	/**
	 * Filters this stream by if the field has the given modifier
	 * @param modifier The modifier to filter by
	 * @return The filtered stream
	 */
	public FieldStream hasModifier(int modifier) {
		return filter(f -> f.hasModifier(modifier));
	}

	/**
	 * Sets each field in this stream to be accessible
	 * @param accessible If the field is accessible
	 * @return The stream
	 */
	public FieldStream setAccessible(boolean accessible) {
		return peek(f -> f.setAccessible(accessible));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream filter(Predicate<? super MirrorField> predicate) {
		return new FieldStream(underlying.filter(predicate));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> Stream<R> map(Function<? super MirrorField, ? extends R> mapper) {
		return underlying.map(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntStream mapToInt(ToIntFunction<? super MirrorField> mapper) {
		return underlying.mapToInt(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongStream mapToLong(ToLongFunction<? super MirrorField> mapper) {
		return underlying.mapToLong(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super MirrorField> mapper) {
		return underlying.mapToDouble(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> Stream<R> flatMap(Function<? super MirrorField, ? extends Stream<? extends R>> mapper) {
		return underlying.flatMap(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntStream flatMapToInt(Function<? super MirrorField, ? extends IntStream> mapper) {
		return underlying.flatMapToInt(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongStream flatMapToLong(Function<? super MirrorField, ? extends LongStream> mapper) {
		return underlying.flatMapToLong(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleStream flatMapToDouble(Function<? super MirrorField, ? extends DoubleStream> mapper) {
		return underlying.flatMapToDouble(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream distinct() {
		return new FieldStream(underlying.distinct());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream sorted() {
		return new FieldStream(underlying.sorted());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream sorted(Comparator<? super MirrorField> comparator) {
		return new FieldStream(underlying.sorted(comparator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream peek(Consumer<? super MirrorField> action) {
		return new FieldStream(underlying.peek(action));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream limit(long maxSize) {
		return new FieldStream(underlying.limit(maxSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream skip(long n) {
		return new FieldStream(underlying.skip(n));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Consumer<? super MirrorField> action) {
		underlying.forEach(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEachOrdered(Consumer<? super MirrorField> action) {
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
	public MirrorField reduce(MirrorField identity, BinaryOperator<MirrorField> accumulator) {
		return underlying.reduce(identity, accumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorField> reduce(BinaryOperator<MirrorField> accumulator) {
		return underlying.reduce(accumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super MirrorField, U> accumulator, BinaryOperator<U> combiner) {
		return underlying.reduce(identity, accumulator, combiner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super MirrorField> accumulator, BiConsumer<R, R> combiner) {
		return underlying.collect(supplier, accumulator, combiner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R, A> R collect(Collector<? super MirrorField, A, R> collector) {
		return underlying.collect(collector);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorField> min(Comparator<? super MirrorField> comparator) {
		return underlying.min(comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorField> max(Comparator<? super MirrorField> comparator) {
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
	public boolean anyMatch(Predicate<? super MirrorField> predicate) {
		return underlying.anyMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean allMatch(Predicate<? super MirrorField> predicate) {
		return underlying.allMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean noneMatch(Predicate<? super MirrorField> predicate) {
		return underlying.noneMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorField> findFirst() {
		return underlying.findFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorField> findAny() {
		return underlying.findAny();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<MirrorField> iterator() {
		return underlying.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Spliterator<MirrorField> spliterator() {
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
	public FieldStream sequential() {
		return new FieldStream(underlying.sequential());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream parallel() {
		return new FieldStream(underlying.parallel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream unordered() {
		return new FieldStream(underlying.unordered());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldStream onClose(Runnable closeHandler) {
		underlying.onClose(closeHandler);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		underlying.close();
	}

}
