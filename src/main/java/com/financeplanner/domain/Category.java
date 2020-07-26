package com.financeplanner.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a category for {@link Transaction transactions}.
 */
@Getter
@Setter
@NoArgsConstructor
public class Category {

    private int id;
    private String name;
    private IconData icon;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IconData {

        private int codePoint;
        private String fontFamily;
        private String fontPackage;

    }

}
