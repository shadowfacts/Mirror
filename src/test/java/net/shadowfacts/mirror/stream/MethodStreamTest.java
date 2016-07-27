package net.shadowfacts.mirror.stream;

import net.shadowfacts.mirror.Mirror;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author shadowfacts
 */
public class MethodStreamTest {

	@org.junit.Test
	public void testInvoke() {
		List<Object> list = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.invoke(null)
				.collect(Collectors.toList());
		assertTrue(list.contains("Hello"));
		assertTrue(list.contains("World"));
	}

	@org.junit.Test
	public void testInvokeToArray() {
		Object[] array = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.invoke(null)
				.sorted()
				.toArray();
		assertArrayEquals(array, new String[]{"Hello", "World"});
	}

	@org.junit.Test
	public void testHasAnnotation() {
		Optional<Object> optional = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.hasAnnotation(Annotation.class)
				.invoke(null)
				.findFirst();
		assertTrue(optional.isPresent());
		assertEquals(optional.get(), "Hello");
	}

	@org.junit.Test
	public void testGetAnnotation() {
		Optional<Annotation> optional = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.getAnnotation(Annotation.class)
				.findFirst();
		assertTrue(optional.isPresent());
		assertEquals(optional.get().value(), "test");
	}

	@org.junit.Test
	public void testIsStatic() {
		Object[] array = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.isStatic()
				.invoke(null)
				.sorted()
				.toArray();
		assertArrayEquals(array, new String[]{"Hello", "World"});
	}

	@org.junit.Test
	public void testIsNotStatic() {
		Object[] array = Mirror.of(Test2.class)
				.methods()
				.filterDeclaringClass(Test2.class)
				.isNotStatic()
				.invokeToArray(new Test2());
		assertArrayEquals(array, new String[]{"Test 3"});
	}

	@org.junit.Test
	public void testIsPublic() {
		Object[] array = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.isPublic()
				.invoke(null)
				.sorted()
				.toArray();
		assertArrayEquals(array, new String[]{"Hello", "World"});
	}

	@org.junit.Test
	public void testIsProtected() {
		Object[] array = Mirror.of(Test3.class)
				.declaredMethods()
				.filterDeclaringClass(Test3.class)
				.isProtected()
				.toArray();
		assertEquals(array.length, 1);
	}

	@org.junit.Test
	public void testIsPrivate() {
		Object[] array = Mirror.of(Test3.class)
				.declaredMethods()
				.filterDeclaringClass(Test3.class)
				.isPrivate()
				.toArray();
		assertEquals(array.length, 1);
	}

	@org.junit.Test
	public void testHasModifier() {
		Object[] array = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.hasModifier(Modifier.STATIC)
				.toArray();
		assertEquals(array.length, 2);
	}

	@org.junit.Test
	public void testSetAccessible() {
		List<Object> list = Mirror.of(Test3.class)
				.declaredMethods()
				.filterDeclaringClass(Test3.class)
				.setAccessible(true)
				.invoke(null)
				.collect(Collectors.toList());
		assertEquals(list.size(), 2);
		assertTrue(list.contains("Test 4"));
		assertTrue(list.contains("Test 5"));
	}

	@org.junit.Test
	public void testIsAbstract() throws ReflectiveOperationException {
		List<Method> list = Mirror.of(Test4.class)
				.methods()
				.filterDeclaringClass(Test4.class)
				.isAbstract()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 1);
		assertEquals(list.get(0), Test4.class.getMethod("m6"));
	}

	@org.junit.Test
	public void testIsNotAbstract() throws ReflectiveOperationException {
		List<Method> list = Mirror.of(Test4.class)
				.methods()
				.filterDeclaringClass(Test4.class)
				.isNotAbstract()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(list.size(), 1);
		assertEquals(list.get(0), Test4.class.getMethod("m7"));
	}

	public static class Test {
		@Annotation("test")
		public static String m() {
			return "Hello";
		}
		public static String m2() {
			return "World";
		}
	}

	public static class Test2 {
		public String m3() {
			return "Test 3";
		}
	}

	public static class Test3 {
		protected static String m4() {
			return "Test 4";
		}
		private static String m5() {
			return "Test 5";
		}
	}

	public static abstract class Test4 {
		public abstract void m6();
		public void m7() {}
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value();
	}

}
