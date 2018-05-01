
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.ArrayList;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.core.MassiveParticleFactory;
	import ar.edu.itba.ss.tp4.core.Vector;
	import ar.edu.itba.ss.tp4.interfaces.ForceField;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class GearIntegrator<T extends MassiveParticle>
		implements Integrator<T> {

		protected static final double α [][] = {
			{3.0/20.0, 3.0/16.0},		// α0
			{251.0/360.0, 251.0/360.0},	// α1
			{1.0, 1.0},					// α2
			{11.0/18.0, 11.0/18.0},		// α3
			{1.0/6.0, 1.0/6.0},			// α4
			{1.0/60.0, 1.0/60.0},		// α5
		};

		protected final MassiveParticleFactory<T> factory;
		protected final ForceField<T> force;
		protected final List<T> state;
		protected final Vector [][] derivatives;
		protected final double [] Δ;

		public GearIntegrator(final Builder<T> builder) {
			this.factory = builder.factory;
			this.force = builder.force;
			this.state = builder.state;
			this.Δ = getΔ(builder.Δt);
			final int N = builder.state.size();
			this.derivatives = new Vector [N][6];
			for (int i = 0; i < N; ++i) {
				final T p = state.get(i);
				final double mass = p.getMass();
				final Vector r0 = Vector.of(p.getX(), p.getY());
				final Vector r1 = Vector.of(p.getVx(), p.getVy());
				final Vector r2 = force.apply(state, p).dividedBy(mass);
				final Vector r3 = force.derivative1(state, p).dividedBy(mass);
				final Vector r4 = force.derivative2(state, p).dividedBy(mass);
				final Vector r5 = force.derivative3(state, p).dividedBy(mass);
				derivatives[i][0] = r0p(r0, r1, r2, r3, r4, r5);
				derivatives[i][1] = r1p(r1, r2, r3, r4, r5);
				derivatives[i][2] = r2p(r2, r3, r4, r5);
				derivatives[i][3] = r3p(r3, r4, r5);
				derivatives[i][4] = r4p(r4, r5);
				derivatives[i][5] = r5p(r5);
			}
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
			final List<T> predicted = new ArrayList<>(N);
			for (int i = 0; i < N; ++i) {
				final T p = state.get(i);
				predicted.add(factory
						.x(derivatives[i][0].getX()).y(derivatives[i][0].getY()).radius(p.getRadius())
						.vx(derivatives[i][1].getX()).vy(derivatives[i][1].getY()).mass(p.getMass())
						.create());
			}
			for (int i = 0; i < N; ++i) {
				final T p = predicted.get(i);
				final Vector r2p = derivatives[i][2];
				final Vector r2New = force.apply(predicted, p).dividedBy(p.getMass());
				final Vector ΔR2 = r2New.subtract(r2p).multiplyBy(Δ[1]);
				final int factor = force.isVelocityDependent()? 1 : 0;
				final Vector r0 = derivatives[i][0].add(ΔR2.multiplyBy(α[0][factor]));
				final Vector r1 = derivatives[i][1].add(ΔR2.multiplyBy(α[1][factor] / Δ[0]));
				final Vector r2 = derivatives[i][2].add(ΔR2.multiplyBy(α[2][factor] / Δ[1]));
				final Vector r3 = derivatives[i][3].add(ΔR2.multiplyBy(α[3][factor] / Δ[2]));
				final Vector r4 = derivatives[i][4].add(ΔR2.multiplyBy(α[4][factor] / Δ[3]));
				final Vector r5 = derivatives[i][5].add(ΔR2.multiplyBy(α[5][factor] / Δ[4]));
				state.set(i, factory
						.x(r0.getX()).y(r0.getY()).radius(p.getRadius())
						.vx(r1.getX()).vy(r1.getY()).mass(p.getMass())
						.create());
				derivatives[i][0] = r0p(r0, r1, r2, r3, r4, r5);
				derivatives[i][1] = r1p(r1, r2, r3, r4, r5);
				derivatives[i][2] = r2p(r2, r3, r4, r5);
				derivatives[i][3] = r3p(r3, r4, r5);
				derivatives[i][4] = r4p(r4, r5);
				derivatives[i][5] = r5p(r5);
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
				final Vector r0, final Vector r1, final Vector r2,
				final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					r0.getX()+Δ[0]*r1.getX()+Δ[1]*r2.getX()+Δ[2]*r3.getX()+Δ[3]*r4.getX()+Δ[4]*r5.getX(),
					r0.getY()+Δ[0]*r1.getY()+Δ[1]*r2.getY()+Δ[2]*r3.getY()+Δ[3]*r4.getY()+Δ[4]*r5.getY());
		}

		protected Vector r1p(
				final Vector r1, final Vector r2,
				final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					r1.getX() + Δ[0]*r2.getX() + Δ[1]*r3.getX() + Δ[2]*r4.getX() + Δ[3]*r5.getX(),
					r1.getY() + Δ[0]*r2.getY() + Δ[1]*r3.getY() + Δ[2]*r4.getY() + Δ[3]*r5.getY());
		}

		protected Vector r2p(
				final Vector r2, final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					r2.getX() + Δ[0]*r3.getX() + Δ[1]*r4.getX() + Δ[2]*r5.getX(),
					r2.getY() + Δ[0]*r3.getY() + Δ[1]*r4.getY() + Δ[2]*r5.getY());
		}

		protected Vector r3p(
				final Vector r3, final Vector r4, final Vector r5) {
			return Vector.of(
					r3.getX() + Δ[0]*r4.getX() + Δ[1]*r5.getX(),
					r3.getY() + Δ[0]*r4.getY() + Δ[1]*r5.getY());
		}

		protected Vector r4p(final Vector r4, final Vector r5) {
			return Vector.of(
					r4.getX() + Δ[0]*r5.getX(),
					r4.getY() + Δ[0]*r5.getY());
		}

		protected Vector r5p(final Vector r5) {
			return r5;
		}

		public static class Builder<T extends MassiveParticle>
			extends IntegratorBuilder<T, GearIntegrator<T>> {

			protected double Δt;

			public Builder(final ForceField<T> force) {
				super(force);
			}

			@Override
			public GearIntegrator<T> build() {
				return new GearIntegrator<>(this);
			}

			public Builder<T> Δt(final double Δt) {
				this.Δt = Δt;
				return this;
			}
		}
	}
