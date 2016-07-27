package net.shadowfacts.mirror;

import net.shadowfacts.mirror.test.PackageTest;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class MirrorTest {

	@org.junit.Test
	public void testOfClass() {
		MirrorClass<Test> mirror = Mirror.of(Test.class);
		assertEquals(mirror.unwrap(), Test.class);
	}

	@org.junit.Test
	public void testOfAllInPackage() {
		List<Class<?>> mirrors = Mirror.ofAllInPackage("net.shadowfacts.mirror.test")
				.filter(MirrorClass::isNotInner)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(mirrors.size(), 1);
		assertEquals(mirrors.get(0), PackageTest.class);
	}

	@org.junit.Test
	public void testOfAllInJar() throws IOException, ClassNotFoundException {
		File file = new File("Hello.jar");
//		create child class loader
		URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
		Class<?> helloClass = Class.forName("Hello", true, classLoader);
//		test
		List<Class<?>> mirrors = Mirror.ofAllInJar(file, classLoader)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(mirrors.size(), 1);
		assertEquals(mirrors.get(0), helloClass);
	}

	@org.junit.Test
	public void testOfEnum() {
		MirrorEnum<Test2> mirror = Mirror.ofEnum(Test2.class);
		assertEquals(mirror.unwrap(), Test2.class);
	}

	@org.junit.Test
	public void testOfConstructor() throws ReflectiveOperationException {
		MirrorConstructor<Test> mirror = Mirror.of(Test.class.getConstructor());
		assertEquals(mirror.unwrap(), Test.class.getConstructor());
	}

	@org.junit.Test
	public void testOfField() throws ReflectiveOperationException {
		MirrorField mirror = Mirror.of(Test.class.getField("f"));
		assertEquals(mirror.unwrap(), Test.class.getField("f"));
	}

	@org.junit.Test
	public void testOfMethod() throws ReflectiveOperationException {
		MirrorMethod mirror = Mirror.of(Test.class.getMethod("m"));
		assertEquals(mirror.unwrap(), Test.class.getMethod("m"));
	}

	public static class Test {
		public int f;

		public void m() {}
	}

	public enum Test2 {
		VALUE1,
		VALUE2;
	}

}
