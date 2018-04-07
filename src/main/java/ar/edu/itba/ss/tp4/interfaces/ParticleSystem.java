
	package ar.edu.itba.ss.tp4.interfaces;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;

	public interface ParticleSystem {

		public List<MassiveParticle> bootstrap();
		public List<MassiveParticle> evolve(final double Δt);
		public double force(final double Δt);
	}
