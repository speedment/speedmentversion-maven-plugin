package com.speedment.speedmentversion.util;

import junit.framework.TestCase;

/**
 *
 * @author Emil Forslund
 */
public class VersionUtilTest extends TestCase {
    
    public void testCompareVersions1() {
        final String first = "1.0.0";
        final String second = "1.0.0";
        assertEquals(0, VersionUtil.compareVersions(first, second));
    }
    
    public void testCompareVersions2() {
        final String first = "2.0.0";
        final String second = "2.0.0";
        assertEquals(0, VersionUtil.compareVersions(first, second));
    }
    
    public void testCompareVersions3() {
        final String first = "2.0.0";
        final String second = "2.0";
        assertEquals(0, VersionUtil.compareVersions(first, second));
    }
    
    public void testCompareVersions4() {
        final String first = "2.0.0";
        final String second = "02.0";
        assertEquals(0, VersionUtil.compareVersions(first, second));
    }
    
    public void testCompareVersions5() {
        final String first = "2";
        final String second = "0000002";
        assertEquals(0, VersionUtil.compareVersions(first, second));
    }
    
    public void testCompareVersions6() {
        final String first  = "1.0.0";
        final String second = "1.0.0-SNAPSHOT";
        assertTrue(VersionUtil.compareVersions(first, second) > 0);
    }
    
    public void testCompareVersions7() {
        final String first  = "1.0.0-SNAPSHOT";
        final String second = "1.0.0";
        assertTrue(VersionUtil.compareVersions(first, second) < 0);
    }

    public void testCompareVersions8() {
        final String first  = "1.0.1-SNAPSHOT";
        final String second = "1.0.0";
        assertTrue(VersionUtil.compareVersions(first, second) > 0);
    }
    
    public void testCompareVersions9() {
        final String first  = "1.0.0";
        final String second = "1.0.1-SNAPSHOT";
        assertTrue(VersionUtil.compareVersions(first, second) < 0);
    }
    
    public void testCompareVersions10() {
        final String first  = "1.0-SNAPSHOT";
        final String second = "1.0.0-SNAPSHOT";
        assertTrue(VersionUtil.compareVersions(first, second) < 0);
    }
    
    public void testCompareVersions11() {
        final String first  = "1.0.0-EA";
        final String second = "1.0.0-SNAPSHOT";
        assertTrue(VersionUtil.compareVersions(first, second) > 0);
    }
    
    public void testCompareVersions12() {
        final String first  = "1.0.0-SNAPSHOT";
        final String second = "1.0.0-EA";
        assertTrue(VersionUtil.compareVersions(first, second) < 0);
    }
    
    public void testCompareVersions13() {
        final String first  = "1.0.0-EA2";
        final String second = "1.0.0-EA";
        assertTrue(VersionUtil.compareVersions(first, second) > 0);
    }
    
    public void testCompareVersions14() {
        final String first  = "1.0.0-EA";
        final String second = "1.0.0-EA2";
        assertTrue(VersionUtil.compareVersions(first, second) < 0);
    }
    
    public void testCompareVersions15() {
        final String first  = "1.2.3";
        final String second = "1.2.4";
        assertTrue(VersionUtil.compareVersions(first, second) < 0);
    }
    
    public void testCompareVersions16() {
        final String first  = "1.3.3";
        final String second = "1.2.4";
        assertTrue(VersionUtil.compareVersions(first, second) > 0);
    }
}
