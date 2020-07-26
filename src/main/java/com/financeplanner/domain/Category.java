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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class IconData {

        private int codePoint;
        private String fontFamily;
        private String fontPackage;

    }

}
