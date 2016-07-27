package net.shadowfacts.mirror.stream;

import net.shadowfacts.mirror.MirrorMethod;
import net.shadowfacts.mirror.scanner.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A wrapper for a stream of {@link MirrorMethod}s that provides helper operations
 *
 * @author shadowfacts
 *
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedMethods(Stream)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedMethods(Collection)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedMethods(Method...)
 * @see net.shadowfacts.mirror.Mirror#ofAllUnwrappedMethods(net.shadowfacts.mirror.scanner.Scanner, Object)
 */
public class MethodStream implements Stream<MirrorMethod> {

	private Stream<MirrorMethod> underlying;

	/**
	 * Creates a new method stream based on the given underlying stream
	 * @param underlying The underlying stream to use for all stream operations
	 */
	public MethodStream(Stream<MirrorMethod> underlying) {
		this.underlying = underlying;
	}

	/**
	 * Maps this stream of {@link MirrorMethod}s to a stream of unwrapped {@link Method}s
	 * @return The unwrapped stream
	 */
	public Stream<Method> unwrap() {
		return map(MirrorMethod::unwrap);
	}

	/**
	 * Filters this stream by if the method is declared in one of the given classes
	 * @param classes The classes to filter by
	 * @return The filtered stream
	 */
	public MethodStream filterDeclaringClass(Class<?>... classes) {
		return filter(m -> {
			Class<?> declaringClass = m.declaringClass().unwrap();
			for (Class<?> c : classes) {
				if (declaringClass == c) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Maps this stream of methods to the results of their invocations on the given instance with the given arguments
	 * @param instance The instance to invoke the methods on
	 * @param args The arguments to pass to the method
	 * @return The mapped stream
	 */
	public Stream<Object> invoke(Object instance, Object... args) {
		return map(m -> m.invoke(instance, args));
	}

	/**
	 * Maps this stream of methods to the results of their invocations on the given instance with the given arguments
	 * and converts them to an array
	 * @param instance The instance to invoke the methods on
	 * @param args The arguments to pass to the method
	 * @return The array of method results
	 */
	public Object[] invokeToArray(Object instance, Object... args) {
		return invoke(instance, args).toArray();
	}

	/**
	 * Filters this stream by if the method has the given annotation
	 * @param clazz The annotation class
	 * @return The filtered stream
	 */
	public MethodStream hasAnnotation(Class<? extends Annotation> clazz) {
		return filter(m -> m.hasAnnotation(clazz));
	}

	/**
	 * Maps this stream of methods given annotations on the methods
	 * @param clazz The annotation class
	 * @param <A> The annotation type
	 * @return The mapped stream
	 */
	public <A extends Annotation> Stream<A> getAnnotation(Class<A> clazz) {
		return map(m -> m.getAnnotation(clazz));
	}

	/**
	 * Filters this stream by if the method is static
	 * @return The filtered stream
	 */
	public MethodStream isStatic() {
		return filter(MirrorMethod::isStatic);
	}

	/**
	 * Filters this stream by if the method is not static
	 * @return The filtered stream
	 */
	public MethodStream isNotStatic() {
		return filter(MirrorMethod::isNotStatic);
	}

	/**
	 * Filters this stream by if the method is abstract
	 * @return The filtered stream
	 */
	public MethodStream isAbstract() {
		return filter(MirrorMethod::isAbstract);
	}

	/**
	 * Filters this stream by if the method is not abstract
	 * @return The filtered stream
	 */
	public MethodStream isNotAbstract() {
		return filter(MirrorMethod::isNotAbstract);
	}

	/**
	 * Filters this stream by if the method is public
	 * @return The filtered stream
	 */
	public MethodStream isPublic() {
		return filter(MirrorMethod::isPublic);
	}

	/**
	 * Filters this stream by if the method is protected
	 * @return The filtered stream
	 */
	public MethodStream isProtected() {
		return filter(MirrorMethod::isProtected);
	}

	/**
	 * Filters this stream by if the method is private
	 * @return The filtered stream
	 */
	public MethodStream isPrivate() {
		return filter(MirrorMethod::isPrivate);
	}

	/**
	 * Filters this stream by if the method has the given modifier
	 * @param modifier The modifier to filter by
	 * @return The filtered stream
	 */
	public MethodStream hasModifier(int modifier) {
		return filter(f -> f.hasModifier(modifier));
	}

	/**
	 * Sets each method in this stream to be accessible
	 * @param accessible If the method is accessible
	 * @return The stream
	 */
	public MethodStream setAccessible(boolean accessible) {
		return peek(f -> f.setAccessible(accessible));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream filter(Predicate<? super MirrorMethod> predicate) {
		return new MethodStream(underlying.filter(predicate));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> Stream<R> map(Function<? super MirrorMethod, ? extends R> mapper) {
		return underlying.map(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntStream mapToInt(ToIntFunction<? super MirrorMethod> mapper) {
		return underlying.mapToInt(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongStream mapToLong(ToLongFunction<? super MirrorMethod> mapper) {
		return underlying.mapToLong(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super MirrorMethod> mapper) {
		return underlying.mapToDouble(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> Stream<R> flatMap(Function<? super MirrorMethod, ? extends Stream<? extends R>> mapper) {
		return underlying.flatMap(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntStream flatMapToInt(Function<? super MirrorMethod, ? extends IntStream> mapper) {
		return underlying.flatMapToInt(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongStream flatMapToLong(Function<? super MirrorMethod, ? extends LongStream> mapper) {
		return underlying.flatMapToLong(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DoubleStream flatMapToDouble(Function<? super MirrorMethod, ? extends DoubleStream> mapper) {
		return underlying.flatMapToDouble(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream distinct() {
		return new MethodStream(underlying.distinct());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream sorted() {
		return new MethodStream(underlying.sorted());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream sorted(Comparator<? super MirrorMethod> comparator) {
		return new MethodStream(underlying.sorted(comparator));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream peek(Consumer<? super MirrorMethod> action) {
		return new MethodStream(underlying.peek(action));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream limit(long maxSize) {
		return new MethodStream(underlying.limit(maxSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream skip(long n) {
		return new MethodStream(underlying.skip(n));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Consumer<? super MirrorMethod> action) {
		underlying.forEach(action);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEachOrdered(Consumer<? super MirrorMethod> action) {
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
	public MirrorMethod reduce(MirrorMethod identity, BinaryOperator<MirrorMethod> accumulator) {
		return underlying.reduce(identity, accumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorMethod> reduce(BinaryOperator<MirrorMethod> accumulator) {
		return underlying.reduce(accumulator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super MirrorMethod, U> accumulator, BinaryOperator<U> combiner) {
		return underlying.reduce(identity, accumulator, combiner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super MirrorMethod> accumulator, BiConsumer<R, R> combiner) {
		return underlying.collect(supplier, accumulator, combiner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R, A> R collect(Collector<? super MirrorMethod, A, R> collector) {
		return underlying.collect(collector);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorMethod> min(Comparator<? super MirrorMethod> comparator) {
		return underlying.min(comparator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorMethod> max(Comparator<? super MirrorMethod> comparator) {
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
	public boolean anyMatch(Predicate<? super MirrorMethod> predicate) {
		return underlying.anyMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean allMatch(Predicate<? super MirrorMethod> predicate) {
		return underlying.allMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean noneMatch(Predicate<? super MirrorMethod> predicate) {
		return underlying.noneMatch(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorMethod> findFirst() {
		return underlying.findFirst();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MirrorMethod> findAny() {
		return underlying.findAny();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<MirrorMethod> iterator() {
		return underlying.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Spliterator<MirrorMethod> spliterator() {
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
	public MethodStream sequential() {
		return new MethodStream(underlying.sequential());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream parallel() {
		return new MethodStream(underlying.parallel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodStream unordered() {
		return new MethodStream(underlying.unordered());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<MirrorMethod> onClose(Runnable closeHandler) {
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
