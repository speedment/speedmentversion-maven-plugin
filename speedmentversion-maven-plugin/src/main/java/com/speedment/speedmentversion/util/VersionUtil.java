package com.speedment.speedmentversion.util;

import java.util.Arrays;
import static java.util.Comparator.naturalOrder;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class VersionUtil {
    
    private final static int FIRST_IS_GREATER  = 1;
    private final static int SECOND_IS_GREATER = -1;
    private final static int SUBVERSION_COUNT  = 5;
    
    public static int compareVersions(String first, String second) {
        final String[] firstSub  = separateSubversions(first);
        final String[] secondSub = separateSubversions(second);
        
        // Go through the padded subversions. If one of the versions has fewer 
        // subversions than the other, it will be padded with 0:es until it 
        // reached the SUBVERSION_COUNT.
        for (int i = 0; i < SUBVERSION_COUNT; i++) {
            
            final String[] firstWord  = firstSub[i].split("-");
            final String[] secondWord = secondSub[i].split("-");
            final int maxWords = Math.max(firstWord.length, secondWord.length);
            
            // Go through every word in the subversion (a word is something that
            // has been separated with a '-')
            for (int j = 0; j < maxWords; j++) {
                // If the second has more words than the first, it means that
                // the first is greater. For an example:
                // 1.1 > 1.1-SNAPSHOT
                if (j >= firstWord.length) {
                    return FIRST_IS_GREATER;
                    
                // If the first one has more words than the second, it means
                // that the second is greater. For an example:
                // 1.1-SNAPSHOT < 1.1
                } else if (j >= secondWord.length) {
                    return SECOND_IS_GREATER;
                    
                // If the current word is specified in both versions, proceed by
                // comparing the content.
                } else {
                    final boolean isFirstNum  = isNumeric(firstWord[j]);
                    final boolean isSecondNum = isNumeric(secondWord[j]);
                    
                    // If both are numbers, we should convert them into integers 
                    // and then compare them. This makes sure that 01 == 1.
                    if (isFirstNum && isSecondNum) {
                        final int fistNum   = Integer.parseInt(firstWord[j]);
                        final int secondNum = Integer.parseInt(secondWord[j]);
                        final int diff = fistNum - secondNum;
                        
                        if (diff != 0) {
                            return diff;
                        }
                        
                    // If the first version has a number but the second has a 
                    // literal, the first one is greater. 
                    // Example: 1.2 > 1.RELEASE
                    } else if (isFirstNum) {
                        return FIRST_IS_GREATER;
                        
                    // If the second version has a number but the first has a 
                    // literal, the second one is greater. 
                    // Example: 1.2 > 1.RELEASE
                    } else if (isSecondNum) {
                        return SECOND_IS_GREATER;
                        
                    // If both of the words was literals, do a regular string
                    // comparison, except if one of them is SNAPSHOT
                    } else {
                        final boolean firstSnap  = isSnapshot(firstWord[j]);
                        final boolean secondSnap = isSnapshot(secondWord[j]);
                        
                        if (firstSnap && secondSnap) {
                            continue;
                        } else if (firstSnap) {
                            return SECOND_IS_GREATER;
                        } else if (secondSnap) {
                            return FIRST_IS_GREATER;
                        }
                        
                        final int c = Objects.compare(
                            firstWord[j], 
                            secondWord[i], 
                            naturalOrder()
                        );
                        
                        if (c != 0) {
                            return c;
                        }
                    }
                }
            }
        }
        
        return 0;
    }
    
    private final static Pattern NUMERIC = Pattern.compile("[0-9]+");
    private static boolean isNumeric(String str) {
        return NUMERIC.matcher(str).find();
    }
    
    private static boolean isSnapshot(String str) {
        return "SNAPSHOT".equals(str);
    }
    
    private static String[] separateSubversions(String version) {
        final String[] subversion = version.split("\\.");
        
        if (subversion.length < SUBVERSION_COUNT) {
            final String[] newSubversion = 
                Arrays.copyOf(subversion, SUBVERSION_COUNT);
            
            for (int i = subversion.length; i < SUBVERSION_COUNT; i++) {
                newSubversion[i] = "0";
            }
            
            return newSubversion;
        }
        
        return subversion;
    }
    
    private VersionUtil() {}
}