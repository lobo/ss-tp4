
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.MassiveParticleFactory;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class BeemanIntegrator<T extends MassiveParticle>
		implements Integrator<T> {

		protected final MassiveParticleFactory<T> factory;
		protected final ForceField<T> force;
		protected final List<T> state;
		protected final Vector [] fOld;

		public BeemanIntegrator(final Builder<T> builder) {
			this.factory = builder.factory;
			this.force = builder.force;
			this.state = builder.state;
			this.fOld = new Vector[state.size()];
			Arrays.setAll(fOld, k -> force.apply(state, state.get(k)));
		}

		public static <T extends MassiveParticle> Builder<T> of(final ForceField<T> force) {
			return new Builder<>(force);
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
			final List<T> futures = new ArrayList<>(N);
			final List<T> predicted = new ArrayList<>(N);
			final List<T> target = force.isVelocityDependent()?
					futures : predicted;
			for (int i = 0; i < N; ++i) {
				final T p = state.get(i);
				final Vector f = force.apply(state, p);
				final Vector r = r(Δt, p, f, fOld[i]);
				forces.add(f);
				final T pNew = factory
						.x(r.getX()).y(r.getY()).radius(p.getRadius())
						.vx(p.getVx()).vy(p.getVy()).mass(p.getMass())
						.create();
				predicted.add(pNew);
				if (force.isVelocityDependent()) {
					final Vector vp = vp(Δt, p, f, fOld[i]);
					futures.add(factory.from(pNew)
							.vx(vp.getX()).vy(vp.getY())
							.create());
				}
			}
			for (int i = 0; i < N; ++i) {
				final T p = predicted.get(i);
				final Vector f = forces.get(i);
				final Vector fNew = force.apply(target, target.get(i));
				final Vector v = v(Δt, p, fNew, f, fOld[i]);
				fOld[i] = f;
				state.set(i, factory.from(p)
						.vx(v.getX()).vy(v.getY())
						.create());
			}
			return state;
		}

		public static class Builder<T extends MassiveParticle>
			extends IntegratorBuilder<T, BeemanIntegrator<T>> {

			public Builder(final ForceField<T> force) {
				super(force);
			}

			@Override
			public BeemanIntegrator<T> build() {
				return new BeemanIntegrator<>(this);
			}
		}

		protected Vector r(
				final double Δt,
				final T p,
				final Vector f, final Vector fOld) {
			return Vector.of(
					p.getX() + Δt*p.getVx() + Δt*Δt*(2.0*f.getX()/3.0-fOld.getX()/6.0)/p.getMass(),
					p.getY() + Δt*p.getVy() + Δt*Δt*(2.0*f.getY()/3.0-fOld.getY()/6.0)/p.getMass());
		}

		protected Vector vp(
				final double Δt,
				final T p,
				final Vector f, final Vector fOld) {
			return Vector.of(
					p.getVx() + Δt*(1.5*f.getX()-0.5*fOld.getX())/p.getMass(),
					p.getVy() + Δt*(1.5*f.getY()-0.5*fOld.getY())/p.getMass());
		}

		protected Vector v(
				final double Δt,
				final T p,
				final Vector fNew, final Vector f, final Vector fOld) {
			return Vector.of(
					p.getVx() + Δt*(fNew.getX()/3.0+5.0*f.getX()/6.0-fOld.getX()/6.0)/p.getMass(),
					p.getVy() + Δt*(fNew.getY()/3.0+5.0*f.getY()/6.0-fOld.getY()/6.0)/p.getMass());
		}
	}
