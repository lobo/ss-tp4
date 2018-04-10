
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
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
			final int N = state.size();
			final List<Vector> forces = new ArrayList<>(N);
			final List<MassiveParticle> predicted = new ArrayList<>(N);
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = state.get(i);
				final Vector f = force.apply(state, p);
				final Vector r = r(Δt, p, f);
				forces.add(f);
				predicted.add(new MassiveParticle(
						r.getX(), r.getY(), p.getRadius(),
						p.getVx(), p.getVy(), p.getMass()));
			}
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = predicted.get(i);
				final Vector f = forces.get(i);
				final Vector fNew = force.apply(predicted, p);
				final Vector v = v(Δt, p, fNew, f);
				state.set(i, p.bounce(v.getX(), v.getY()));
			}
			return state;
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

		protected Vector r(
				final double Δt,
				final MassiveParticle p,
				final Vector f) {
			return Vector.of(
					p.getX() + Δt * p.getVx() + 0.5 * Δt * Δt * f.getX() / p.getMass(),
					p.getY() + Δt * p.getVy() + 0.5 * Δt * Δt * f.getY() / p.getMass());
		}

		protected Vector v(
				final double Δt,
				final MassiveParticle p,
				final Vector fNew, final Vector f) {
			return Vector.of(
					p.getVx() + 0.5 * Δt * (fNew.getX() + f.getX()) / p.getMass(),
					p.getVy() + 0.5 * Δt * (fNew.getY() + f.getY()) / p.getMass());
		}
	}
