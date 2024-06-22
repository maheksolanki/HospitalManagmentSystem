package HospitalManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagmentSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    //private : only access within the class , static = access through class name , final = value of this variable throughout the program same
    private static final String username = "root";

    private static final String password = "root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5.Exit");

                System.out.print("Enter your choice : ");
                int choice = scanner.nextInt();

                switch (choice){

                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //view doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //book appointment
                        BookAppintment(patient,doctor,connection,scanner);//patient and doctor are object
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Thank you for using Hospital management system!!");
                        return;
                    default:
                        System.out.print("Enter valid choice...");
                        break;
                }
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void BookAppintment(Patient patient , Doctor doctor, Connection connection, Scanner scanner)
    {
        System.out.print("Enter Patient Id : ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id : ");
        int doctorId = scanner.nextInt();

        System.out.print("Enter Appointment Date (YYYY-MM-DD) : ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailibility(doctorId, appointmentDate,connection)){
                String appointmetnQuery = "insert into appointments(patient_id , doctor_id,apointment_date) values(? ,?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmetnQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int resultAffected = preparedStatement.executeUpdate();
                    if(resultAffected > 0)
                    {
                        System.out.println("Appointment book");
                    }else {
                        System.out.println("Failed to Appointment");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not available in this date!!");
            }
        }else
        {
            System.out.println("Either Doctor or Patient don't exist !! ");
        }
    }
    public static boolean checkDoctorAvailibility(int doctorid , String appointmentDate , Connection connection){
        String query = "select count(*) from appointments where doctor_id = ? AND  apointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorid);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
