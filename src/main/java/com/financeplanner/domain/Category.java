package com.financeplanner.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a category for {@link Transaction transactions}.
 */
@Getter
@NoArgsConstructor
public class Category {

    private int id;
    private String name;
    private IconData icon;

    @Getter
    @NoArgsConstructor
    public static class IconData {

        private int codePoint;
        private String fontFamily;
        private String fontPackage;

    }

}
