package net.shadowfacts.mirror;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class MirrorMethodTest {

	@Test
	public void testName() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().name(), "m");
	}

	@Test
	public void testDeclaringClass() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().declaringClass().unwrap(), Test1.class);
	}

	@Test
	public void testInvoke() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().invoke(null), "test");
	}

	@Test
	public void testSetAccessible() {
		Optional<MirrorMethod> optional = Mirror.of(Test2.class).declaredMethod("m");
		assertTrue(optional.isPresent());
		optional.get().setAccessible(true);
		assertEquals(optional.get().invoke(null), "test");
	}

	@Test
	public void testParameterTypes() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m2", int.class, boolean.class, String.class);
		assertTrue(optional.isPresent());
		MirrorClass<?>[] expected = new MirrorClass<?>[]{Mirror.of(int.class), Mirror.of(boolean.class), Mirror.of(String.class)};
		assertArrayEquals(optional.get().parameterTypes(), expected);
	}

	@Test
	public void testIsStatic() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isStatic());
	}

	@Test
	public void testIsNotStatic() {
		Optional<MirrorMethod> optional = Mirror.of(Test3.class).declaredMethod("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isNotStatic());
	}

	@Test
	public void testIsPublic() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isPublic());
	}

	@Test
	public void testIsProtected() {
		Optional<MirrorMethod> optional = Mirror.of(Test2.class).declaredMethod("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isProtected());
	}

	@Test
	public void testIsPrivate() {
		Optional<MirrorMethod> optional = Mirror.of(Test3.class).declaredMethod("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isPrivate());
	}

	@Test
	public void testIsAbstract() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m3");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isAbstract());
	}

	@Test
	public void testIsNotAbstract() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isNotAbstract());
	}

	@Test
	public void testHasModifier() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().hasModifier(Modifier.STATIC));
	}

	@Test
	public void testHasAnnotation() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().hasAnnotation(Annotation.class));
	}

	@Test
	public void testGetAnnotation() {
		Optional<MirrorMethod> optional = Mirror.of(Test1.class).method("m");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().getAnnotation(Annotation.class).value(), "test");
	}

	public abstract static class Test1 {
		@Annotation("test")
		public static String m() { return "test"; }

		public static void m2(int i, boolean b, String s) {}

		public abstract void m3();
	}

	public static class Test2 {
		protected static String m() { return "test"; }
	}

	public static class Test3 {
		private void m() {}
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value();
	}

}
