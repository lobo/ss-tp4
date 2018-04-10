
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class GearIntegrator implements Integrator<MassiveParticle> {

		protected static final double α [][] = {
			{3.0/20.0, 3.0/16.0},		// α0
			{251.0/360.0, 251.0/360.0}	// α1
		};

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
		public ForceField<MassiveParticle> getForceField() {
			return force;
		}

		@Override
		public List<MassiveParticle> integrate(final double Δt) {
			final int N = state.size();
			final List<Vector> accelerations = new ArrayList<>(N);
			final List<MassiveParticle> predicted = new ArrayList<>(N);
			final double Δ [] = getΔ(Δt);
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = state.get(i);
				final double mass = p.getMass();
				final Vector r2 = force.apply(state, p).dividedBy(mass);
				final Vector r3 = force.derivative1(state, p).dividedBy(mass);
				final Vector r4 = force.derivative2(state, p).dividedBy(mass);
				final Vector r5 = force.derivative3(state, p).dividedBy(mass);
				final Vector r0p = r0p(Δ, p, r2, r3, r4, r5);
				final Vector r1p = r1p(Δ, p, r2, r3, r4, r5);
				final Vector r2p = r2p(Δ, p, r2, r3, r4, r5);
				accelerations.add(r2p);
				predicted.add(new MassiveParticle(
						r0p.getX(), r0p.getY(), p.getRadius(),
						r1p.getX(), r1p.getY(), mass));
			}
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = predicted.get(i);
				final Vector r2p = accelerations.get(i);
				final Vector r2New = force.apply(predicted, p).dividedBy(p.getMass());
				final Vector ΔR2 = r2New.subtract(r2p).multiplyBy(0.5*Δt*Δt);
				final int factor = force.isVelocityDependent()? 1 : 0;
				state.set(i, new MassiveParticle(
					p.getX() + α[0][factor]*ΔR2.getX(), p.getY() + α[0][factor]*ΔR2.getY(),
					p.getRadius(),
					p.getVx() + α[1][factor]*ΔR2.getX()/Δt, p.getVy() + α[1][factor]*ΔR2.getY()/Δt,
					p.getMass()));
			}
			return state;
		}

		protected double [] getΔ(final double Δt) {
			return new double [] {
				Δt,
				0.5*Δt*Δt,
				Δt*Δt*Δt/6.0,
				Δt*Δt*Δt*Δt/24.0,
				Δt*Δt*Δt*Δt*Δt/120.0
			};
		}

		protected Vector r0p(
				final double [] Δ, final MassiveParticle p,
				final Vector r2, final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					p.getX()+Δ[0]*p.getVx()+Δ[1]*r2.getX()+Δ[2]*r3.getX()+Δ[3]*r4.getX()+Δ[4]*r5.getX(),
					p.getY()+Δ[0]*p.getVy()+Δ[1]*r2.getY()+Δ[2]*r3.getY()+Δ[3]*r4.getY()+Δ[4]*r5.getY());
		}

		protected Vector r1p(
				final double [] Δ, final MassiveParticle p,
				final Vector r2, final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					p.getVx() + Δ[0]*r2.getX() + Δ[1]*r3.getX() + Δ[2]*r4.getX() + Δ[3]*r5.getX(),
					p.getVy() + Δ[0]*r2.getY() + Δ[1]*r3.getY() + Δ[2]*r4.getY() + Δ[3]*r5.getY());
		}

		protected Vector r2p(
				final double [] Δ, final MassiveParticle p,
				final Vector r2, final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					r2.getX() + Δ[0]*r3.getX() + Δ[1]*r4.getX() + Δ[2]*r5.getX(),
					r2.getY() + Δ[0]*r3.getY() + Δ[1]*r4.getY() + Δ[2]*r5.getY());
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
