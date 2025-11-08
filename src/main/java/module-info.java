module Oraclib {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires com.dlsc.unitfx;
    requires org.apache.pdfbox;
    requires org.kordamp.ikonli.materialdesign;
    requires fr.brouillard.oss.cssfx;
    requires org.apache.commons.io;

    exports OracLib;
    opens OracLib to javafx.fxml;
}