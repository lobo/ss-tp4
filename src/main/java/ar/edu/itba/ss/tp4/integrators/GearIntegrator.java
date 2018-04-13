
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
			{251.0/360.0, 251.0/360.0},	// α1
			{1.0, 1.0},					// α2
			{11.0/18.0, 11.0/18.0},		// α3
			{1.0/6.0, 1.0/6.0},			// α4
			{1.0/60.0, 1.0/60.0},		// α5
		};

		protected final ForceField<MassiveParticle> force;
		protected final List<MassiveParticle> state;
		protected final Vector [][] derivatives;
		protected final double [] Δ;

		public GearIntegrator(final Builder builder) {
			this.force = builder.force;
			this.state = builder.state;
			this.Δ = getΔ(builder.Δt);
			final int N = builder.state.size();
			this.derivatives = new Vector [N][6];
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = state.get(i);
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
			final List<MassiveParticle> predicted = new ArrayList<>(N);
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = state.get(i);
				final Vector r0 = derivatives[i][0];
				final Vector r1 = derivatives[i][1];
				final Vector r2 = derivatives[i][2];
				final Vector r3 = derivatives[i][3];
				final Vector r4 = derivatives[i][4];
				final Vector r5 = derivatives[i][5];
				derivatives[i][0] = r0p(r0, r1, r2, r3, r4, r5);
				derivatives[i][1] = r1p(r1, r2, r3, r4, r5);
				derivatives[i][2] = r2p(r2, r3, r4, r5);
				derivatives[i][3] = r3p(r3, r4, r5);
				derivatives[i][4] = r4p(r4, r5);
				derivatives[i][5] = r5p(r5);
				predicted.add(new MassiveParticle(
						derivatives[i][0].getX(), derivatives[i][0].getY(), p.getRadius(),
						derivatives[i][1].getX(), derivatives[i][1].getY(), p.getMass()));
			}
			for (int i = 0; i < N; ++i) {
				final MassiveParticle p = predicted.get(i);
				final Vector r2p = derivatives[i][2];
				final Vector r2New = force.apply(predicted, p).dividedBy(p.getMass());
				final Vector ΔR2 = r2New.subtract(r2p).multiplyBy(Δ[1]);
				final int factor = force.isVelocityDependent()? 1 : 0;
				derivatives[i][0] = derivatives[i][0].add(ΔR2.multiplyBy(α[0][factor]));
				derivatives[i][1] = derivatives[i][1].add(ΔR2.multiplyBy(α[1][factor] / Δ[0]));
				derivatives[i][2] = derivatives[i][2].add(ΔR2.multiplyBy(α[2][factor] / Δ[1]));
				derivatives[i][3] = derivatives[i][3].add(ΔR2.multiplyBy(α[3][factor] / Δ[2]));
				derivatives[i][4] = derivatives[i][4].add(ΔR2.multiplyBy(α[4][factor] / Δ[3]));
				derivatives[i][5] = derivatives[i][5].add(ΔR2.multiplyBy(α[5][factor] / Δ[4]));
				state.set(i, new MassiveParticle(
					derivatives[i][0].getX(), derivatives[i][0].getY(),
					p.getRadius(),
					derivatives[i][1].getX(), derivatives[i][1].getY(),
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

		public static class Builder
			extends IntegratorBuilder<GearIntegrator> {

			protected double Δt;

			public Builder(final ForceField<MassiveParticle> force) {
				super(force);
			}

			@Override
			public GearIntegrator build() {
				return new GearIntegrator(this);
			}

			public Builder Δt(final double Δt) {
				this.Δt = Δt;
				return this;
			}
		}
	}
