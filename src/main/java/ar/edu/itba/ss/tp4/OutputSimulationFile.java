package ar.edu.itba.ss.tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ar.edu.itba.ss.tp3.core.MassiveParticle;

public class OutputSimulationFile {

	private static OutputSimulationFile instance = null;
	private static PrintWriter pw = null;

	private OutputSimulationFile(PrintWriter printWriter) {
		pw = printWriter;
	}

	public static OutputSimulationFile getInstace(final String output) throws IOException {
		if(instance == null)
			instance = new OutputSimulationFile(new PrintWriter(new BufferedWriter(new FileWriter(output, false))));
		return instance;
	}

	public void write(final double time, final List<MassiveParticle> list) {
		for (final MassiveParticle p: list) {
			pw.write(p.getX() + " " +  p.getY() + " " +
					p.getRadius() + " " + p.getVx() + " " + p.getVy() + "\n");
		}
	}

	public void close() {
		pw.close();
	}
}
