package HospitalManagmentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    public Patient(Connection connection , Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addPatient()
    {
        System.out.print("Enter patient name : ");
        String name = scanner.next();
        System.out.print("Enter Patient age :");
        int age = scanner.nextInt();
        System.out.print("Enter patient Gender :");
        String gender = scanner.next();

        try{
            String query = "insert into patients (name,age , gender) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();//it shows how many rows are affected
            if(affectedRows > 0)
            {
                System.out.println("Patient added sucessfully!!");
            }
            else {
                System.out.println("Failed to add patient!!");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void viewPatients(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();//it hold data which is comes from db and set one pinter that poit the data
            System.out.println("Patient : ");
            System.out.println("+------------+----------------------+--------------+-------------------+");
            System.out.println("| Patient Id | Name                 | Age          | Gender            |");
            System.out.println("+------------+----------------------+--------------+-------------------+");

            while (resultSet.next()){
                int id = resultSet.getInt("id");//here column label is db table column heading
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-22s| %-14s|%-19s|\n",id,name,age,gender);
                System.out.println("+------------+----------------------+--------------+-------------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Boolean getPatientById(int id){
        String query = "select * from patients where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return false;
    }
}
