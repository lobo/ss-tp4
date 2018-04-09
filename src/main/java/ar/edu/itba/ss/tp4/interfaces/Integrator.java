
	package ar.edu.itba.ss.tp4.interfaces;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;

	public interface Integrator<T extends MassiveParticle> {

		public List<T> getState();
		public List<T> integrate(final double Δt);
	}
