module ir.imorate {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.csv;
    requires org.apache.commons.io;
    requires combinatoricslib3;
    requires static lombok;

    opens ir.imorate to javafx.fxml;
    exports ir.imorate;
}