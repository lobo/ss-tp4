
	%{
		Imprime el gráfico de variación de la distancia del Voyager-1 a lo
		largo del tiempo hacia una partícula cualquiera del sistema solar. La
		partícula correspondiente a la nave debe ser la primera de todas.

		@param source
			El nombre del archivo.
		@param step
			El paso temporal.
		@maxTime
			El tiempo máximo de simulación que contiene el archivo.
		@bodies
			Cantidad de partículas presentes en la simulación.
		@target
			ID de la partícula hacia la cual determinar la distancia. Para N
			partículas, se debe cumplir 0 <= ID < N.
	%}

	function [] = distance(source, step, maxTime, bodies, target)

		% Unidad Astronómica:
		AU = 149597870700.0;
		secondsByDay = 3600.0 * 24.0;

		body = {
			'Voyager-1',
			'Sun',
			'Mercury',
			'Venus',
			'Earth',
			'Mars',
			'Jupiter',
			'Saturn'
			%'Uranus',
			%'Neptune'
		};

		disp(['Reading ', source, ' ...']);
		xyrvv = importdata(source);

		time = (0.0:step:maxTime)';
		Pv(:, 1:2) = xyrvv(1 + bodies .* (0:size(time, 1) - 1), 1:2);
		Pt(:, 1:2) = xyrvv(1 + target + bodies .* (0:size(time, 1) - 1), 1:2) - Pv;
		Pt = Pt .* Pt;
		Pt = sqrt(Pt(:, 1) + Pt(:, 2)) / AU;

		[minPt, index] = min(Pt, [], 1);

		% Begin plotting...

		display = figure();
		display.Name = 'Gravitational Field';
		display.NumberTitle = 'off';

		hold on;

		plot(time ./ secondsByDay, Pt(:, 1));
		scatter(time(index, 1) / secondsByDay, minPt, 100, 'o', 'filled');

		display.CurrentAxes.Title.String = ['Voyager-1: Distance to ', body{target + 1}, ' (\Deltat = ', num2str(step), ' [s])'];
		display.CurrentAxes.Title.FontSize = 16;
		display.CurrentAxes.Title.FontWeight = 'bold';
		display.CurrentAxes.Title.Color = [0, 0, 0];
		display.CurrentAxes.XLabel.String = 'Time [days]';
		display.CurrentAxes.XLabel.FontSize = 16;
		display.CurrentAxes.XLabel.FontWeight = 'bold';
		display.CurrentAxes.XLabel.Color = [0, 0, 0];
		display.CurrentAxes.YLabel.FontSize = 16;
		display.CurrentAxes.YLabel.FontWeight = 'bold';
		display.CurrentAxes.YLabel.Color = [0, 0, 0];
		display.CurrentAxes.XGrid = 'on';
		display.CurrentAxes.YGrid = 'on';
		display.CurrentAxes.FontSize = 13;
		display.CurrentAxes.XLim = [0 (maxTime/secondsByDay)];
		display.CurrentAxes.YLim = [0 Inf];
		display.CurrentAxes.YLabel.String = 'Distance [AU]';
		display.CurrentAxes.addprop('Legend');
		display.CurrentAxes.Legend = legend({
			'Distance [AU]',
			['Minimum: ', num2str(minPt, '%.4e'), ' [AU]']
		});
		display.CurrentAxes.Legend.Location = 'southeast';
	end
