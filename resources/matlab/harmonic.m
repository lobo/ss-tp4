
	%{
		Permite graficar diferentes aproximaciones de un oscilador armónico
		amortiguado, utilizando diferentes métodos de integración. Todos los
		métodos deben poseer el mismo paso temporal (Δt).

		@example Para visualizar 2 métodos de integración, se deben
			proporcionar 2 archivos y sus leyendas:

			harmonic({
				{'../data/harmonic-velocity-verlet.data', 'Velocity Verlet'},
				{'../data/harmonic-beeman.data', 'Beeman'}
			}, 0.001);
	%}

	function [] = harmonic(sources, step)

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

		% Begin plotting...

		display = figure();
		display.Name = 'Harmonic Oscillator';
		display.NumberTitle = 'off';

		hold on;

		time = 0.0:step:5.0;
		realX = exp(-10.0 .* time / 14.0) .* cos(time .* sqrt(1000.0/7.0 - 100.0/196.0));

		legends = {'Solucion Analitica'};
		plot(time, realX);
		for k = 1:size(x, 2)
			plot(time, x(:, k));
			legends{end + 1} = sources{k}{2};
		end

		display.CurrentAxes.Title.String = ['Oscilador Armonico Amortiguado'];
		display.CurrentAxes.Title.FontSize = 16;
		display.CurrentAxes.Title.FontWeight = 'bold';
		display.CurrentAxes.Title.Color = [0, 0, 0];
		display.CurrentAxes.XLabel.String = 'Tiempo [s]';
		display.CurrentAxes.XLabel.FontSize = 16;
		display.CurrentAxes.XLabel.FontWeight = 'bold';
		display.CurrentAxes.XLabel.Color = [0, 0, 0];
		display.CurrentAxes.YLabel.FontSize = 16;
		display.CurrentAxes.YLabel.FontWeight = 'bold';
		display.CurrentAxes.YLabel.Color = [0, 0, 0];
		display.CurrentAxes.XGrid = 'on';
		display.CurrentAxes.YGrid = 'on';
		display.CurrentAxes.FontSize = 13;
		display.CurrentAxes.XLim = [0 5];
		display.CurrentAxes.YLim = [-1 1];
		display.CurrentAxes.YLabel.String = 'Posicion sobre X [m]';
		display.CurrentAxes.addprop('Legend');
		display.CurrentAxes.Legend = legend(legends);
	end
