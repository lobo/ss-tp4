
	package ar.edu.itba.ss.tp4.core;

	import java.util.Arrays;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;
	import ar.edu.itba.ss.tp4.interfaces.ParticleSystem;

	public class HarmonicOscillator implements ParticleSystem {

		protected final MassiveParticle particle;
		protected final Integrator integrator;

		public HarmonicOscillator(final Builder builder) {
			if (builder.particles.isEmpty())
				throw new IllegalStateException(
					"La lista de partículas está vacía.");
			this.particle = builder.particles.get(0);
			this.integrator = builder.integrator;
		}

		@Override
		public List<MassiveParticle> bootstrap() {
			final List<MassiveParticle> particles = Arrays.asList(particle);
			integrator.setup(particles);
			return particles;
		}

		@Override
		public List<MassiveParticle> evolve(final double Δt) {
			// Completar...
			// return integrator.integrate(Δt);
			return Arrays.asList(particle);
		}

		@Override
		public double force(final double Δt) {
			// No depende de Δt!
			return -10000.0 * particle.getX() - 100.0 * particle.getVx();
		}

		public static Builder of(final List<MassiveParticle> particles) {
			return new Builder(particles);
		}

		public static class Builder {

			protected final List<MassiveParticle> particles;
			protected Integrator integrator;

			public Builder(final List<MassiveParticle> particles) {
				this.particles = particles;
			}

			public HarmonicOscillator build() {
				return new HarmonicOscillator(this);
			}

			public Builder with(final Integrator integrator) {
				this.integrator = integrator;
				return this;
			}
		}
	}
