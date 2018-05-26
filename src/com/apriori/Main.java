package com.apriori;

import java.util.Scanner;
import com.improved.apriori.ImprovedApriori;
import com.standard.apriori.StandardApriori;

public class Main {

	static Scanner in = new Scanner(System.in);

	static String filename = "";
	static float minSupportCount;
	static float minConfidence;
	static int version;

	public static void main(String ar[]) {

		try {

			// Get User Input
			getInputFromUser();

			// Based on input parameter redirect to appropriate code
			if (version == 1) {
				// For Standard
				StandardApriori standardApriori = new StandardApriori(filename, minSupportCount, minConfidence);
				standardApriori.initiateStandardApriori();
			} else {
				// For Improved
				ImprovedApriori improvedApriori = new ImprovedApriori(filename, minSupportCount, minConfidence);
				improvedApriori.initiateImprovedApriori();
			}

		} catch (Exception e) {
			System.exit(0);
		}

	}

	/**
	 * No Param
	 */
	// Reads the data from users input
	static void getInputFromUser() {

		System.out.println("Demonstration of Standard Apriori Algorithm and Improved Apriori Algorithm!");
		System.out.println("Select any of the following (By Number):");
		System.out.println("1. Standard Apriori Algorithm\n2. Improved Apriori Algorithm");

		try {

			version = in.nextInt();

			if(version < 1 || version > 2){
				System.out.println("Invalid Input. Retry Again !");
				System.exit(0);
			}

			in.nextLine();

			System.out.print("Please Enter the appropriate name of dataset:");
			filename = in.nextLine();

			do {
				System.out.print("Please Enter the Minimum Support Rate between 0 and 1:");
				minSupportCount = in.nextFloat();
			} while (minSupportCount <= 0.0 || minSupportCount >= 1.0);

			do {
				System.out.print("Please Enter the Minimum Confidence Rate between 0 and 1:");
				minConfidence = in.nextFloat();
			} while (minConfidence <= 0.0 || minConfidence > 1.0);

		} catch (Exception e) {
			System.out.println("Invalid input. Retry again !");
			System.exit(0);
		}
	}
}
