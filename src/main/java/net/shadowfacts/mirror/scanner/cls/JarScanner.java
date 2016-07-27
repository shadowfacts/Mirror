package net.shadowfacts.mirror.scanner.cls;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A class scanner that finds all available classes in a Jar file using the given {@link ClassLoader}
 *
 * @author shadowfacts
 *
 * @see JarScannerOptions
 * @see net.shadowfacts.mirror.Mirror#ofAllInJar(File)
 * @see net.shadowfacts.mirror.Mirror#ofAllInJar(File, ClassLoader)
 *
 */
public class JarScanner implements ClassScanner<JarScanner.JarScannerOptions> {

	/**
	 * The {@code JarScanner} instance used by {@link net.shadowfacts.mirror.Mirror#ofAllInJar(File)} and {@link net.shadowfacts.mirror.Mirror#ofAllInJar(File, ClassLoader)}
	 */
	public static final JarScanner instance = new JarScanner();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Class<?>> scan(JarScannerOptions options) {
		JarFile jar;

		try {
			jar = new JarFile(options.file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Set<Class<?>> classes = new HashSet<>();

		jar.stream()
				.map(JarEntry::getName)
				.filter(name -> name.endsWith(".class"))
				.map(name -> name.substring(0, name.length() - ".class".length()))
				.map(name -> name.replace('/', '.'))
				.map(name -> {
					try {
						return Class.forName(name, true, options.classLoader);
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				})
				.forEach(classes::add);

		return classes;
	}

	/**
	 * Options to configure a {@link JarScanner} scan operation.
	 */
	public static class JarScannerOptions {
		private File file;
		private ClassLoader classLoader;

		/**
		 * @param file The Jar file to scan in
		 * @param classLoader The class loader to load classes with
		 */
		public JarScannerOptions(File file, ClassLoader classLoader) {
			this.file = file;
			this.classLoader = classLoader;
		}
	}

}
