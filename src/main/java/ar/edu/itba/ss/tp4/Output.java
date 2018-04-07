package ar.edu.itba.ss.tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ar.edu.itba.ss.tp3.core.MassiveParticle;

public class Output {
	private static Output instance = null;
	
	public static Output getInstace(){
		if(instance == null)
			instance = new Output();
		return instance;
	}

	public void write(List<MassiveParticle> list, double time, PrintWriter pw){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)))) {
			out.write(String.valueOf(list.size()) + "\n");
			out.write(String.valueOf(time) + "\n");
			
			for(MassiveParticle p: list){
				out.write(p.getX() + " " +  p.getY() + " " + p.getRadius() + p.getVx() + p.getVy() + "\n");
			}
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
}
