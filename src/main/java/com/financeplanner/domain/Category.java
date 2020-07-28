package com.financeplanner.domain;

import lombok.*;

/**
 * Represents a category for {@link Transaction transactions}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private int id;
    private String name;
    private IconData icon;

    /**
     * Represents an icon from the app.
     */
    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class IconData {

        private final int codePoint;
        private final String fontFamily;
        private final String fontPackage;

    }

}
