package com.example.di.level4;

import com.example.di.level4.wiring.CompositionRoot;

/** The only job of main: ask the composition root for the application, and start it. PROVIDED CODE - do not change. */
public class Main {

    public static void main(String[] args) {
        CompositionRoot.build(System.out).run("Write docs", "Fix bug");
    }
}
