package ar.edu.itba.ss.tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


import ar.edu.itba.ss.tp3.core.MassiveParticle;

public class OutputAnimatedFile {
	private static OutputAnimatedFile instance = null;
	private static PrintWriter pw = null;

	private OutputAnimatedFile(PrintWriter printWriter) {
		pw = printWriter;
	}
	
	public static OutputAnimatedFile getInstance(final String output) throws IOException {
		if(instance == null)
			instance = new OutputAnimatedFile(new PrintWriter(new BufferedWriter(new FileWriter(output, false))));
		return instance;
	}

	public void write(List<MassiveParticle> list, double time){
		pw.write(String.valueOf(10) + "\n");
		pw.write(String.valueOf(time) + "\n");
		
		for(MassiveParticle p: list){
			pw.write(p.getX() + " " +  p.getY() + " " + p.getRadius() + " " + p.getSpeed() + "\n");
		}
	}	
}

