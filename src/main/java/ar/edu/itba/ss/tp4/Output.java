package ar.edu.itba.ss.tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ar.edu.itba.ss.tp3.core.MassiveParticle;

public class Output {
	private static Output instance = null;
	private static PrintWriter pw = null;
	
	
	private Output(PrintWriter printWriter) {
		pw = printWriter;
	}
	
	
	public static Output getInstace() throws IOException{
		if(instance == null)
			instance = new Output(new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true))));
		return instance;
	}

	public void write(final double time, final List<MassiveParticle> list){
		if(time == 0){
			try{
				
				pw.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		try(PrintWriter out = pw) {
			out.write(String.valueOf(list.size()) + "\n");
			out.write(String.valueOf(time) + "\n");
			
			for(MassiveParticle p: list){
				out.write(p.getX() + " " +  p.getY() + " " + p.getRadius() + p.getVx() + p.getVy() + "\n");
			}
		}
	}
	
}
