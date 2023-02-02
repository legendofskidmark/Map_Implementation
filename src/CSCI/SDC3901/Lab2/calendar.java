package CSCI.SDC3901.Lab2;

import java.util.Scanner;

// Class to print the first three months of any year (after 1800)

public class calendar {

	// Note how many months to print for the calendar.
	
	private static final int NUM_MONTHS = 3;

	 // Compute the day of the week from a year, month, and day.  0 is Sunday,
	 //  1 is Monday, and so on until 6 is Saturday.

	 //  This function, with minor modification, comes from Wikipedia on September 26, 2015
	 //  (  https://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week  )

	public static int dayofweek(int y, int m, int d)     /* 1 <= m <= 12,  y > 1752 (in the U.K.) */
	{
	  int[] t = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};

	  if (m < 3) {
	    y = y - 1;
	  }

	  return (y + y/4 - y/100 + y/400 + t[m-1] + d) % 7;
	}

	public static void main(String[] args) {
		  int year;
		  int month;
		  int day_of_week;
		  int[] month_days = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //todo: Add a dummy placeholder in the array to avoid unnecessary changes
		  int days_to_print = 0;
		  int i;
		  Scanner userInput = new Scanner( System.in );

                  // Get the year from the user.

		  System.out.println ("Please enter a numeric year from 1800 or later.");
		  year = userInput.nextInt();
		  userInput.close();
		  
		  if (year >= 1800) {
		    // Print the calendar for this particular year.

		    for (month = 1; month <= NUM_MONTHS; month++) {
		      day_of_week = dayofweek( year, month, 1 );

		      // Get us to print up to just before the first day of the week in the month

		      for (i = 0; i < day_of_week; i++) {
		        System.out.print ("   ");
		      }

		      // Figure out how many days to print for the month.

		      days_to_print = month_days[ month ];
		      if (month == 2) {
		        // Check for a leap year
		        if ( (year % 4) == 0 ) {
		          if ( ( (year % 400) == 0 ) || ( (year % 100) != 0) ) {
		            // It's a leap year.

		              days_to_print = days_to_print + 1;
		          }
		        }
		      }

		      // Print the rest of the month.

		      for (i = 1; i <= days_to_print; i++) { //todo: change from i=0 to i=1
		        System.out.printf ("%2d ", i);
		        if ( (i + day_of_week - 1) % 7 == 6) {
		          System.out.println ("");
		        }
		      }
		      // Now that the month is printed, go to the start of a new line if
		      // we didn't just end on a Saturday.

		      if ( (i + day_of_week - 1) % 7 != 0) {
		        System.out.println ("");
		      }

		      // Print a blank line between months.

		      System.out.println ("");
		    }
		  } 
	}
}
