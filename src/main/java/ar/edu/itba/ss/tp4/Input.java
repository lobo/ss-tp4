package ar.edu.itba.ss.tp4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ar.edu.itba.ss.tp3.core.MassiveParticle;

public class Input{
	
	private static List<MassiveParticle> particles = new ArrayList<MassiveParticle>();
	
	public List<MassiveParticle> getParticles() {
		return particles;
	}

	public Input (String dynamicFilePath) {

		try {
			
			File dynamicFile = new File(dynamicFilePath);
			
			try {
				Scanner dynamicRead = new Scanner(dynamicFile);
				
				dynamicRead.next(); // avoid t0 in Dynamic File
			
				while(dynamicRead.hasNext()){
					particles.add(new MassiveParticle(
							Double.parseDouble(dynamicRead.next()), // x
							Double.parseDouble(dynamicRead.next()), // y
							Double.parseDouble(dynamicRead.next()), //radius 
							Double.parseDouble(dynamicRead.next()), // vx
							Double.parseDouble(dynamicRead.next()), // vy
							0) // mass -> for the purpose of parsing, I do not need the mass
					);
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
