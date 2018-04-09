
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class BeemanIntegrator implements Integrator<MassiveParticle> {

		protected final ForceField<MassiveParticle> force;
		protected final List<MassiveParticle> state;
		protected final Vector [] fOld;

		public BeemanIntegrator(final Builder builder) {
			this.force = builder.force;
			this.state = builder.state;
			this.fOld = new Vector[state.size()];
			Arrays.fill(fOld, Vector.of(0.0, 0.0)); // Mejorar...
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
			final List<Vector> forces = new ArrayList<>();
			for (int i = 0; i < state.size(); ++i) {
				final MassiveParticle p = state.get(i);
				final Vector f = force.apply(state, p);
				final Vector r = r(Δt, p, f, fOld[i]);
				final Vector vp = vp(Δt, p, f, fOld[i]);
				forces.add(f);
				state.set(i, new MassiveParticle(
								r.getX(), r.getY(), p.getRadius(),
								vp.getX(), vp.getY(), p.getMass()));
			}
			for (int i = 0; i < state.size(); ++i) {
				final MassiveParticle p = state.get(i);
				final Vector f = forces.get(i);
				final Vector fNew = force.apply(state, p);
				final Vector v = v(Δt, p, fNew, f, fOld[i]);
				fOld[i] = f;
				state.set(i, p.bounce(v.getX(), v.getY()));
			}
			return state;
		}

		public static class Builder
			extends IntegratorBuilder<BeemanIntegrator> {

			public Builder(final ForceField<MassiveParticle> force) {
				super(force);
			}

			@Override
			public BeemanIntegrator build() {
				return new BeemanIntegrator(this);
			}
		}

		protected Vector r(
				final double Δt,
				final MassiveParticle p,
				final Vector f, final Vector fOld) {
			return Vector.of(
					p.getX() + Δt*p.getVx() + Δt*Δt*(2.0*f.getX()/3.0-fOld.getX()/6.0)/p.getMass(),
					p.getY() + Δt*p.getVy() + Δt*Δt*(2.0*f.getY()/3.0-fOld.getY()/6.0)/p.getMass());
		}

		protected Vector vp(
				final double Δt,
				final MassiveParticle p,
				final Vector f, final Vector fOld) {
			return Vector.of(
					p.getVx() + Δt*(1.5*f.getX()-0.5*fOld.getX())/p.getMass(),
					p.getVy() + Δt*(1.5*f.getY()-0.5*fOld.getY())/p.getMass());
		}

		protected Vector v(
				final double Δt,
				final MassiveParticle p,
				final Vector fNew, final Vector f, final Vector fOld) {
			return Vector.of(
					p.getVx() + Δt*(1.0*fNew.getX()/3.0+5.0*f.getX()/6.0-1.0*fOld.getX()/6.0)/p.getMass(),
					p.getVy() + Δt*(1.0*fNew.getY()/3.0+5.0*f.getY()/6.0-1.0*fOld.getY()/6.0)/p.getMass());
		}
	}
