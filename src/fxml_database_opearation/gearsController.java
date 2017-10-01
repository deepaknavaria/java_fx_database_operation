/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_database_opearation;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author issei
 */
public class gearsController implements Initializable {

    //define table
    Connection connection;
    Statement statement;
    ResultSet resultset;

    @FXML
    TableView<data> table;
    @FXML
    TableColumn<data, Integer> id;
    @FXML
    TableColumn<data, String> name;
    @FXML
    TableColumn<data, String> address;
    @FXML
    TableColumn<data, String> city;
    @FXML
    TableColumn<data, String> state;
    @FXML
    TableColumn<data, String> zip;
    @FXML
    TextField id_txt, name_txt, address_txt, city_txt, state_txt, zip_txt;
    @FXML
    Button add, delete, refresh;
    @FXML
    Label status;
    data data_obj = new data();
    ObservableList<data> database;

    public void add_details() throws Exception {
        try {

            data_obj.setId(Integer.parseInt(id_txt.getText()));
            data_obj.setName(name_txt.getText());
            data_obj.setAddress(address_txt.getText());
            data_obj.setCity(city_txt.getText());
            data_obj.setState(state_txt.getText());
            data_obj.setZip(Integer.parseInt(zip_txt.getText()));
            int insert = statement.executeUpdate("Insert into customers values(" + Integer.parseInt(id_txt.getText()) + ",'" + name_txt.getText() + "','" + address_txt.getText() + "','" + city_txt.getText() + "','" + state_txt.getText() + "'," + Integer.parseInt(zip_txt.getText()) + ")");
            table.getItems().add(data_obj);
            status.setText("Database Updated : New Value Added");
            id_txt.clear();
            name_txt.clear();
            address_txt.clear();
            city_txt.clear();
            state_txt.clear();
            zip_txt.clear();
        } catch (Exception e) {
            status.setText("Entered data is not valid");
        }
    }

    public void delete_details() throws Exception {
        //create observable list for selected rows and all rows
        try {
            ObservableList<data> row_selected, all_rows;
            all_rows = table.getItems();

            //selection mode for number of rows selection at a time
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            row_selected = table.getSelectionModel().getSelectedItems();
            if (row_selected == null) {
                status.setText("Select At least one row");
            } else {
                int i1 = get_id1();

                int del = statement.executeUpdate("delete from customers where id=" + i1);
                row_selected.forEach(all_rows::remove);
                status.setText("Database Updated : Value Deleted");
            }

        } catch (Exception e) {
            status.setText("Select At least one row");
        }
    }

    public void get_data() throws SQLException {
        database = FXCollections.observableArrayList();
        resultset = statement.executeQuery("select * from customers;");
        int i, z;
        String n, a, c, s;
        while (resultset.next()) {
            i = resultset.getInt("id");
            z = resultset.getInt("zip");
            n = resultset.getString("name");
            a = resultset.getString("address");
            c = resultset.getString("city");
            s = resultset.getString("state");
            database.add(new data(i, n, a, c, s, z));
            table.setItems(database);
        }
        status.setText("Database Loaded");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        city.setCellValueFactory(new PropertyValueFactory<>("City"));
        state.setCellValueFactory(new PropertyValueFactory<>("State"));
        zip.setCellValueFactory(new PropertyValueFactory<>("Zip"));

        try {
            // TODO
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/store", "root", "root");
            statement = (Statement) connection.createStatement();
            System.out.println("Connecting Database........");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(gearsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int get_id1() {
        data dy = table.getItems().get(table.getSelectionModel().getSelectedIndex());
        int y = dy.getId();
        return y;

    }

}
