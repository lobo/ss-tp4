
	package ar.edu.itba.ss.tp4.integrators;

	import java.util.Arrays;
	import java.util.List;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.Integrator;

	public class BeemanIntegrator implements Integrator {

		protected List<MassiveParticle> particles;
		protected double [] fxOld;
		protected double [] fyOld;

		public BeemanIntegrator() {
			this.particles = Arrays.asList();
			this.fxOld = new double [0];
			this.fyOld = new double [0];
		}

		@Override
		public Integrator setup(final List<MassiveParticle> particles) {
			this.particles = particles;
			this.fxOld = new double [particles.size()];
			this.fyOld = new double [particles.size()];
			// Inicializar estos vectores!
			return this;
		}

		@Override
		public List<MassiveParticle> integrate(final double Δt) {
			for (int i = 0; i < particles.size(); ++i) {
				final MassiveParticle p = particles.get(i);
				final double fx = 0.0;
				final double fy = 0.0;
				final double rx = p.getX() + Δt * p.getVx() +
						Δt * Δt * (2.0 * fx / 3.0 - fxOld[i] / 6.0) / p.getMass();
				final double ry = p.getY() + Δt * p.getVy() +
						Δt * Δt * (2.0 * fy / 3.0 - fyOld[i] / 6.0) / p.getMass();
				final double vxp = p.getVx() +
						Δt * (1.5 * fx - 0.5 * fxOld[i]) / p.getMass();
				final double vyp = p.getVy() +
						Δt * (1.5 * fy - 0.5 * fyOld[i]) / p.getMass();
				final double fxNew = 0.0; // ??? Depende de vxp
				final double fyNew = 0.0; // ??? Depende de vyp
				final double vx = p.getVx() +
						Δt * (1.0 * fxNew / 3.0 + 5.0 * fx / 6.0 - 1.0 * fxOld[i] / 6.0) / p.getMass();
				final double vy = p.getVy() +
						Δt * (1.0 * fyNew / 3.0 + 5.0 * fy / 6.0 - 1.0 * fyOld[i] / 6.0) / p.getMass();
				fxOld[i] = fx; // Está bien?
				fyOld[i] = fy; // Está bien?
				particles.set(i, new MassiveParticle(
					rx, ry, p.getRadius(), vx, vy, p.getMass()));
			}
			return particles;
		}
	}
