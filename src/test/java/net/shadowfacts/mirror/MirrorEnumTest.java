package net.shadowfacts.mirror;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author shadowfacts
 */
public class MirrorEnumTest {

	@org.junit.Test
	public void testValues() {
		MirrorEnum<Test> mirror = Mirror.ofEnum(Test.class);
		assertArrayEquals(mirror.values(), Test.values());
	}

	@org.junit.Test
	public void testNext() {
		MirrorEnum<Test> mirror = Mirror.ofEnum(Test.class);
		assertEquals(mirror.next(Test.VALUE1), Test.VALUE2);
		assertEquals(mirror.next(Test.VALUE2), Test.VALUE1);
	}

	@org.junit.Test
	public void testPrevious() {
		MirrorEnum<Test> mirror = Mirror.ofEnum(Test.class);
		assertEquals(mirror.previous(Test.VALUE1), Test.VALUE2);
		assertEquals(mirror.previous(Test.VALUE2), Test.VALUE1);
	}

	public enum Test {
		VALUE1,
		VALUE2;
	}

}
