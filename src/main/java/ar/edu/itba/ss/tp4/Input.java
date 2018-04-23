package ar.edu.itba.ss.tp4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Input{
	
	private PrintWriter out;
	
	public Input (Integer n, Double deltat, Integer fps, String dynamicFilePath) {

		try {
			
			File dynamicFile = new File(dynamicFilePath);
			
			try {
				Scanner dynamicRead = new Scanner(dynamicFile);
				out = new PrintWriter(new BufferedWriter(new FileWriter("data.xyz", true)));
				
				int lineNumber = 0;
				int chunkNumber = 0;
				Double deltatLocal = 0.0;
				
				while(dynamicRead.hasNext()){
					/*
					if (chunkNumber % fps == 0) {
						
						if (lineNumber % n == 0) {
							out.write(n.toString() + "\n");
							deltatLocal += deltat;
							out.write(deltatLocal.toString() + "\n");
						}
						
						out.write(dynamicRead.next() + " "); // x
						out.write(dynamicRead.next() + " "); // y
						out.write(dynamicRead.next() + " "); // radius
						out.write(dynamicRead.next() + " "); // vx
						out.write(dynamicRead.next() + " "); // vy
						out.write("\n");
						
						chunkNumber++;
					}
					
					for (int i = 0; i < 5; i++) {
						dynamicRead.next();
					}
					
					lineNumber++;	
					*/			
				}
				
				dynamicRead.close();			
			} catch (Exception e) {
				System.out.println("Error scanning file");
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			System.out.println("Error opening or finding file");
		}
		
	}
	
	
}
