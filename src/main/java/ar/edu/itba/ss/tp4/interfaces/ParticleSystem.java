
	package ar.edu.itba.ss.tp4.interfaces;

	public interface ParticleSystem {

		public ParticleSystem bootstrap();
		public ParticleSystem evolve(final double Δt);
	}
