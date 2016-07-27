package net.shadowfacts.mirror;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class MirrorClassTest {

	@org.junit.Test
	public void testSimpleName() {
		MirrorClass<Test> mirror = Mirror.of(Test.class);
		assertEquals(mirror.simpleName(), "Test");
	}

	@org.junit.Test
	public void testFullName() {
		MirrorClass<Test> mirror = Mirror.of(Test.class);
		assertEquals(mirror.fullName(), "net.shadowfacts.mirror.MirrorClassTest$Test");
	}

	@org.junit.Test
	public void testConstructor() throws ReflectiveOperationException {
		MirrorConstructor<Test> mirror = Mirror.of(Test.class).constructor();
		assertEquals(mirror.unwrap(), Test.class.getConstructor());
	}

	@org.junit.Test
	public void testIsSubClassOf() {
		MirrorClass<Test> mirror = Mirror.of(Test.class);
		assertTrue(mirror.isSubClassOf(Mirror.of(Object.class)));
		assertFalse(mirror.isSubClassOf(Mirror.of(String.class)));
	}

	@org.junit.Test
	public void testIsSuperClassOf() {
		MirrorClass<Test> mirror = Mirror.of(Test.class);
		assertTrue(mirror.isSuperClassOf(Mirror.of(Test2.class)));
		assertFalse(mirror.isSuperClassOf(Mirror.of(String.class)));
	}

	@org.junit.Test
	public void testIsInterface() {
		assertTrue(Mirror.of(Test.class).isNotInterface());
		assertTrue(Mirror.of(Test3.class).isInterface());
	}

	@org.junit.Test
	public void testGetSuperClass() {
		assertEquals(Mirror.of(Test.class).getSuperClass().unwrap(), Object.class);
		assertEquals(Mirror.of(Test2.class).getSuperClass().unwrap(), Test.class);
	}

	@org.junit.Test
	public void testGetInterfaces() {
		MirrorClass<Test2> mirror = Mirror.of(Test2.class);
		MirrorClass<?>[] interfaces = mirror.getInterfaces();
		assertArrayEquals(interfaces, new MirrorClass<?>[]{Mirror.of(Test3.class)});
	}

	@org.junit.Test
	public void testHasAnnotation() {
		MirrorClass<Test> mirror = Mirror.of(Test.class);
		assertTrue(mirror.hasAnnotation(Annotation.class));
	}

	@org.junit.Test
	public void testGetAnnotation() {
		Annotation a = Mirror.of(Test.class).getAnnotation(Annotation.class);
		assertEquals(a.value(), "test");
	}

	@org.junit.Test
	public void testFields() throws ReflectiveOperationException {
		List<Field> fields = Mirror.of(Test.class)
				.fields()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(fields.size(), 1);
		assertEquals(fields.get(0), Test.class.getField("f1"));
	}

	@org.junit.Test
	public void testDeclaredFields() throws ReflectiveOperationException {
		List<Field> fields = Mirror.of(Test.class)
				.declaredFields()
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(fields.size(), 2);
		assertEquals(fields.get(0), Test.class.getDeclaredField("f1"));
		assertEquals(fields.get(1), Test.class.getDeclaredField("f2"));
	}

	@org.junit.Test
	public void testField() throws ReflectiveOperationException {
		Optional<MirrorField> field = Mirror.of(Test.class)
				.field("f1");
		assertTrue(field.isPresent());
		assertEquals(field.get().unwrap(), Test.class.getField("f1"));
	}

	@org.junit.Test
	public void testDeclaredField() throws ReflectiveOperationException {
		Optional<MirrorField> field = Mirror.of(Test.class).declaredField("f2");
		assertTrue(field.isPresent());
		assertEquals(field.get().unwrap(), Test.class.getDeclaredField("f2"));
	}

	@org.junit.Test
	public void testMethods() throws ReflectiveOperationException {
		List<Method> methods = Mirror.of(Test.class)
				.methods()
				.filterDeclaringClass(Test.class)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(methods.size(), 1);
		assertEquals(methods.get(0), Test.class.getMethod("m1"));
	}

	@org.junit.Test
	public void testDeclaredMethods() throws ReflectiveOperationException {
		List<Method> methods = Mirror.of(Test.class)
				.declaredMethods()
				.filterDeclaringClass(Test.class)
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(methods.size(), 2);
		assertTrue(methods.contains(Test.class.getMethod("m1")));
		assertTrue(methods.contains(Test.class.getDeclaredMethod("m2")));
	}

	@org.junit.Test
	public void testMethod() throws ReflectiveOperationException {
		Optional<MirrorMethod> method = Mirror.of(Test.class).method("m1");
		assertTrue(method.isPresent());
		assertEquals(method.get().unwrap(), Test.class.getMethod("m1"));
	}

	@org.junit.Test
	public void testDeclaredMethod() throws ReflectiveOperationException {
		Optional<MirrorMethod> method = Mirror.of(Test.class).declaredMethod("m2");
		assertTrue(method.isPresent());
		assertEquals(method.get().unwrap(), Test.class.getDeclaredMethod("m2"));
	}


	@Annotation("test")
	public static class Test {
		public int f1;
		private int f2;

		public void m1() {}
		private void m2() {}
	}

	public static class Test2 extends Test implements Test3 {

	}

	public interface Test3 {

	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Annotation {
		String value();
	}

}
