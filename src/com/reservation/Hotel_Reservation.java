package com.reservation;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Hotel_Reservation {
	
	static Scanner sc=new Scanner(System.in);
	static Connection con=null;
	public static void main(String args[])
	{
		String url="jdbc:mysql://localhost:3306/hotel_db";
		String usr="root";
		String pwd="root";
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,usr,pwd);
			System.out.println("connection successfully establish");
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		
		while(true)
		{
			System.out.println();
			System.out.println("HOTEL MANAGEMENT SYSTEM");
			System.out.println("1. Reserve a Room");
			System.out.println("2. View Reservations");
			System.out.println("3. Get Room Number");
			System.out.println("4. Update Reservation");
			System.out.println("5. Delete Reservation");
			System.out.println("0. Exit");
			System.out.println("Choose an Opiton: ");
			int choice=sc.nextInt();
			switch(choice)
			{	
				
				case 1:
					reservation();
					break;
				case 2:
					viewreservation();
					break;
				case 3:
					getroom();
					break;
				case 4:
					update_reservation();
					break;
				case 5:
					delete_reservation();
					break;
				case 0:
					exit();
					return;
				default:
					System.out.println("Invalid choice. Try again");

			}
			
		}
		
	}
	public static void reservation()
	{
		System.out.println("Enter Reservation ID");
		int id=sc.nextInt();
		System.out.println("Enter Guest_Name");
		String name=sc.next();
		System.out.println("Enter Room Number");
		int room=sc.nextInt();
		System.out.println("Enter Mobile Number");
		long mobile_no=sc.nextLong();
		
		try
		{
			PreparedStatement pst=con.prepareStatement("insert into reservation(reservation_id,guest_name,room_number,mobile_number)values(?,?,?,?)");
			pst.setInt(1, id);
			pst.setString(2,name);
			pst.setInt(3, room);
			pst.setLong(4, mobile_no);
			int affected = pst.executeUpdate();
			System.out.println(affected+" rows affected");
			
		}
		catch(Exception e)
		{
			
		}
		
		
	}
	public static void viewreservation()
	{	
		int t=0;
		try
		{
			Statement st=con.createStatement();
			ResultSet rs = st.executeQuery("select * from reservation");
			while(rs.next())
			{
				int id=rs.getInt("reservation_id");
				String name=rs.getString("guest_name");
				int room=rs.getInt("room_number");
				long mobile=rs.getLong("mobile_number");
				java.sql.Timestamp date = rs.getTimestamp("reservation_date");
				LocalDateTime localDateTime = date.toLocalDateTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = localDateTime.format(formatter);
				if(t==0)
				{
					System.out.println("+----------------+------------+-------------+---------------+---------------------+");
					System.out.println("| reservation_id | guest_name | room_number | mobile_number | reservation_date    |");
					System.out.println("+----------------+------------+-------------+---------------+---------------------+");
					t=1;
				}
				System.out.printf("| %-14d | %-10s | %-11d | %-13s | %-19s |\n", id, name, room, mobile, formattedDateTime);	
				System.out.println("+----------------+------------+-------------+---------------+---------------------+");
				
			}
		}
		catch(Exception e)
		{
			
		}
	}
	public static void getroom()
	{
		try
		{
			Statement st=con.createStatement();
			System.out.println("Enter Reservation_id");
			int r_id=sc.nextInt();
			System.out.println("Enter guest_name");
			String g_name=sc.next();
			boolean found=false;
			ResultSet rs=st.executeQuery("select * from reservation");
			while(rs.next())
			{
				int id=rs.getInt("reservation_id");
				String name=rs.getString("guest_name");
				int room=rs.getInt("room_number");
				if(r_id==id && g_name.equals(name))
				{
					System.out.println(id+" "+name+" Room Number is:"+room);
					found=true;
					break;
				}

			}
			if(!found)
			{
				System.out.println("invalid reservation id");
			}
		}
		catch(Exception e)
		{
			
		}
		
		
	}
	public static boolean reservationExist(int id)
	{	
		try
		{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from reservation");
			boolean f=false;
			while(rs.next())
			{
				int r_id=rs.getInt("reservation_id");
				if(id==r_id)
				{

					return f=true;
				
				}
			}
			if(!f)
			{

				return f=false;
			}
		
			
		}
		catch(Exception e)
		{
			
		}
		return false;
				
	}
	public static void update_reservation()
	{	
		try
		{
			PreparedStatement pst=con.prepareStatement("update reservation set guest_name=(?),room_number=(?),mobile_number=(?) where reservation_id=(?)");

			System.out.println("Enter Reservation ID to Update");
			int id=sc.nextInt();
			if(reservationExist(id))
			{
				System.out.println("Enter guest_name to Update");
				String name=sc.next();
				System.out.println("Enter room_number to Update");
				int room=sc.nextInt();
				System.out.println("Enter mobile_number to Update");
				long mobile=sc.nextLong();
				pst.setString(1, name);
				pst.setInt(2, room);
				pst.setLong(3, mobile);
				pst.setInt(4, id);
				pst.executeUpdate();
			}
			else
			{
				System.out.println("Reservation_id not exist");
			}
		
		}
		catch(Exception e)
		{
			
		}
		
		
	}
	public static void delete_reservation()
	{
		try
		{
			PreparedStatement pst =con.prepareStatement("delete from reservation where reservation_id=(?)");
			System.out.println("Enter Reservation ID to Delete");
			int id=sc.nextInt();
			if(reservationExist(id))
			{
				pst.setInt(1, id);
				pst.executeUpdate();
				System.out.println("Successfully Deleted");
			}
			else
			{
				System.out.println("Not Exist");
			}
			
			
		}
		catch(Exception e)
		{
			
		}
	}
	public static void exit()
	{
		System.out.print("Exiting System");
		for(int i=0;i<5;i++)
		{
			System.out.print(".");
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException e)
			{
				
			}
		}
		System.out.println();
		System.out.println();
		System.out.println("ThankYou For Using Hotel Reservation System!!!");
		

	}
}
