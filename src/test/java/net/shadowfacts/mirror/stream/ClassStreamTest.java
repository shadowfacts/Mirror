package net.shadowfacts.mirror.stream;

import net.shadowfacts.mirror.Mirror;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class ClassStreamTest {

	@Test
	public void testIsInner() {
		List<Class<?>> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, ClassStreamTest.class)
				.isInner()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 2);
		assertTrue(list.contains(Test1.class));
		assertTrue(list.contains(Test2.class));
	}

	@Test
	public void testIsNotInner() {
		List<Class<?>> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, ClassStreamTest.class)
				.isNotInner()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 1);
		assertTrue(list.contains(ClassStreamTest.class));
	}

	@Test
	public void testIsSubClassOf() {
		Object[] array = Mirror.ofAllUnwrapped(Test4.class)
				.isSubClassOf(Mirror.of(Test1.class))
				.toArray();
		assertArrayEquals(array, new Object[]{Mirror.of(Test4.class)});
	}

	@Test
	public void testIsSuperClassOf() {
		Object[] array = Mirror.ofAllUnwrapped(Test1.class)
				.isSuperClassOf(Mirror.of(Test4.class))
				.toArray();
		assertArrayEquals(array, new Object[]{Mirror.of(Test1.class)});
	}

	@Test
	public void testIsInterface() {
		Object[] array = Mirror.ofAllUnwrapped(Test5.class)
				.isInterface()
				.toArray();
		assertArrayEquals(array, new Object[]{Mirror.of(Test5.class)});
	}

	@Test
	public void testMapToSuperClass() {
		Object[] array = Mirror.ofAllUnwrapped(Test1.class)
				.mapToSuperClass()
				.toArray();
		assertArrayEquals(array, new Object[]{Mirror.of(Object.class)});
	}

	@Test
	public void testFlatMapToInterfaces() {
		Object[] array = Mirror.ofAllUnwrapped(Test4.class)
				.flatMapToInterfaces()
				.toArray();
		assertArrayEquals(array, new Object[]{Mirror.of(Test5.class)});
	}

	@Test
	public void testHasAnnotation() {
		List<Class<?>> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.hasAnnotation(Annotation.class)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 1);
		assertEquals(list.get(0), Test1.class);
	}

	@Test
	public void testGetAnnotation() {
		List<Annotation> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.getAnnotation(Annotation.class)
				.collect(Collectors.toList());
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).value(), "test");
	}

	@Test
	public void testMapToField() throws ReflectiveOperationException {
		List<Field> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.mapToField("s")
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 2);
		assertTrue(list.contains(Test1.class.getField("s")));
		assertTrue(list.contains(Test2.class.getField("s")));
	}

	@Test
	public void testMapToDeclaredField() throws ReflectiveOperationException {
		List<Field> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.mapToDeclaredField("s")
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 3);
		assertTrue(list.contains(Test1.class.getField("s")));
		assertTrue(list.contains(Test2.class.getField("s")));
		assertTrue(list.contains(Test3.class.getDeclaredField("s")));
	}

	@Test
	public void testFlatMapToFields() throws ReflectiveOperationException {
		List<Field> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.flatMapToFields()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 2);
		assertTrue(list.contains(Test1.class.getField("s")));
		assertTrue(list.contains(Test2.class.getField("s")));
	}

	@Test
	public void testFlatMapToDeclaredFields() throws ReflectiveOperationException {
		List<Field> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.flatMapToDeclaredFields()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 3);
		assertTrue(list.contains(Test1.class.getField("s")));
		assertTrue(list.contains(Test2.class.getField("s")));
		assertTrue(list.contains(Test3.class.getDeclaredField("s")));
	}

	@Test
	public void testMapToMethod() throws ReflectiveOperationException {
		List<Method> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.mapToMethod("m")
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 2);
		assertTrue(list.contains(Test1.class.getMethod("m")));
		assertTrue(list.contains(Test2.class.getMethod("m")));
	}

	@Test
	public void testMapToDeclaredMethod() throws ReflectiveOperationException {
		List<Method> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.mapToDeclaredMethod("m")
				.filterDeclaringClass(Test1.class, Test2.class, Test3.class)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 3);
		assertTrue(list.contains(Test1.class.getMethod("m")));
		assertTrue(list.contains(Test2.class.getMethod("m")));
		assertTrue(list.contains(Test3.class.getDeclaredMethod("m")));
	}

	@Test
	public void testFlatMapToMethods() throws ReflectiveOperationException {
		List<Method> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.flatMapToMethods()
				.filterDeclaringClass(Test1.class, Test2.class, Test3.class)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 2);
		assertTrue(list.contains(Test1.class.getMethod("m")));
		assertTrue(list.contains(Test2.class.getMethod("m")));
	}

	@Test
	public void testFlatMapToDeclaredMethods() throws ReflectiveOperationException {
		List<Method> list = Mirror.ofAllUnwrapped(Test1.class, Test2.class, Test3.class)
				.flatMapToDeclaredMethods()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 3);
		assertTrue(list.contains(Test1.class.getMethod("m")));
		assertTrue(list.contains(Test2.class.getMethod("m")));
		assertTrue(list.contains(Test3.class.getDeclaredMethod("m")));
	}

	@Annotation("test")
	public static class Test1 {
		public static String s = "test";

		public static void m() {}
	}

	public static class Test2 {
		public static String s = "test";

		public static void m() {}
	}

	public static class Test3 {
		private static String s = "test";

		private static void m() {}
	}

	public static class Test4 extends Test1 implements Test5 {

	}

	public interface Test5 {

	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value();
	}

}
