package net.shadowfacts.mirror;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class MirrorConstructorTest {

	@org.junit.Test
	public void testParameterTypes() throws ReflectiveOperationException {
		List<Class<?>> types = Mirror.ofAll(Mirror.of(Test.class.getConstructor(String.class)).parameterTypes())
				.unwrap()
				.collect(Collectors.toList());
		assertEquals(types.size(), 1);
		assertEquals(types.get(0), String.class);
	}

	@org.junit.Test
	public void testInvoke() throws ReflectiveOperationException {
		MirrorConstructor<Test> mirror = Mirror.of(Test.class.getConstructor(String.class));
		Test instance = mirror.invoke("Test");
		assertEquals(instance.s, "Test");
	}

	public static class Test {
		private String s;
		public Test(String s) {
			this.s = s;
		}
	}

}
