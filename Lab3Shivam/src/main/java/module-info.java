module org.example.lab3shivam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.mariadb.jdbc;
    requires waffle.jna;

    opens org.example.lab3shivam to javafx.fxml;
    exports org.example.lab3shivam;
}