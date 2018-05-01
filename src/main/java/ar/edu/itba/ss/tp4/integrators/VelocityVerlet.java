
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.MassiveParticleFactory;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class VelocityVerlet<T extends MassiveParticle>
		implements Integrator<T> {

		protected final ForceField<T> force;
		protected final MassiveParticleFactory<T> factory;
		protected final List<T> state;

		public VelocityVerlet(final Builder<T> builder) {
			this.force = builder.force;
			this.state = builder.state;
			this.factory = builder.factory;
		}

		public static <T extends MassiveParticle> Builder<T> of(final ForceField<T> force) {
			return new Builder<T>(force);
		}

		@Override
		public List<T> getState() {
			return state;
		}

		@Override
		public ForceField<T> getForceField() {
			return force;
		}

		@Override
		public List<T> integrate(final double Δt) {
			final int N = state.size();
			final List<Vector> forces = new ArrayList<>(N);
			final List<T> predicted = new ArrayList<>(N);
			for (int i = 0; i < N; ++i) {
				final T p = state.get(i);
				final Vector f = force.apply(state, p);
				final Vector r = r(Δt, p, f);
				forces.add(f);
				predicted.add(factory.from(p)
						.x(r.getX()).y(r.getY())
						.create());
			}
			for (int i = 0; i < N; ++i) {
				final T p = predicted.get(i);
				final Vector f = forces.get(i);
				final Vector fNew = force.apply(predicted, p);
				final Vector v = v(Δt, p, fNew, f);
				state.set(i, factory.from(p)
						.vx(v.getX()).vy(v.getY())
						.create());
			}
			return state;
		}

		public static class Builder<T extends MassiveParticle>
			extends IntegratorBuilder<T, VelocityVerlet<T>> {

			public Builder(final ForceField<T> force) {
				super(force);
			}

			@Override
			public VelocityVerlet<T> build() {
				return new VelocityVerlet<>(this);
			}
		}

		protected Vector r(
				final double Δt,
				final T p,
				final Vector f) {
			return Vector.of(
					p.getX() + Δt * p.getVx() + 0.5 * Δt * Δt * f.getX() / p.getMass(),
					p.getY() + Δt * p.getVy() + 0.5 * Δt * Δt * f.getY() / p.getMass());
		}

		protected Vector v(
				final double Δt,
				final T p,
				final Vector fNew, final Vector f) {
			return Vector.of(
					p.getVx() + 0.5 * Δt * (fNew.getX() + f.getX()) / p.getMass(),
					p.getVy() + 0.5 * Δt * (fNew.getY() + f.getY()) / p.getMass());
		}
	}
