
	package ar.edu.itba.ss.tp4.interfaces;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;

	public interface Integrator {

		public Integrator setup(final List<MassiveParticle> particles);
		public List<MassiveParticle> integrate(final double Δt);
	}
