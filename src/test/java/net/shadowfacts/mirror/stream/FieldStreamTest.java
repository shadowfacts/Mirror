package net.shadowfacts.mirror.stream;

import net.shadowfacts.mirror.Mirror;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class FieldStreamTest {

	@org.junit.Test
	public void testGet() {
		List<Object> list = Mirror.of(Test.class)
				.fields()
				.get(null)
				.collect(Collectors.toList());
		assertTrue(list.contains("Hello"));
		assertTrue(list.contains("World"));
	}

	@org.junit.Test
	public void testGetToArray() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.getToArray(null);
		assertEquals(array[0], "Hello");
		assertEquals(array[1], "World");
	}

	@org.junit.Test
	public void testHasAnnotation() {
		Optional<Object> optional = Mirror.of(Test.class)
				.fields()
				.hasAnnotation(Annotation.class)
				.get(null)
				.findFirst();
		assertTrue(optional.isPresent());
		assertEquals(optional.get(), "Hello");
	}

	@org.junit.Test
	public void testGetAnnotation() {
		Optional<Annotation> optional = Mirror.of(Test.class)
				.fields()
				.getAnnotation(Annotation.class)
				.findFirst();
		assertTrue(optional.isPresent());
		assertEquals(optional.get().value(), "test");
	}

	@org.junit.Test
	public void testIsStatic() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.isStatic()
				.getToArray(null);
		assertEquals(array[0], "Hello");
		assertEquals(array[1], "World");
	}

	@org.junit.Test
	public void testIsNotStatic() {
		Object[] array = Mirror.of(Test2.class)
				.fields()
				.isNotStatic()
				.getToArray(new Test2());
		assertEquals(array[0], "Test 3");
	}

	@org.junit.Test
	public void testIsFinal() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.isFinal()
				.getToArray(null);
		assertEquals(array[0], "Hello");
	}

	@org.junit.Test
	public void testIsNotFinal() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.isNotFinal()
				.getToArray(null);
		assertEquals(array[0], "World");
	}

	@org.junit.Test
	public void testIsPublic() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.isPublic()
				.getToArray(null);
		assertEquals(array[0], "Hello");
		assertEquals(array[1], "World");
	}

	@org.junit.Test
	public void testIsProtected() {
		Object[] array = Mirror.of(Test3.class)
				.declaredFields()
				.isProtected()
				.toArray();
		assertEquals(array.length, 1);
	}

	@org.junit.Test
	public void testIsPrivate() {
		Object[] array = Mirror.of(Test3.class)
				.declaredFields()
				.isPrivate()
				.toArray();
		assertEquals(array.length, 1);
	}

	@org.junit.Test
	public void testHasModifier() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.hasModifier(Modifier.STATIC)
				.toArray();
		assertEquals(array.length, 2);
	}

	@org.junit.Test
	public void testSetAccessible() {
		Object[] array = Mirror.of(Test3.class)
				.declaredFields()
				.setAccessible(true)
				.getToArray(null);
		assertArrayEquals(array, new Object[]{"Test 4", "Test 5"});
	}

	@org.junit.Test
	public void testFilterDeclaringClass() {
		Object[] array = Mirror.of(Test.class)
				.fields()
				.filterDeclaringClass(Test.class)
				.getToArray(null);
		assertArrayEquals(array, new Object[]{"Hello", "World"});
	}

	public static class Test {
		@Annotation("test")
		public static final String s = "Hello";
		public static String s2 = "World";
	}

	public static class Test2 {
		public String s3 = "Test 3";
	}

	public static class Test3 {
		protected static String s4 = "Test 4";
		private static String s5 = "Test 5";
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value();
	}

}
