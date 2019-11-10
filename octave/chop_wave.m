# soundcloud like wave visualization
function chop_wave(input_file, step_in_sec)

   ai = audioread(input_file);
   left = ai(:,1);

   sample_rate = 44100;
   length_in_s = length(left) / (step_in_sec * sample_rate)  # in seconds

   pavgs = zeros(1, length_in_s);
   navgs = zeros(1, length_in_s);

   for i=1:length_in_s
      second_slice = left((i-1)*sample_rate+1 : i*sample_rate);
      positive = find(second_slice >= 0);
      pavgs(i) = mean(second_slice(positive));
      negative = find(second_slice < 0);
      navgs(i) = mean(second_slice(negative));
   end

   navgs /= 2;  # no idea how soundcloud made negatives

   figure;
   plot(pavgs);
   hold on;
   plot(navgs);
   xlabel('t [s]');
   title('chop\_wave output');

   # lets do some experiments with bar
   figure;
   bar(pavgs);
   hold on;
   bar(navgs);

endfunction
