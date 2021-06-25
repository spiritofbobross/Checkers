module Checkers {
    requires transitive javafx.controls;
    requires org.junit.jupiter.api;
    exports gui;
    exports model;
    exports ptui;
    exports solver;
    exports tests;
}