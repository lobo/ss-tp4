
	package ar.edu.itba.ss.tp4.core;

	import java.util.Arrays;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.ParticleSystem;

	public class HarmonicOscillator implements ParticleSystem {

		protected final MassiveParticle particle;

		public HarmonicOscillator(final List<MassiveParticle> particles) {
			if (particles.isEmpty())
				throw new IllegalStateException(
					"La lista de partículas está vacía.");
			this.particle = particles.get(0);
		}

		@Override
		public List<MassiveParticle> bootstrap() {
			return Arrays.asList(particle);
		}
	}
