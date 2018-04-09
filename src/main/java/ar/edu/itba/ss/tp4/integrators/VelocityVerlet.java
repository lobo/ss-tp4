
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class VelocityVerlet implements Integrator<MassiveParticle> {

		protected final ForceField<MassiveParticle> force;
		protected final List<MassiveParticle> state;

		public VelocityVerlet(final Builder builder) {
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
			extends IntegratorBuilder<VelocityVerlet> {

			public Builder(final ForceField<MassiveParticle> force) {
				super(force);
			}

			@Override
			public VelocityVerlet build() {
				return new VelocityVerlet(this);
			}
		}
	}
