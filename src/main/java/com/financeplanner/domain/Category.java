package com.financeplanner.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private IconData icon;

    @Embeddable
    @Getter @NoArgsConstructor
    private static class IconData {

        private int codePoint;
        private String fontFamily;
        private String fontPackage;

    }

}
