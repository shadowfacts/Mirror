package net.shadowfacts.mirror.scanner.cls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * A class scanner that finds all available classes in the given package
 *
 * @author shadowfacts
 *
 * @see net.shadowfacts.mirror.Mirror#ofAllInPackage(String)
 */
public class PackageScanner implements ClassScanner<String> {

	public static final PackageScanner instance = new PackageScanner();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Class<?>> scan(String thePackage) {
		Set<Class<?>> classes = new HashSet<>();

		Enumeration<URL> urls = null;

		try {
			urls = Thread.currentThread().getContextClassLoader().getResources(thePackage.replace('.', '/'));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url == null) {
				throw new IllegalArgumentException("No such package " + thePackage);
			}

			File dir = new File(url.getFile());

			for (File f : dir.listFiles()) {
				addAll(classes, f, thePackage);
			}
		}

		return classes;
	}

	private void addAll(Set<Class<?>> classes, File f, String thePackage) {
		String name = thePackage + "." + f.getName();

		if (f.isDirectory()) {
			for (File dir : f.listFiles()) {
				addAll(classes, dir, name);
			}
		} else if (name.endsWith(".class")) {
			try {
				String className = name;
				className = className.substring(0, className.length() - ".class".length());
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
