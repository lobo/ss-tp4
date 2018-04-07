
	package ar.edu.itba.ss.tp4.core;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.ParticleSystem;

	public class SolarSystem implements ParticleSystem {

		protected final List<MassiveParticle> particles;

		public SolarSystem(final List<MassiveParticle> particles) {
			this.particles = particles;
		}

		@Override
		public List<MassiveParticle> bootstrap() {
			// Completar...
			return null;
		}

		@Override
		public List<MassiveParticle> evolve(double Δt) {
			// Completar...
			return null;
		}

		@Override
		public double force(final double Δt) {
			// Completar...
			return 0;
		}
	}
