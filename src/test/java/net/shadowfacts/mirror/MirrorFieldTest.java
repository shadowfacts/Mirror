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
public class MirrorFieldTest {

	@Test
	public void testName() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().name(), "f");
	}

	@Test
	public void testDeclaringClass() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().declaringClass().unwrap(), Test1.class);
	}

	@Test
	public void testSetAccessible() {
		Optional<MirrorField> optional = Mirror.of(Test3.class).declaredField("f2");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().setAccessible(true).get(new Test3()), "test 2");
	}

	@Test
	public void testGet() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().get(null), "test");
	}

	@Test
	public void testSet() {
		Optional<MirrorField> optional = Mirror.of(Test4.class).field("f");
		assertTrue(optional.isPresent());
		optional.get().set(null, "test 2");
		assertEquals(optional.get().get(null), "test 2");
	}

	@Test
	public void testIsStatic() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isStatic());
	}

	@Test
	public void testIsNotStatic() {
		Optional<MirrorField> optional = Mirror.of(Test3.class).declaredField("f2");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isNotStatic());
	}

	@Test
	public void testIsFinal() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isFinal());
	}

	@Test
	public void testIsNotFinal() {
		Optional<MirrorField> optional = Mirror.of(Test3.class).declaredField("f2");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isNotFinal());
	}

	@Test
	public void testIsPublic() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isPublic());
	}

	@Test
	public void testIsProtected() {
		Optional<MirrorField> optional = Mirror.of(Test2.class).declaredField("f");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isProtected());
	}

	@Test
	public void testIsPrivate() {
		Optional<MirrorField> optional = Mirror.of(Test3.class).declaredField("f2");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().isPrivate());
	}

	@Test
	public void testHasModifier() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().hasModifier(Modifier.STATIC));
	}

	@Test
	public void testHasAnnotation() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertTrue(optional.get().hasAnnotation(Annotation.class));
	}

	@Test
	public void testGetAnnotation() {
		Optional<MirrorField> optional = Mirror.of(Test1.class).field("f");
		assertTrue(optional.isPresent());
		assertEquals(optional.get().getAnnotation(Annotation.class).value(), "test");
	}

	public static class Test1 {
		@Annotation("test")
		public static final String f = "test";
	}

	public static class Test2 {
		protected static String f = "test";
	}

	public static class Test3 {
		private String f2 = "test 2";
	}

	public static class Test4 {
		public static String f = "test";
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value();
	}

}
