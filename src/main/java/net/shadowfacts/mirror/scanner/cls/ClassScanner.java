package net.shadowfacts.mirror.scanner.cls;

import net.shadowfacts.mirror.scanner.Scanner;

import java.util.Set;

/**
 * A scanner that returns a {@code Set} of all {@code Class}es available based on the input.
 *
 * @author shadowfacts
 *
 * @see JarScanner
 * @see PackageScanner
 *
 */
public interface ClassScanner<I> extends Scanner<Class<?>, I> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	Set<Class<?>> scan(I input);

}
