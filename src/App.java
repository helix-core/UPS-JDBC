import java.sql.*;
import java.util.*;
class StudentDatabase{
    private static Connection connection=null;
    static Scanner sc=new Scanner(System.in);
    static StudentDatabase studentDatabase=new StudentDatabase();
    public static void main(String[] args){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUrl="jdbc:mysql://localhost:3306/testing";
            String username="root";
            String password="mysqlboss321@";
            connection=DriverManager.getConnection(dbUrl,username,password);
            if(connection!=null){
                System.out.println(connection);
                System.out.println("Connection successful!");
            }
            System.out.println();
            System.out.println("Enter your choice: ");
            int choice=sc.nextInt();
            switch(choice){
                case 1:
                    studentDatabase.insertRecord();
                    break;
                case 2:
                    studentDatabase.selectRecord();
                    break;
                case 3:
                    studentDatabase.selectAllRecords();
                    break;
                case 4:
                    studentDatabase.updateRecords();
                    break;
                case 5:
                    studentDatabase.deleteRecords();
                    break;
                case 6:
                    studentDatabase.transaction();
                    break;
                case 7:
                    studentDatabase.batchProcessing();
                    break;
                default:
                    break;
            }

        }
        catch(Exception e){
            System.out.println("There was a error");
        }
        finally{
            try{
                if(connection!=null && !connection.isClosed()){
                    connection.close();
                    System.out.println("Connection closed!");
                }
            }catch(Exception e){
                System.out.println("Connection does not exist or has been closed");
            }
        }
    }

    private void insertRecord() throws SQLException{
        System.out.println("Inside Insert function");
        System.out.println("Enter name:");
        String name=sc.next();

        System.out.println("Enter percentage:");
        Float percentage=sc.nextFloat();

        System.out.println("Enter address:");
        String address=sc.next();

        //String sql="insert into student(name,percentage,address) values('Ahmed',89,'CIT Chennai, Kundrathur')";
        String sql="insert into student(name,percentage,address) values(?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);

        preparedStatement.setString(1,name);
        preparedStatement.setFloat(2,percentage);
        preparedStatement.setString(3, address);

        int rows=preparedStatement.executeUpdate();
        if(rows>0){
            System.out.println("Record Inserted Successfully!");
        }
    }

    public void selectRecord() throws SQLException{
        System.out.println("Inside select function");
        System.out.println("Enter the roll number of student to fetch:");
        int roll=sc.nextInt();
        String sql="select * from student where rollno="+roll;
        Statement statement=connection.createStatement();
        ResultSet result=statement.executeQuery(sql);
        if(result.next()){
            int rollNumber=result.getInt("rollno");
            String Studname=result.getString("name");
            float perct=result.getFloat("percentage");
            String address=result.getString("address");
            System.out.println("The Name of the student is: "+Studname);
            System.out.println("The Roll Number of the student is: "+rollNumber);
            System.out.println("The Percentage of the student is: "+perct);
            System.out.println("The address of the student is: "+address);
        }
        else{
            System.out.println("No records found");
        }
    }

    public void selectAllRecords() throws SQLException{
        CallableStatement cst = connection.prepareCall("{call get_all()}");
        ResultSet rst = cst.executeQuery();
        while (rst.next()){
            int roll = rst.getInt(2);            
            String name = rst.getString(1);
            float mark = rst.getFloat(3);
            String add = rst.getString(4);
            System.out.println("RollNo:"+roll);
            System.out.println("Name:"+name);
            System.out.println("Percentage:"+mark);
            System.out.println("Address:"+add);
            System.out.println();
        }
    }

    public void updateRecords() throws SQLException{
        System.out.println("Enter roll number to update the record");
        int roll=sc.nextInt();
        String sql="select * from student where rollno="+roll;
        Statement statement=connection.createStatement();
        ResultSet result=statement.executeQuery(sql);
        if(result.next()){
            int rollNumber=result.getInt("rollno");
            String Studname=result.getString("name");
            float perct=result.getFloat("percentage");
            String address=result.getString("address");
            System.out.println("The Name of the student is: "+Studname);
            System.out.println("The Roll Number of the student is: "+rollNumber);
            System.out.println("The Percentage of the student is: "+perct);
            System.out.println("The address of the student is: "+address);

            System.out.println("What to be updated?");
            System.out.println("1.Name");
            System.out.println("2.Percentage");
            System.out.println("3.Address");
            System.out.println("Enter your choice:");
            int choice=sc.nextInt();
            String query="update student set ";
            switch(choice){
                case 1:
                    System.out.println("Enter new name:");
                    String name=sc.next();
                    query+="name="+name;
                    break;
                case 2:
                    System.out.println("Enter new percentage:");
                    Float percent=sc.nextFloat();
                    query+="percentage="+percent;
                    break;
                case 3:
                    System.out.println("Enter new address:");
                    String add=sc.next();
                    query+="address="+add;
                    break;
                default:
                    break;
            }
            query+=" where rollno="+roll;
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            int rows=preparedStatement.executeUpdate();
            if(rows>0){
                System.out.println("Record updated Successfully!");
                String newsql="select * from student where rollno="+roll;
                Statement stat=connection.createStatement();
                ResultSet res=stat.executeQuery(newsql);
                if(res.next()){
                    int rollNum=res.getInt("rollno");
                    String Studn=res.getString("name");
                    float perc=res.getFloat("percentage");
                    String addres=res.getString("address");
                    System.out.println("The Name of the student is: "+Studn);
                    System.out.println("The Roll Number of the student is: "+rollNum);
                    System.out.println("The Percentage of the student is: "+perc);
                    System.out.println("The address of the student is: "+addres);
                    }
            }
        }
    }

    public void deleteRecords() throws SQLException{
        System.out.println("Enter roll number to delete the record");
        int roll=sc.nextInt();
        System.out.println("The record before deleting is:");
        String sql="select * from student where rollno="+roll;
        Statement statement=connection.createStatement();
        ResultSet result=statement.executeQuery(sql);
        if(result.next()){
            int rollNumber=result.getInt("rollno");
            String Studname=result.getString("name");
            float perct=result.getFloat("percentage");
            String address=result.getString("address");
            System.out.println("The Name of the student is: "+Studname);
            System.out.println("The Roll Number of the student is: "+rollNumber);
            System.out.println("The Percentage of the student is: "+perct);
            System.out.println("The address of the student is: "+address);

            String newquery="delete from student where rollno="+roll;
            PreparedStatement preparedStatement=connection.prepareStatement(newquery);
            int rows=preparedStatement.executeUpdate();
            System.out.println("Record deleted Successfully!");
            System.out.println("Do you wish to see current records? Y/N");
            String choice=sc.next().toLowerCase();
            if(choice.equals("y")){
                studentDatabase.selectAllRecords();
                if(rows==0){
                    System.out.println("No records present!");
                }
            }
            else{
                System.out.println("Thank you!");
            }
        }
    }
    public void transaction() throws SQLException{
        connection.setAutoCommit(false);
        String sql1="insert into student (name,percentage,address) values('Mathi',5,'China')";
        String sql2="update student set name='Ahmed' where name='mathi'";
        PreparedStatement preparedStatement=connection.prepareStatement(sql1);
        PreparedStatement preparedStatements=connection.prepareStatement(sql2);
        int rows=preparedStatement.executeUpdate();
        int rows2=preparedStatements.executeUpdate();
        if(rows>0 && rows2>0){
            connection.commit();
        }
        else{
            connection.rollback();
        }
    }
    public void batchProcessing() throws SQLException{
        connection.setAutoCommit(false);

        String sql1="insert into student (name,percentage,address) values ('Giri',88,'Kashmir')";
        String sql2="update student set name='mathi' where name='Ahmed'";

        Statement stat=connection.createStatement();
        stat.addBatch(sql1);
        stat.addBatch(sql2);
        int[] rows=stat.executeBatch();
        for(int i:rows){
            if(i>0){
                continue;
            }else{
                connection.rollback();
            }
        }
        connection.commit();
        System.out.println("Queries executed successfully!");
    }
}