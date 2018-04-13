
	function [] = mse(sources, step)

		x = [];
		for k = 1:size(sources, 2)
			file = fopen(sources{k}{1}, 'r');
			disp(['Reading ', sources{k}{1}, ' ...']);

			temp = [];
			N = str2num(fgetl(file));
			while true
				time = fgetl(file);
				if time == -1
					break;
				end
				xyrvv = str2num(fgetl(file));
				temp(end + 1, 1) = xyrvv(1, 1);
				fgetl(file);
			end
			fclose(file);
			x(:, k) = temp;
		end

		time = 0.0:step:5.0;
		realX = exp(-10.0 .* time / 14.0) .* cos(time .* sqrt(1000.0/7.0 - 100.0/196.0));
		realX = realX';

		% Error Cuadrático Medio:
		errors = [];
		for k = 1:size(x, 2)
			errors(1, k) = immse(realX, x(:, k));
		end

		for k = 1:size(x, 2)
			disp([sources{k}{2}, ' (MSE = ', num2str(errors(1, k), '%.16e'), ' [m^2])']);
		end
	end
