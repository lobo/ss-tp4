
	function [] = msePlot(source)

		matrix = table2array(readtable(source, ...
					'ReadVariableNames', true, ...
					'FileType', 'text', ...
					'Delimiter', '\t'));

		display = figure();
		display.Name = 'Harmonic Oscillator';
		display.NumberTitle = 'off';

		hold on;

		scatter(matrix(:, 1), log10(matrix(:, 2)), 100, 'o', 'filled');
		scatter(matrix(:, 1), log10(matrix(:, 3)), 100, 'o', 'filled');
		scatter(matrix(:, 1), log10(matrix(:, 4)), 100, 'o', 'filled');
		plot(matrix(:, 1), log10(matrix(:, 2)), '-', 'Color', [0.7, 0.7, 0.7]);
		plot(matrix(:, 1), log10(matrix(:, 3)), '-', 'Color', [0.7, 0.7, 0.7]);
		plot(matrix(:, 1), log10(matrix(:, 4)), '-', 'Color', [0.7, 0.7, 0.7]);

		display.CurrentAxes.Title.String = ['Integrators MSE'];
		display.CurrentAxes.Title.FontSize = 16;
		display.CurrentAxes.Title.FontWeight = 'bold';
		display.CurrentAxes.Title.Color = [0, 0, 0];
		display.CurrentAxes.XLabel.String = '\Deltat [s]';
		display.CurrentAxes.XLabel.FontSize = 16;
		display.CurrentAxes.XLabel.FontWeight = 'bold';
		display.CurrentAxes.XLabel.Color = [0, 0, 0];
		display.CurrentAxes.YLabel.FontSize = 16;
		display.CurrentAxes.YLabel.FontWeight = 'bold';
		display.CurrentAxes.YLabel.Color = [0, 0, 0];
		display.CurrentAxes.XGrid = 'on';
		display.CurrentAxes.YGrid = 'on';
		display.CurrentAxes.FontSize = 13;
		display.CurrentAxes.XLim = [-0.001 0.011];
		display.CurrentAxes.YLim = [-20.0 0.0];
		display.CurrentAxes.YLabel.String = 'log_1_0(MSE)';
		display.CurrentAxes.addprop('Legend');
		display.CurrentAxes.Legend = legend({
			'Velocity Verlet',
			'Beeman',
			'Gear Order-5'
		});
		display.CurrentAxes.Legend.Location = 'southeast';
	end
