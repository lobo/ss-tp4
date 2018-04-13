
	%{
		Imprime el gráfico de variación del módulo de la velocidad del
		Voyager-1 a lo largo del tiempo. La partícula correspondiente a la
		nave debe ser la primera de todas.

		@param source
			El nombre del archivo.
		@param step
			El paso temporal.
		@maxTime
			El tiempo máximo de simulación que contiene el archivo.
		@bodies
			Cantidad de partículas presentes en la simulación.
	%}

	function [] = speed(source, step, maxTime, bodies)

		disp(['Reading ', source, ' ...']);
		xyrvv = importdata(source);

		time = (0.0:step:maxTime)';
		S(:, 1:2) = xyrvv(1 + bodies .* (0:size(time, 1) - 1), 4:5);
		S = S .* S;
		S = sqrt(S(:, 1) + S(:, 2));

		% Begin plotting...

		display = figure();
		display.Name = 'Gravitational Field';
		display.NumberTitle = 'off';

		hold on;

		plot(time, S(:, 1));

		display.CurrentAxes.Title.String = ['Voyager-1: Speed (\Deltat = ', num2str(step), ' [s])'];
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
		display.CurrentAxes.XLim = [0 maxTime];
		display.CurrentAxes.YLim = [0 Inf];
		display.CurrentAxes.YLabel.String = 'Modulo de la Velocidad [m/s]';
	end
