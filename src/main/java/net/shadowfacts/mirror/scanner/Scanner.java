package net.shadowfacts.mirror.scanner;

import java.util.Set;

/**
 * A scanner that returns a {@code Set} of all {@code T}s available based on the given input.
 * Right now only class scanners are implemented.
 *
 * @author shadowfacts
 *
 * @see net.shadowfacts.mirror.scanner.cls.ClassScanner
 *
 * @param <T> The type of thing to scan for
 * @param <I> The input options that determine the scan parameters
 *
 */
public interface Scanner<T, I> {

	/**
	 * Scans for all applicable {@code T}s based on the given input {@code I}
	 * @param input The input
	 * @return All applicable {@code T}s
	 */
	Set<T> scan(I input);

}
