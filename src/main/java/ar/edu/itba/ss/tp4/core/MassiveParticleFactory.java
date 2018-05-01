
	package ar.edu.itba.ss.tp4.core;

	import ar.edu.itba.ss.tp3.core.MassiveParticle;
	import ar.edu.itba.ss.tp4.interfaces.Factory;

	public class MassiveParticleFactory<T extends MassiveParticle>
		implements Factory<MassiveParticle> {

		protected double x;
		protected double y;
		protected double radius;
		protected double vx;
		protected double vy;
		protected double mass;

		@Override
		@SuppressWarnings("unchecked")
		public T create() {
			return (T) new MassiveParticle(x, y, radius, vx, vy, mass);
		}

		public MassiveParticleFactory<T> from(final T particle) {
			this.x = particle.getX();
			this.y = particle.getY();
			this.radius = particle.getRadius();
			this.vx = particle.getVx();
			this.vy = particle.getVy();
			this.mass = particle.getMass();
			return this;
		}

		public MassiveParticleFactory<T> x(final double x) {
			this.x = x;
			return this;
		}

		public MassiveParticleFactory<T> y(final double y) {
			this.y = y;
			return this;
		}

		public MassiveParticleFactory<T> radius(final double radius) {
			this.radius = radius;
			return this;
		}

		public MassiveParticleFactory<T> vx(final double vx) {
			this.vx = vx;
			return this;
		}

		public MassiveParticleFactory<T> vy(final double vy) {
			this.vy = vy;
			return this;
		}

		public MassiveParticleFactory<T> mass(final double mass) {
			this.mass = mass;
			return this;
		}
	}
