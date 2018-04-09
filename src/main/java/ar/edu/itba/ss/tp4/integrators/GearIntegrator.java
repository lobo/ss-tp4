
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class GearIntegrator implements Integrator<MassiveParticle> {

		protected final ForceField<MassiveParticle> force;
		protected final List<MassiveParticle> state;

		public GearIntegrator(final Builder builder) {
			this.force = builder.force;
			this.state = builder.state;
		}

		public static Builder of(final ForceField<MassiveParticle> force) {
			return new Builder(force);
		}

		@Override
		public List<MassiveParticle> getState() {
			return state;
		}

		@Override
		public List<MassiveParticle> integrate(final double Δt) {
			return null;
		}

		public static class Builder
			extends IntegratorBuilder<GearIntegrator> {

			public Builder(final ForceField<MassiveParticle> force) {
				super(force);
			}

			@Override
			public GearIntegrator build() {
				return new GearIntegrator(this);
			}
		}
	}
