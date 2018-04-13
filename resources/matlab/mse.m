
	function [] = mse(sources, step)

		x = [];
		for k = 1:size(sources, 2)
			file = fopen(sources{k}{1}, 'r');
			disp(['Reading ', sources{k}{1}, ' ...']);
			xyrvv = importdata(sources{k}{1});
			x(:, k) = xyrvv(:, 1);
		end

		time = (0.0:step:5.0)';
		realX = exp(-10.0 .* time / 14.0) .* cos(time .* sqrt(1000.0/7.0 - 100.0/196.0));

		% Error Cuadrático Medio:
		errors = [];
		for k = 1:size(x, 2)
			errors(1, k) = immse(realX, x(:, k));
		end

		for k = 1:size(x, 2)
			disp([sources{k}{2}, ' (MSE = ', num2str(errors(1, k), '%.16e'), ' [m^2])']);
		end
	end
